package com.blackhearth.blockchain.paramsreader;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ParamsReaderTest {

    @Test
    void getFirstKnown() {
        //given
        String[] args = getArgs("-fn 192.168.1.288");

        // when
        Map<RunParams, Object> params = ParamsReader.getParams(args);

        //then
        assertThat(params)
                .hasSize(2)
                .containsKeys(RunParams.FIRST_KNOWN, RunParams.UNKNOWN);

        assertThat(params.get(RunParams.FIRST_KNOWN)).isEqualTo("192.168.1.288");
    }

    @Test
    void getUnknownOnlyWhenEmpty() {
        // given
        String[] args = getArgs("");

        // when
        Map<RunParams, Object> params = ParamsReader.getParams(args);

        //then
        assertThat(params)
                .hasSize(1)
                .containsKeys(RunParams.UNKNOWN);
    }

    @Test
    void getUnknownOnlyWhenUnknownParams() {
        // given
        String[] args = getArgs("-cp -abc xd Temp -yy xD xDXDXDXD");

        // when
        Map<RunParams, Object> params = ParamsReader.getParams(args);

        //then
        assertThat(params)
                .hasSize(1)
                .containsKeys(RunParams.UNKNOWN);
    }


    private String[] getArgs(String consoleLine) {
        return consoleLine.split("\\s");
    }
}