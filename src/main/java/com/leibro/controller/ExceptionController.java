package com.leibro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by leibro on 2017/3/9.
 */
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(RuntimeException.class)
    public String exceptionHandler() {
        return "error";
    }

}
