package com.mq.domain;

import java.io.IOException;

/*
@author Sridhar
 */
public interface Validator {
    void validate() throws ValidationException, IOException;
}
