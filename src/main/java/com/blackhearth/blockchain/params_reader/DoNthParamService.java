package com.blackhearth.blockchain.params_reader;

public class DoNthParamService implements ParamService {

    @Override
    public Pair<RunParams, ?> getValue(RunParams param, String[] args, int paramNumber) {
        return new Pair<>(param, "");
    }
}
