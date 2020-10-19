package com.blackhearth.blockchain.startup;

import javafx.util.Pair;

public class DoNthParamService implements ParamService {

    @Override
    public Pair<RunParams, ?> getValue(RunParams param, String[] args, int i) {
        return new Pair<>(param, "");
    }
}
