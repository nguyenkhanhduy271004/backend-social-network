package com.project.social_network.models.responses;

public class ResponseError extends ResponseData{

  public ResponseError(int status, String message) {
    super(status, message);
  }
}
