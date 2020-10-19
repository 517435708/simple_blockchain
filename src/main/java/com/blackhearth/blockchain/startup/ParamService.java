package com.blackhearth.blockchain.startup;

import javafx.util.Pair;

public interface ParamService {
    Pair<RunParams, ?> getValue(RunParams param, String[] args, int i);
}
