package com.thoughtworks.rslist.exception;

public class RsEventNotValidException extends RuntimeException{
    private String errorMsg;

    public RsEventNotValidException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage(){
        return errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
