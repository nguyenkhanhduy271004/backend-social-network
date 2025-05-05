package com.project.social_network.service.interfaces;

import com.project.social_network.dto.ReelDto;
import com.project.social_network.exceptions.ReelException;
import com.project.social_network.exceptions.UserException;
import com.project.social_network.model.User;
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
