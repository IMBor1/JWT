package com.test.JWT.utils;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ModelAndView handleAuthenticationException(AuthenticationException e) {
        ModelAndView modelAndView = new ModelAndView("error/authentication");
        modelAndView.setStatus(HttpStatus.UNAUTHORIZED);
        return modelAndView;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException e) {
        ModelAndView modelAndView = new ModelAndView("error/access-denied");
        modelAndView.setStatus(HttpStatus.FORBIDDEN);
        return modelAndView;
    }
}