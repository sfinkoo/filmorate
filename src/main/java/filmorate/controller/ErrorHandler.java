package filmorate.controller;

import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHappinessOverflow(ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ResourceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleHappinessOverflow(ResourceException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleHappinessOverflow(Throwable e) {
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}
