package com.project.social_network.exception;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", new Date());
    errorResponse.put("status", HttpServletResponse.SC_BAD_REQUEST);
    errorResponse.put("error", "Bad Request");
    errorResponse.put("message", "Invalid token");
    errorResponse.put("path", request.getRequestURI());

    ObjectMapper objectMapper = new ObjectMapper();
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
