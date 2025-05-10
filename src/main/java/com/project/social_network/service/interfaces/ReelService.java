package com.project.social_network.service.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.social_network.dto.ReelDto;
import com.project.social_network.model.User;

public interface ReelService {
  ReelDto createReel(MultipartFile file, String content, User user) throws IOException;

  List<ReelDto> findAllReel();

  ReelDto findReelById(Long reelId);

  void deleteReelById(Long reelId, Long userId);

  List<ReelDto> getUserReel(User user);

}
