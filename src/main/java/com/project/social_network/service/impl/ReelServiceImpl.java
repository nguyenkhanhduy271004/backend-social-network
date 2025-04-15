package com.project.social_network.service.impl;

import com.project.social_network.converter.ReelConverter;
import com.project.social_network.model.dto.ReelDto;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.ReelException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.entity.Reel;
import com.project.social_network.model.entity.User;
import com.project.social_network.repository.ReelRepository;
import com.project.social_network.service.interfaces.ReelService;
import com.project.social_network.service.interfaces.UploadImageFile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReelServiceImpl implements ReelService {

  @Autowired
  private ReelRepository reelRepository;
  @Autowired
  private ReelConverter reelConverter;
  @Autowired
  private UploadImageFile uploadImageFile;

  @Override
  public ReelDto createReel(MultipartFile file, String content, User user) throws UserException, IOException {
    String imageFileUrl = null;
    if (file != null && !file.isEmpty()) {
      imageFileUrl = uploadImageFile.uploadImage(file);
    }
    Reel reel = new Reel();
    reel.setContent(content);
    reel.setImage(imageFileUrl);
    reel.setUser(user);

    Reel savedReel = reelRepository.save(reel);
    return reelConverter.toReelDto(savedReel, savedReel.getUser());
  }

  @Override
  public List<ReelDto> findAllReel() {

    return reelRepository.findAllByIsDeletedFalse()
        .stream()
        .map((reel) -> reelConverter.toReelDto(reel, reel.getUser()))
        .collect(Collectors.toList());
  }

  @Override
  public ReelDto findReelById(Long reelId) {
    Reel reel = reelRepository.findById(reelId).orElseThrow(() -> new PostException("Reel not found with id: " + reelId));
    return reelConverter.toReelDto(reel, reel.getUser());
  }

  public Reel findReelById2(Long reelId) {
    return reelRepository.findById(reelId).orElseThrow(() -> new PostException("Reel not found with id: " + reelId));
  }

  @Override
  public void deleteReelById(Long reelId, Long userId) throws UserException, ReelException {
    Reel reel = findReelById2(reelId);

    if (!userId.equals(reel.getUser().getId())) {
      throw new UserException("You can't delete another user's reel");
    }

    reelRepository.deleteById(reel.getId());
  }

  @Override
  public List<ReelDto> getUserReel(User user) {

    return reelRepository.findByUser(user)
        .stream()
        .map((reel) -> reelConverter.toReelDto(reel, reel.getUser()))
        .collect(Collectors.toList());
  }
}
