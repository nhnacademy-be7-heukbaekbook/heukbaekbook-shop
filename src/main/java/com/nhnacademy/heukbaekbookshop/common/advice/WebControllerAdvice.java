package com.nhnacademy.heukbaekbookshop.common.advice;

import com.nhnacademy.heukbaekbookshop.contributor.exception.ContributorAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.contributor.exception.ContributorNotFoundException;
import com.nhnacademy.heukbaekbookshop.contributor.exception.PublisherAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.contributor.exception.PublisherNotFoundException;
import com.nhnacademy.heukbaekbookshop.image.exception.ImageNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryFeeNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.DeliveryNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.OrderNotFoundException;
import com.nhnacademy.heukbaekbookshop.order.exception.WrappingPaperNotFoundException;
import org.hibernate.metamodel.mapping.ordering.ast.OrderByComplianceViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class WebControllerAdvice {

//    @ExceptionHandler(UnauthorizedException.class)
//    public ResponseEntity<ErrorResponse> handleUnauthorizedException(Exception ex) {
//        ErrorResponse errorResponse = new ErrorResponse("Unauthorized", 401, ZonedDateTime.now());
//        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler({
            ImageNotFoundException.class,
            ContributorNotFoundException.class,
            PublisherNotFoundException.class,
            OrderByComplianceViolation.class,
            DeliveryNotFoundException.class,
            DeliveryFeeNotFoundException.class,
            WrappingPaperNotFoundException.class,
            OrderNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), 404, ZonedDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ContributorAlreadyExistException.class, PublisherAlreadyExistException.class})
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), 400, ZonedDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MissingParameterException.class)
//    public ResponseEntity<ErrorResponse> handleMissingException(Exception ex) {
//        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), 400, ZonedDateTime.now());
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(MissingAcceptHeaderException.class)
//    public ResponseEntity<ErrorResponse> handleMissingAcceptHeaderException(Exception ex) {
//        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), 400, ZonedDateTime.now());
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
}
