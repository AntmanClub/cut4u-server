package antmanclub.cut4userver.posts.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProfileResponseDto {
    private String name;
    private String email;
    private String profileImg;
    private int postCount;
    private int followerCount;
    private int followingCount;
    private List<PostsDto> postsDtoList = new ArrayList<>();
}
