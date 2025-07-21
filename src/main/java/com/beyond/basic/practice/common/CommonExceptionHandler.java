//package com.beyond.basic.practice.common;
//
//import com.beyond.basic.practice.author.dto.CommonErrorDto;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.util.NoSuchElementException;
//
//@ControllerAdvice
//public class CommonExceptionHandler {
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<?> illegalException(IllegalArgumentException e) {
////                                  <----------------------------응답Body---------------------------->  <-------상태코드------->
//        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<?> noSuchElementException(NoSuchElementException e) {
//        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
//    }
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> validationError(MethodArgumentNotValidException e) {
//        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
//        return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), errorMessage), HttpStatus.BAD_REQUEST);
//    }
//}
