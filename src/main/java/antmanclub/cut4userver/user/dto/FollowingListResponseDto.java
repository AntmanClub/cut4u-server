package antmanclub.cut4userver.user.dto;

import antmanclub.cut4userver.user.domain.User;
import lombok.Getter;

import java.util.List;

@Getter
public class FollowingListResponseDto {
    private String name;
    private String profileimg;

    public FollowingListResponseDto(User entity) {
        this.name = entity.getName();
        this.profileimg = entity.getProfileimg();
    }
}
