package com.project.social_network.converter;

import com.project.social_network.dto.response.GroupDto;
import com.project.social_network.dto.response.PostDto;
import com.project.social_network.dto.response.UserDto;
import com.project.social_network.entity.Group;
import com.project.social_network.entity.Post;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupConverter {

  @Autowired
  private UserConverter userConverter;

  @Autowired
  private PostConverter postConverter;




  public GroupDto toGroupDto(Group group) {

    GroupDto groupDto = new GroupDto();

    GroupDto.User user = groupDto.new User();

    user.setId(group.getAdmin().getId());
    user.setName(group.getAdmin().getFullName());
    List<PostDto> postDtos = new ArrayList<>();

    for(Post post:group.getPosts()) {
      PostDto postDto = postConverter.toPostDto(post, post.getUser());
      postDtos.add(postDto);
    }

    groupDto.setId(group.getId());
    groupDto.setName(groupDto.getName());
    groupDto.setAdmin(user);
    groupDto.setPosts(postDtos);

    return groupDto;

  }

  public List<GroupDto> toGroupDtos(List<Group> groups) {
    List<GroupDto> groupDtos = new ArrayList<>();

    for(Group group:groups) {
      GroupDto groupDto = toGroupDto(group);
      groupDtos.add(groupDto);
    }

    return groupDtos;
  }
}
