package com.mimacom.ai.mock.spring.controller.exceptionhandler;


import com.mimacom.ai.mock.exception.UnkownErrorException;
import com.mimacom.ai.mock.exception.WrongUserPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PasswordUpdateExceptionHanlder {


    @ExceptionHandler(WrongUserPasswordException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void wrongUserPasswordAdvice(WrongUserPasswordException ex) {

    }


    @ExceptionHandler(UnkownErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void genericErrorAdvice(UnkownErrorException ex) {

    }
}
