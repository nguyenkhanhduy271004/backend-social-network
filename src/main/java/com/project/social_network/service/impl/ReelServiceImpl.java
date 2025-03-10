package com.project.social_network.service.impl;

import com.project.social_network.converter.ReelConverter;
import com.project.social_network.exception.PostException;
import com.project.social_network.exception.ReelException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.entity.Reel;
import com.project.social_network.model.entity.User;
import com.project.social_network.repository.ReelRepository;
import com.project.social_network.service.interfaces.ReelService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReelServiceImpl implements ReelService {

  @Autowired
  private ReelRepository reelRepository;
  @Autowired
  private ReelConverter reelConverter;

  @Override
  public Reel createReel(Reel req, User user) throws UserException {
    Reel reel = reelConverter.reelConverter(req, user);
    return reelRepository.save(reel);
  }

  @Override
  public List<Reel> findAllReel() {
    return reelRepository.findAllByIsDeletedFalse();
  }

  @Override
  public Reel findReelById(Long reelId) {
    return reelRepository.findById(reelId).orElseThrow(() -> new PostException("Reel not found with id: " + reelId));
  }

  @Override
  public void deleteReelById(Long reelId, Long userId) throws UserException, ReelException {
    Reel reel = findReelById(reelId);

    if (!userId.equals(reel.getUser().getId())) {
      throw new UserException("You can't delete another user's reel");
    }

    reelRepository.deleteById(reel.getId());
  }

  @Override
  public List<Reel> getUserReel(User user) {
    return reelRepository.findByUser(user);
  }
}
