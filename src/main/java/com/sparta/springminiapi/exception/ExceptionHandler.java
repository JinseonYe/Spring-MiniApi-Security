package com.sparta.springminiapi.exception;

import com.sparta.springminiapi.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@RestControllerAdvice
public class ExceptionHandler {

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult IllegalArgumentExceptionHandler(IllegalArgumentException e){
//        ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST, e.getMessage());
//        return errorResult;
//    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponseDto IllegalArgumentExceptionHandler(IllegalArgumentException e){
        return new ExceptionResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
}

//    @ExceptionHandler(NotAuthorizedException.class)
//    protected ResponseEntity<String> handleNotAuthorizedException(NotAuthorizedException e) {
//        return new ResponseEntity<>("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
//    }


//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
//    public String IllegalArgumentExceptionHandler(IllegalArgumentException e) {
//        return e.getMessage();
//    }
}
