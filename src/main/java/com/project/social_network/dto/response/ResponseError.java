package com.project.social_network.dto.response;

public class ResponseError extends ResponseData{

  public ResponseError(int status, String message) {
    super(status, message);
  }
}
