package org.jsmart.zerocode.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.classpath.ClassPath;
import com.google.classpath.ClassPathFactory;
import com.google.classpath.RegExpResourceFilter;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.text.StrSubstitutor;
import org.jsmart.zerocode.core.di.provider.ObjectMapperProvider;
import org.jsmart.zerocode.core.domain.ScenarioSpec;
import org.jsmart.zerocode.core.engine.assertion.FieldAssertionMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.Charset.defaultCharset;
import static org.jsmart.zerocode.core.engine.assertion.FieldAssertionMatcher.aMatchingMessage;
import static org.jsmart.zerocode.core.engine.assertion.FieldAssertionMatcher.aNotMatchingMessage;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

@Singleton
public class SmartUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmartUtils.class);

    @Inject
    private ObjectMapper mapper; //<--- remember the static methods can not use this objectMapper. So make the methods non-static if you want to use this objectMapper.

    @Inject @Named("YamlMapper")
    private ObjectMapper yamlMapper;

    public <T> String getItRight() throws IOException {
        String jsonAsString = mapper.toString();
        return jsonAsString;
    }

    public <T> String getJsonDocumentAsString(String fileName) throws IOException {
        String jsonAsString = Resources.toString(getClass().getClassLoader().getResource(fileName), StandardCharsets.UTF_8);
        return jsonAsString;
    }

    public static String readJsonAsString(String jsonFile){
        try {
            return Resources.toString(Resources.getResource(jsonFile), defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while reading the JSON file - " + jsonFile);
        }
    }

    public static String readYamlAsString(String yamlFile){
        return readJsonAsString(yamlFile);
    }

    public Map<String, Object> readJsonStringAsMap(String json) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});

        return map;
    }

    public static List<String> getAllEndPointFiles(String packageName) {
        ClassPathFactory factory = new ClassPathFactory();
        ClassPath jvmClassPath = factory.createFromJVM();
        String[] allSimulationFiles = jvmClassPath.findResources(packageName, new RegExpResourceFilter(".*", ".*\\.json$"));
        if (null == allSimulationFiles || allSimulationFiles.length == 0) {
            throw new RuntimeException("NothingFoundHereException: Check the (" + packageName + ") integration test repo folder(empty?). ");
        }

        return Arrays.asList(allSimulationFiles);
    }


    public <T> T scenarioFileToJava(String scenarioFile, Class<T> clazz) throws IOException {
        if(scenarioFile.endsWith(".yml") || scenarioFile.endsWith(".yaml")){
            return yamlMapper.readValue(readYamlAsString(scenarioFile), clazz);
        }

        return mapper.readValue(readJsonAsString(scenarioFile), clazz);
    }

    public static void main(String[] args) {

//        File f = new File("/Path/To/File/or/Directory");
//        String path = "/Users/nirmalchandra/dev/ZEROCODE_REPOS/zerocode/core/src/main/resources/reports";
        String path = "~/dev/ZEROCODE_REPOS/zerocode/core/src/main/resources/reports";
//        String path = "~/dev/ZEROCODE_REPOS/zerocode/core/src/main/resources/engine/request_respone_actual.json";
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            System.out.println("dir exists");
        } else {
            System.out.println("no such dir");
        }


        try {
            Path path1 = Paths.get(path);
            System.out.println("Absolute path:" + path1.toAbsolutePath());

            if(path1.toFile().exists() && path1.toFile().isDirectory()){
                System.out.println("It's a dir...");
            } else {
                System.out.println("It's a file(not a dir");
            }
        } catch (InvalidPathException | NullPointerException ex) {
            System.out.println("no such dir or path");
        }
        System.out.println("dir or path exists");
    }

    public List<ScenarioSpec> getScenarioSpecListByPackage(String packageName) {
        List<String> allEndPointFiles = getAllEndPointFiles(packageName);
        List<ScenarioSpec> scenarioSpecList = allEndPointFiles.stream()
                .map(testResource -> {
                    try {
                        return scenarioFileToJava(testResource, ScenarioSpec.class);
                    } catch (IOException e) {
                        throw new RuntimeException("Exception while deserializing to Spec. Details: " + e);
                    }
                })
                .collect(Collectors.toList());

        return scenarioSpecList;
    }

    public void checkDuplicateScenarios(String testPackageName) {
        Set<String> oops = new HashSet<>();

        getScenarioSpecListByPackage(testPackageName).stream()
                .forEach(scenarioSpec -> {
                    if (!oops.add(scenarioSpec.getScenarioName())) {
                        throw new RuntimeException("Oops! Can not run with multiple Scenarios with same name. Found duplicate: " + scenarioSpec.getScenarioName());
                    }

                    /**
                     * Add this if project needs to avoid duplicate step names
                     */
                    /*Set<String> oops = new HashSet<>();
                    scenarioSpec.getSteps()
                            .forEach(step -> {
                                if(!oops.add(step.getName())){
                                    throw new RuntimeException("Oops! Avoid same step names. Duplicate found: " + step.getName());
                                }
                            });*/
                });
    }

    public static String prettyPrintJson(String jsonString) {
        final ObjectMapper objectMapper = new ObjectMapperProvider().get();
        try {
            final JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

        } catch (IOException e) {
            /*
             *  Pretty-print logic threw an exception, not a big deal, print the original json then.
             */
            LOGGER.error("Non-JSON content was encountered. So pretty print did not format it and returned the raw text");
            return jsonString;
        }

    }

    public static String prettyPrintJson(JsonNode jsonNode) {
        String indented = jsonNode.toString();

        final ObjectMapper objectMapper = new ObjectMapperProvider().get();

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

        } catch (IOException e) {
            /*
             *  Pretty-print logic threw an exception, not a big deal, print the original json then.
             */
            return indented;
        }
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public static String resolveToken(String stringWithToken, Map<String, String> paramMap) {
        StrSubstitutor sub = new StrSubstitutor(paramMap);
        return sub.replace(stringWithToken);
    }

    public static String getEnvPropertyValue(String envPropertyKey){

        final String propertyValue = System.getProperty(envPropertyKey);

        if (propertyValue != null) {
            return propertyValue;
        } else {
            return System.getenv(envPropertyKey);
        }
    }
}
