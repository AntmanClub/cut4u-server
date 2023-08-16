package antmanclub.cut4userver.user.dto;

import antmanclub.cut4userver.user.domain.User;
import lombok.Getter;

@Getter
public class FollowerListResponseDto {
    private String name;
    private String profileimg;

    public FollowerListResponseDto(User entity) {
        this.name = entity.getName();
        this.profileimg = entity.getProfileimg();
    }
}
