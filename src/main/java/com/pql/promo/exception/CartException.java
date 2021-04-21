package com.pql.promo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by pasqualericupero on 21/04/2021.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CartException extends RuntimeException {

    public CartException(String message) {
        super(message);
    }

    public CartException(String message, Throwable cause) {
        super(message, cause);
    }
}
