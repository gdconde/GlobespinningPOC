package com.globespinning.ionic.locationmanager.models;

public class PluginException extends Exception {

    private Integer code;

    public PluginException(String message, int code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
