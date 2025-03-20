//package com.project.social_network.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//
//@Configuration
//public class OAuth2ClientConfig {
//
//  @Bean
//  public ClientRegistrationRepository clientRegistrationRepository() {
//    ClientRegistration googleRegistration = ClientRegistration.withRegistrationId("google")
//        .clientId("YOUR_GOOGLE_CLIENT_ID")
//        .clientSecret("YOUR_GOOGLE_CLIENT_SECRET")
//        .scope("profile", "email")
//        .authorizationUri("https://accounts.google.com/o/oauth2/auth")
//        .tokenUri("https://oauth2.googleapis.com/token")
//        .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//        .userNameAttributeName("sub")
//        .clientName("Google")
//        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//        .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//        .build();
//
//    return new InMemoryClientRegistrationRepository(googleRegistration);
//  }
//}
