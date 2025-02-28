package com.project.social_network.services.interfaces;

import com.project.social_network.exception.ReelException;
import com.project.social_network.exception.UserException;
import com.project.social_network.models.entities.Reel;
import com.project.social_network.models.entities.User;
import java.util.List;

public interface ReelService {
  Reel createReel(Reel req, User user) throws UserException;
  List<Reel> findAllReel();
  Reel findReelById(Long reelId);
  void deleteReelById(Long reelId, Long userId) throws UserException, ReelException;
  List<Reel> getUserReel(User user);

}
