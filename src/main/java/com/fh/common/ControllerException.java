package com.fh.common;

import com.fh.common.exception.CountException;
import com.fh.common.exception.NoLogException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerException {

    /*@ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonData handleException(Exception e) {

        return JsonData.getJsonError(e.getMessage());
    }*/
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonData handleException(Exception e){

        return JsonData.getJsonError(e.getMessage());
    }


    @ExceptionHandler(NoLogException.class)
    @ResponseBody
    public JsonData handlerNoLoginException(NoLogException e){
        return JsonData.getJsonEror(1000,e.getMessage());
    }
    @ExceptionHandler(CountException.class)
    @ResponseBody
    public JsonData handlerCountException(CountException e){
        return JsonData.getJsonEror(2000,e.getMessage());
    }



}
