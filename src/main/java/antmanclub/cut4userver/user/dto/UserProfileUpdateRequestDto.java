package antmanclub.cut4userver.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileUpdateRequestDto {
    private String email;
    private String name;

    @Builder
    public UserProfileUpdateRequestDto(String email, String name){
        this.email = email;
        this.name = name;
    }
}
