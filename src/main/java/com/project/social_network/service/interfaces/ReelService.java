package com.project.social_network.service.interfaces;

import com.project.social_network.dto.response.ReelDto;
import com.project.social_network.exception.ReelException;
import com.project.social_network.exception.UserException;
import com.project.social_network.entity.Reel;
import com.project.social_network.entity.User;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ReelService {
  ReelDto createReel(MultipartFile file, String content, User user) throws UserException, IOException;
  List<ReelDto> findAllReel();
  ReelDto findReelById(Long reelId);
  void deleteReelById(Long reelId, Long userId) throws UserException, ReelException;
  List<ReelDto> getUserReel(User user);

}
