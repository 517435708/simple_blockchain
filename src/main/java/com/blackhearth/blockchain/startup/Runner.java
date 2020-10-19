package com.blackhearth.blockchain.startup;

import java.util.Map;

public interface Runner {
    Map<RunParams, ?> getParams(String[] args);
}
