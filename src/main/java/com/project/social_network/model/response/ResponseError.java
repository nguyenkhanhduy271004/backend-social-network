package com.project.social_network.model.response;

public class ResponseError extends ResponseData{

  public ResponseError(int status, String message) {
    super(status, message);
  }
}
