package com.xuanyuan.common.utils;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
@Configuration
public class PropertyHolder implements EnvironmentAware {
    private static Environment env;

    public static Object getProperty(String key) {
        return env.getProperty(key);
    }
    public static Object getProperty(String key,String defaultValue) {
        return env.getProperty(key,defaultValue);
    }
    public static String getString(String key) {
        return env.getProperty(key);
    }
    public static String getString(String key,String defaultValue) {
        return env.getProperty(key);
    }
    public static Integer getInt(String key) {
        return Integer.parseInt(env.getProperty(key));
    }
    public static Integer getInt(String key,Integer defaultValue) {
        if(env.getProperty(key)==null)return defaultValue;
        return Integer.parseInt(env.getProperty(key));
    }
    public static Boolean getBoolean(String key) {
        return Boolean.valueOf(env.getProperty(key));
    }
    public static Boolean getBoolean(String key,Boolean defaultValue) {
        if(env.getProperty(key)==null)return defaultValue;
        return Boolean.valueOf(env.getProperty(key));
    }

    public static String getFilePathSuffix(){
        if("true".equals(getString("upload.remote"))){
            return getString("static.path");
        }else{
            return "";
        }
    }
    public static String getJdbcType(){
        return getString("jdbc.type");
    }
    public static String getProjectPath(){
        return getString("projectPath");
    }

    @Override
    public void setEnvironment(Environment env) {
        PropertyHolder.env = env;
    }

    public static Environment getEnv() {
        return env;
    }
}
