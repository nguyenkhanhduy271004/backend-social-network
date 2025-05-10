package com.project.social_network.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.project.social_network.dto.ReelDto;
import com.project.social_network.model.Reel;
import com.project.social_network.model.User;
import com.project.social_network.util.ReelUtil;

@Component
public class ReelConverter {

  private final ReelUtil reelUtil;
  private final ModelMapper modelMapper;

  public ReelConverter(ReelUtil reelUtil, ModelMapper modelMapper) {
    this.reelUtil = reelUtil;
    this.modelMapper = modelMapper;
  }

  public Reel reelConverter(Reel reel, User user) {
    if (reel == null || user == null) {
      return null;
    }

    Reel newReel = modelMapper.map(reel, Reel.class);
    newReel.setUser(user);
    return newReel;
  }

  public ReelDto toReelDto(Reel reel, User reqUser) {
    if (reel == null || reqUser == null) {
      return null;
    }

    ReelDto reelDto = modelMapper.map(reel, ReelDto.class);

    ReelDto.User userDto = new ReelDto.User();
    userDto.setId(reel.getUser().getId());
    userDto.setFullName(reel.getUser().getFullName());
    reelDto.setUser(userDto);

    reelDto.setTotalLikes(reel.getLikes() != null ? reel.getLikes().size() : 0);
    reelDto.setLiked(reelUtil.isLikedByReqUser(reqUser, reel));

    return reelDto;
  }
}