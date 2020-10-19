package com.blackhearth.blockchain.startup;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.EnumMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamsReader {

    public static Map<RunParams, Object> getParams(String[] args) {
        Map<RunParams, Object> paramsMap = new EnumMap<>(RunParams.class);

        for (int i = 0; i < args.length; i++) {
            RunParams runParams = RunParams.getByValue(args[i]);
            manageArg(runParams, args, i, paramsMap);
        }

        return paramsMap;
    }

    @SneakyThrows
    private static void manageArg(RunParams param, String[] args, int i, Map<RunParams, Object> map) {
        Class<?> paramService = param.getParamService();
        Constructor<?> declaredConstructor = paramService.getDeclaredConstructor();
        ParamService o = (ParamService) declaredConstructor.newInstance();

        Object value = o.getValue(param, args, i).getValue();
        map.put(param, value);
    }
}
