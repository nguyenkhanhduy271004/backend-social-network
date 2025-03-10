package com.project.social_network.service.interfaces;

import com.project.social_network.exception.ReelException;
import com.project.social_network.exception.UserException;
import com.project.social_network.model.entity.Reel;
import com.project.social_network.model.entity.User;
import java.util.List;

public interface ReelService {
  Reel createReel(Reel req, User user) throws UserException;
  List<Reel> findAllReel();
  Reel findReelById(Long reelId);
  void deleteReelById(Long reelId, Long userId) throws UserException, ReelException;
  List<Reel> getUserReel(User user);

}
