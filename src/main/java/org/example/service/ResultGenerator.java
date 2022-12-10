package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.internal.StringUtil;
import org.example.entity.User;
import org.springframework.http.HttpRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultGenerator {

    public static Map<String, Object> genSuccessResult(){
        return getModelData(200, "success", null, null, null);
    }

    public static Map<String, Object> genSuccessResult(Map<String, Object> dataMap){
        return getModelData(200, "success", dataMap);
    }

    public static Map<String, Object> genSuccessResult(User user){
        return getModelData(200, "success", user, null, null);
    }

    public static Map<String, Object> genSuccessResult(String message){
        return getModelData(200, message, null, null, null);
    }

    public static Map<String, Object> genSuccessResult(List<User> userList, long current, long size) {
        Map<String, Object> modelMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("users", userList);
        dataMap.put("page", current);
        dataMap.put("size", size);

        modelMap.put("code", 200);
        modelMap.put("message", "success");
        modelMap.put("data", dataMap);
        return modelMap;
    }

    public static Map<String, Object> genFailResult(int state){
        Map<String, Object> modelMap = new HashMap<>();
        if (state == -1) {
            modelMap = getModelData(401, "fail", null, null, "用户名或密码错误！");
        }
        else if (state == -2) {
            modelMap = getModelData(403, "fail", null, null, "操作权限不足！");
        }
        else if (state == -3) {
            modelMap = getModelData(400, "fail", null, null, "用户名已存在！");
        } else
            modelMap = getModelData(500, "fail", null, null, "error code: "+String.valueOf(state));
        return modelMap;
    }

    public static Map<String, Object> genFailResult(BindingResult bindingResult){
        return genFailResult(401, bindingResult);
    }

    public static Map<String, Object> genFailResult(int code, BindingResult bindingResult){
        Map<String, Object> modelMap;
        String errorMessage = "";
        for (ObjectError error : bindingResult.getAllErrors()) {
            errorMessage = errorMessage + error.getDefaultMessage() + ";";
        }
        modelMap = getModelData(code, "fail", null, null, errorMessage);
        return modelMap;
    }

    public static Map<String, Object> genFailResult(int code, String errorMessage){
        Map<String, Object> modelMap;
        modelMap = getModelData(401, "fail", null, null, errorMessage);
        return modelMap;
    }

    public static String genJsonFailResult(int code, String errorMessage) throws JsonProcessingException {
        Map<String, Object> modelMap;
        modelMap = getModelData(code, "fail", null, null, errorMessage);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(modelMap);
    }

    private static Map<String, Object> getModelData(int code, String message, User user, String token, String errorMessage) {
        Map<String, Object> modelMap = new HashMap<>();

        Map<String, Object> dataMap = new HashMap<>();
        if (user != null) {dataMap.put("user", user);}
        if (!StringUtil.isNullOrEmpty(token)) {dataMap.put("token", token);}
        if (!StringUtil.isNullOrEmpty(errorMessage)) {dataMap.put("errorMessage", errorMessage);}

        modelMap.put("code", code);
        modelMap.put("message", message);
        modelMap.put("data", dataMap);
        return modelMap;
    }

    private static Map<String, Object> getModelData(int code, String message, Map<String, Object> dataMap) {
        Map<String, Object> modelMap = new HashMap<>();

        modelMap.put("code", code);
        modelMap.put("message", message);
        modelMap.put("data", dataMap);
        return modelMap;
    }
}
