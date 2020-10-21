package com.blackhearth.blockchain.params_reader;

public interface ParamService {
    Pair<RunParams, ?> getValue(RunParams param, String[] args, int paramNumber);
}
