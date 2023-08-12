package antmanclub.cut4userver.posts.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;
@Getter
@NoArgsConstructor
public class SearchPostsResponseDto {
    private Long id;
    private String frameImg;
    private List<String> postImages;
    @Builder
    public SearchPostsResponseDto(Long id, String frameImg, List<String> postImages){
        this.id = id;
        this.frameImg = frameImg;
        this.postImages = postImages;
    }
}
