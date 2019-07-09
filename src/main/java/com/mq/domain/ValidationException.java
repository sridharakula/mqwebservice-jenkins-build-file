package com.mq.domain;

/*
@author Sridhar
 */
public class ValidationException extends RuntimeException{
    public ValidationException(String msg) { super(msg); }
    public ValidationException(String msg, Exception exp) { super(msg, exp); }
}
