package com.maryow.assessment.util;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Type;

public class GsonUtil<T extends Object> {
    Gson gson = new Gson();
    Type typeToken;

    public GsonUtil(Class<T> clazz){
        typeToken = clazz;
    }

    public T parserLinkedTreeMapToObject(LinkedTreeMap<?, ?> parameters){
        T model = null;
        try {
            model = gson.fromJson(gson.toJsonTree(parameters), typeToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public T parsetToObject(String json){
        T model = null;

        try {
            model = gson.fromJson(json, typeToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;

    }

    public  String parseToString(T model){
        String result = "";

        try {
            result = gson.toJson(model,model.getClass());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


}
