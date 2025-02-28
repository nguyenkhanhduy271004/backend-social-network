package com.project.social_network.converter;

import com.project.social_network.models.dtos.ReelDto;
import com.project.social_network.models.dtos.UserDto;
import com.project.social_network.models.entities.Reel;
import com.project.social_network.models.entities.User;
import com.project.social_network.utils.ReelUtil;
import java.time.ZoneId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReelConverter {

  @Autowired
  private UserConverter userConverter;
  @Autowired
  private ReelUtil reelUtil;

  public Reel reelConverter(Reel reel, User user) {
    ModelMapper modelMapper = new ModelMapper();
    Reel newReel = modelMapper.map(reel, Reel.class);
    newReel.setUser(user);
    return newReel;
  }

  public ReelDto toReelDto(Reel reel, User reqUser) {
    UserDto user = userConverter.toUserDto(reel.getUser());

    boolean isLiked = reelUtil.isLikedByReqUser(reqUser, reel);

    ReelDto reelDto = new ReelDto();
    reelDto.setId(reel.getId());
    reelDto.setContent(reel.getContent());
    reelDto.setCreatedAt(reel.getCreatedDate().toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime());
    reelDto.setImage(reel.getImage());
    reelDto.setTotalLikes(reel.getLikes().size());
    reelDto.setUser(user);
    reelDto.setLiked(isLiked);

    return reelDto;
  }
}
