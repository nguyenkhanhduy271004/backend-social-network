package com.project.social_network.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private boolean success;
  private String message;
  private HttpStatus status;
  private LocalDateTime timestamp;
  private T data;


  public static <T> ApiResponse<T> successNoData(String customMessage, HttpStatus status) {
    return ApiResponse.<T>builder()
        .success(true)
        .message(customMessage)
        .status(status)
        .timestamp(LocalDateTime.now())
        .build();
  }

  public static <T> ApiResponse<T> successWithData(T data, String customMessage, HttpStatus status) {
    return ApiResponse.<T>builder()
        .success(true)
        .message(customMessage)
        .status(status)
        .timestamp(LocalDateTime.now())
        .data(data)
        .build();
  }

  public static <T> ApiResponse<T> error() {
    return ApiResponse.<T>builder()
        .message("ERROR!")
        .success(false)
        .timestamp(LocalDateTime.now())
        .build();
  }

  public static <T> ApiResponse<T> error(HttpStatus status, String customMessage) {
    return ApiResponse.<T>builder()
        .message(customMessage)
        .success(false)
        .status(status)
        .timestamp(LocalDateTime.now())
        .build();
  }
}
