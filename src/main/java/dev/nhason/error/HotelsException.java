package dev.nhason.error;

public class HotelsException extends RuntimeException{

    public HotelsException(){}
    public HotelsException(String message) {
        super(message);
    }

    public HotelsException(String message, Throwable cause) {
        super(message, cause);
    }

    public HotelsException(Throwable cause) {
        super(cause);
    }
    protected HotelsException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
