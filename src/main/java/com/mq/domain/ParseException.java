package com.mq.domain;

/*
@author Sridhar
 */
public class ParseException extends RuntimeException{
    public ParseException(String msg) { super(msg); }
    public ParseException(String msg, Exception exp) { super(msg, exp); }
}
