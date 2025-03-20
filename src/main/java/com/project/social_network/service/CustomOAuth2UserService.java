//package com.project.social_network.service;
//
//import com.project.social_network.entity.User;
//import com.project.social_network.enums.AuthProvider;
//import com.project.social_network.repository.UserRepository;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//  private final UserRepository userRepository;
//
//  public CustomOAuth2UserService(UserRepository userRepository) {
//    this.userRepository = userRepository;
//  }
//
//  @Override
//  @Transactional
//  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
//    OAuth2User oAuth2User = super.loadUser(userRequest);
//    return processOAuthUser(oAuth2User, userRequest);
//  }
//
//  private OAuth2User processOAuthUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
//    String email = oAuth2User.getAttribute("email");
//    String name = oAuth2User.getAttribute("name");
//    String imageUrl = oAuth2User.getAttribute("picture");
//
//    AuthProvider provider = userRequest.getClientRegistration().getRegistrationId().equals("google")
//        ? AuthProvider.GOOGLE
//        : AuthProvider.FACEBOOK;
//
//    User user = userRepository.findByEmail(email);
//
//    if (user != null) {
//      user.setFullName(name);
//      user.setImage(imageUrl);
//      user.setAuthProvider(provider);
//      userRepository.save(user);
//    } else {
//      User newUser = new User();
//      newUser.setEmail(email);
//      newUser.setFullName(name);
//      newUser.setImage(imageUrl);
//      newUser.setAuthProvider(provider);
//      userRepository.save(newUser);
//    }
//
//    return oAuth2User;
//  }
//}