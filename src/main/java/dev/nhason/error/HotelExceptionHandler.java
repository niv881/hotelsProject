package dev.nhason.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class HotelExceptionHandler {


    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleResourceNotFound(BadRequestException e){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,e.getMessage());

        problemDetail.setProperty("fieldName",e.getResourceName());
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException e){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,e.getMessage());

        problemDetail.setProperty("fieldName",e.getResourceName());
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

}
