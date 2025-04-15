package com.project.social_network.exception;
import com.project.social_network.model.dto.response.ErrorResponse;
import com.project.social_network.model.dto.response.ResponseError;
import java.io.IOException;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserException.class)
  public ResponseEntity<ResponseError> handleUserException(UserException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
  }

  @ExceptionHandler(PostException.class)
  public ResponseEntity<ResponseError> handlePostException(PostException e) {
    return ResponseEntity.badRequest()
        .body(new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
  }

  @ExceptionHandler(CommentException.class)
  public ResponseEntity<ResponseError> handleCommentException(CommentException e) {
    return ResponseEntity.badRequest()
        .body(new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
  }

  @ExceptionHandler(ReelException.class)
  public ResponseEntity<ResponseError> handleReelException(ReelException e) {
    HttpStatus status = e.getMessage().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status)
        .body(new ResponseError(status.value(), e.getMessage()));
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<ResponseError> handleIOException(IOException e) {
    return ResponseEntity.badRequest()
        .body(new ResponseError(HttpStatus.BAD_REQUEST.value(), "File upload failed: " + e.getMessage()));
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationException(Exception e, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setTimestamp(new Date());
    errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
    errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

    String message = e.getMessage();
    int start = message.lastIndexOf("[");
    int end = message.lastIndexOf("]");
    message = message.substring(start + 1, end - 1);
    errorResponse.setMessage(message);

    return errorResponse;
  }



}