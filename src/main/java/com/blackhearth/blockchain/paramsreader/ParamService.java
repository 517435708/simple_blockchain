package com.blackhearth.blockchain.paramsreader;

public interface ParamService {
    Pair<RunParams, Object> getValue(RunParams param, String[] args, int paramNumber);
}
