package com.blackhearth.blockchain.params_reader;

import javafx.util.Pair;

public interface ParamService {
    Pair<RunParams, ?> getValue(RunParams param, String[] args, int paramNumber);
}
