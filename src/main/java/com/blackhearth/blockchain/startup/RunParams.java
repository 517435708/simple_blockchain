package com.blackhearth.blockchain.startup;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum RunParams {
    FIRST_KNOWN("-fn", TwoArgsParamService.class),
    UNKNOWN("", DoNthParamService.class);

    private String value;
    private Class<? extends ParamService> paramService;

    public static RunParams getByValue(String value) {
        return Stream.of(RunParams.values())
                .filter(en -> en.getValue().equals(value))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
