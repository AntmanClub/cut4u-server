package antmanclub.cut4userver.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class UserProfileUpdateRequestDto {
    private String email;
    private String name;

    @Builder
    public UserProfileUpdateRequestDto(String email, String name, MultipartFile profileimg){
        this.email = email;
        this.name = name;
    }
}
