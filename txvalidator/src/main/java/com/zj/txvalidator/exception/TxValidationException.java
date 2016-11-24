package com.zj.txvalidator.exception;

/**
 * Created by ASUS on 2016/11/23.
 */

public class TxValidationException extends RuntimeException {
    public TxValidationException() {
        super();
    }

    public TxValidationException(String message) {
        super(message);
    }

    public TxValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TxValidationException(Throwable cause) {
        super(cause);
    }
}
