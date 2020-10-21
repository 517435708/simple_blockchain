package com.blackhearth.blockchain.params_reader;

import javafx.util.Pair;

public class TwoArgsParamService implements ParamService {
    @Override
    public Pair<RunParams, ?> getValue(RunParams param, String[] args, int paramNumber) {
        String secondArgValue = args[paramNumber + 1];
        return new Pair<>(param, secondArgValue);
    }
}