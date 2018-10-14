/*
 * Copyright (C) 2014 jApps Ltd and
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jsmart.zerocode.core.engine.preprocessor;

import org.jsmart.zerocode.core.engine.assertion.AssertionReport;
import org.jsmart.zerocode.core.engine.assertion.JsonAsserter;

import java.util.List;

public interface ZeroCodeJsonTestProcesor {

    String resolveStringJson(String requestJsonAsString, String resolvedScenarioState);

    List<String> getAllTokens(String requestJsonAsString);

    String resolveJsonPaths(String resolvedFromTemplate, String jsonString);

    public List<String> getAllJsonPathTokens(String requestJsonAsString);

    List<JsonAsserter> createAssertersFrom(String resolvedAssertionJson);

    List<AssertionReport> assertAllAndReturnFailed(List<JsonAsserter> asserters, String executionResult);
}
