package com.project.social_network.response;

public class ResponseError extends ResponseData{

  public ResponseError(int status, String message) {
    super(status, message);
  }
}
