package com.blackhearth.blockchain.paramsreader;

public class TwoArgsParamService implements ParamService {
    @Override
    public Pair<RunParams, Object> getValue(RunParams param, String[] args, int paramNumber) {
        String secondArgValue = args[paramNumber + 1];
        return new Pair<>(param, secondArgValue);
    }
}
