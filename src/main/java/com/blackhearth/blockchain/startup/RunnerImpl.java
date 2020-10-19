package com.blackhearth.blockchain.startup;

import javafx.util.Pair;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class RunnerImpl implements Runner {

    @Override
    public Map<RunParams, Object> getParams(String[] args) {
        Map<RunParams, Object> paramsMap = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            RunParams runParams = RunParams.getByValue(args[i]);
            manageArg(runParams, args, i, paramsMap);
        }

        return paramsMap;
    }

    @SneakyThrows
    private void manageArg(RunParams param, String[] args, int i, Map<RunParams, Object> map) {
        Class<?> paramService = param.getParamService();
        Constructor<?> declaredConstructor = paramService.getDeclaredConstructor();
        ParamService o = (ParamService) declaredConstructor.newInstance();

        Object value = o.getValue(param, args, i).getValue();
        map.put(param, value);
    }
}
