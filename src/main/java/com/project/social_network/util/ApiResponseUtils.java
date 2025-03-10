package com.project.social_network.util;

import com.project.social_network.model.response.ApiResponse;
import java.util.List;

public class ApiResponseUtils {

  public static <T> ApiResponse<T> buildResponse(
      boolean success,
      String message,
      T data,
      List<String> errors,
      int errorCode
  ) {
    return ApiResponse.<T>builder()
        .success(success)
        .message(message)
        .data(data)
        .errors(errors)
        .errorCode(errorCode)
        .timestamp(System.currentTimeMillis())
        .build();
  }

  public static <T> ApiResponse<T> success(String message, T data) {
    return buildResponse(true, message, data, null, 0);
  }

  public static <T> ApiResponse<T> error(String message, List<String> errors, int errorCode) {
    return buildResponse(false, message, null, errors, errorCode);
  }
}