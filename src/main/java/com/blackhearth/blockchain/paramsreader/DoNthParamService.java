package com.blackhearth.blockchain.paramsreader;

public class DoNthParamService implements ParamService {

    @Override
    public Pair<RunParams, Object> getValue(RunParams param, String[] args, int paramNumber) {
        return new Pair<>(param, "");
    }
}
