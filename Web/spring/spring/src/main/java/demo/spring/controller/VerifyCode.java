package demo.spring.controller;

import java.util.HashMap;

public class VerifyCode {
    private VerifyCode verifyCode;
    private static HashMap<String,HashMap<String,Object>> vCode=new HashMap<String, HashMap<String,Object>>();

    public static void insertvCode(String username, HashMap<String, Object> resultMap){
        vCode.put(username,resultMap);
    }

    public static HashMap<String,Object> queryvCode(String username){
        return vCode.get(username);
    }
}
