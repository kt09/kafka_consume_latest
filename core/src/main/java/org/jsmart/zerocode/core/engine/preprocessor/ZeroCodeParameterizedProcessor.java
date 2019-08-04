package org.jsmart.zerocode.core.engine.preprocessor;

import org.jsmart.zerocode.core.domain.ScenarioSpec;

public interface ZeroCodeParameterizedProcessor {

    ScenarioSpec processParameterized(ScenarioSpec scenario, int iteration);
}
