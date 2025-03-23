package com.project.social_network.converter;

import com.project.social_network.dto.response.GroupDto;
import com.project.social_network.dto.response.PostDto;
import com.project.social_network.dto.response.UserDto;
import com.project.social_network.entity.Group;
import com.project.social_network.entity.Post;
import com.project.social_network.entity.User;
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

    List<GroupDto.User> members = new ArrayList<>();


    for(User user1 : group.getUsers()) {
      GroupDto.User userDto = groupDto.new User();

      userDto.setId(user1.getId());
      userDto.setName(user1.getFullName());

      members.add(userDto);
    }



    groupDto.setId(group.getId());
    groupDto.setName(group.getName());
    groupDto.setAdmin(user);
    groupDto.setPosts(postDtos);
    groupDto.setMembers(members);
    groupDto.setCreatedDate(group.getCreatedDate());

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
