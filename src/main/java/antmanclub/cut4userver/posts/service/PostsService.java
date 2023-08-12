package antmanclub.cut4userver.posts.service;

import antmanclub.cut4userver.aws.AwsUpload;
import antmanclub.cut4userver.posts.domain.Hashtag;
import antmanclub.cut4userver.posts.domain.Posts;
import antmanclub.cut4userver.posts.domain.PostsHashtag;
import antmanclub.cut4userver.posts.dto.*;
import antmanclub.cut4userver.posts.repository.HashtagRepository;
import antmanclub.cut4userver.posts.repository.PostsHashtagRepository;
import antmanclub.cut4userver.posts.repository.PostsRepository;
import antmanclub.cut4userver.user.SemiToken.CurrentUser;
import antmanclub.cut4userver.user.domain.User;
import antmanclub.cut4userver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;
    private final HashtagRepository hashtagRepository;
    private final PostsHashtagRepository postsHashtagRepository;
    private final CurrentUser currentUser;
    private final AwsUpload awsUpload;

    public PostsListResponseDto feed() {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("접속중인 유저가 존재하지 않습니다."));

        // get postslist by user's following users
        List<Posts> postsList = new ArrayList<>();
        user.getFollowers().stream().forEach(follow -> {
            Collections.addAll(postsList, follow.getFollower().getPostsList().toArray(new Posts[0]));
        });
        List<PostsDto> postsDtoList = postsListToPostsDtoList(postsList);    // postsList to postsDtoList

        PostsListResponseDto postsListResponseDto = new PostsListResponseDto(postsDtoList);

        return postsListResponseDto;
    }

    @Transactional
    public PostsListResponseDto add(List<MultipartFile> images, PostsAddRequestDto postsAddRequestDto) {
        // uploads images into aws and get urls
        if(images.size() != 4){
            throw new IllegalArgumentException("사진의 개수가 4개가 아닙니다.");
        }
        List<String> imageUrls = images.stream()
                .map(image -> {
                    try {
                        return awsUpload.upload(image, "image");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        // user entity genarate
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("접속중인 유저가 존재하지 않습니다."));;

        // posts entity genarate
        Posts posts = new Posts();
        posts.setTitle(postsAddRequestDto.getTitle());
        posts.setContent(postsAddRequestDto.getContent());
        posts.setFrameImg(postsAddRequestDto.getFrameImg());
        posts.setImages(imageUrls);
        posts.setUser(user);
        postsRepository.save(posts);
        user.addPosts(posts);

        // hashtag repository insert
        List<String> hashtagNames = postsAddRequestDto.getHashtags();
        hashtagNames.stream().forEach(hashtagName -> {
            Hashtag hashtag = hashtagRepository.findByHashtag(hashtagName)
                    .orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag(hashtagName);
                        return hashtagRepository.save(newHashtag);
                    });

            // posts-hashtag repository insert
            PostsHashtag postsHashtag = new PostsHashtag(posts, hashtag);
            postsHashtagRepository.save(postsHashtag);
        });

        // get postslist by user's following users
        List<Posts> postsList = new ArrayList<>();
        user.getFollowers().stream().forEach(follow -> {
            Collections.addAll(postsList, follow.getFollower().getPostsList().toArray(new Posts[0]));
        });
        List<PostsDto> postsDtoList = postsListToPostsDtoList(postsList);    // postsList to postsDtoList

        return new PostsListResponseDto(postsDtoList);
    }

    @Transactional
    public PostsListResponseDto delete(Long postsId) {
        // delete posts in posts repository
        Posts deletePosts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("접속중인 유저가 존재하지 않습니다."));
        if(deletePosts.getUser().equals(user)){
            postsRepository.delete(deletePosts);
        }else{
            throw new IllegalArgumentException("포스트에 대한 삭제 권한이 없습니다.");
        }
        // get postslist by user's following users
        List<Posts> postsList = new ArrayList<>();
        user.getFollowers().stream().forEach(follow -> {
            Collections.addAll(postsList, follow.getFollower().getPostsList().toArray(new Posts[0]));
        });
        List<PostsDto> postsDtoList = postsListToPostsDtoList(postsList);    // postsList to postsDtoList
        return new PostsListResponseDto(postsDtoList);
    }
    public List<PostsDto> postsListToPostsDtoList(List<Posts> postsList){
        return postsList.stream()
                .map(post -> PostsDto.builder()
                        .userId(post.getUser().getId())
                        .userName(post.getUser().getName())
                        .profileImg(post.getUser().getProfileimg())
                        .postsId(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createTime(post.getCreatedDate())
                        .modifyTime(post.getModifiedDate())
                        .frameImg(post.getFrameImg())
                        .Hashtags(post.getPostsHashtags().stream().map(postsHashtag -> {
                            return postsHashtag.getHashtag().getHashtag();
                        }).collect(Collectors.toList()))
                        .build()
                )
                .collect(Collectors.toList());
    }
    @Transactional
    public ProfileResponseDto userPostsList(String userEmail) {
        // find user with userEmail
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        ProfileResponseDto profileResponseDto = new ProfileResponseDto();
        profileResponseDto.setName(user.getName());
        profileResponseDto.setProfileImg(user.getProfileimg());
        profileResponseDto.setPostCount(user.getPostsList().size());
        profileResponseDto.setFollowerCount(user.getFollowing().size());
        profileResponseDto.setFollowingCount(user.getFollowers().size());
        List<PostsDto> postsDtos = user.getPostsList().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        profileResponseDto.setPostsDtoList(postsDtos);
        return profileResponseDto;
    }
    @Transactional
    public ProfileResponseDto myProfile() {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(()->new IllegalArgumentException("현재 접속중인 유저가 없습니다."));
        ProfileResponseDto profileResponseDto = new ProfileResponseDto();
        profileResponseDto.setName(user.getName());
        profileResponseDto.setProfileImg(user.getProfileimg());
        profileResponseDto.setPostCount(user.getPostsList().size());
        profileResponseDto.setFollowerCount(user.getFollowing().size());
        profileResponseDto.setFollowingCount(user.getFollowers().size());
        List<PostsDto> postsDtos = user.getPostsList().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        profileResponseDto.setPostsDtoList(postsDtos);
        return profileResponseDto;
    }

    private PostsDto convertToDto(Posts post) {
        return PostsDto.builder()
                .userId(post.getUser().getId())
                .userName(post.getUser().getName())
                .profileImg(post.getUser().getProfileimg())
                .postsId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .frameImg(post.getFrameImg())
                .likeCount(post.getLikecount())
                .createTime(post.getCreatedDate())
                .modifyTime(post.getModifiedDate())
                .Hashtags(post.getPostsHashtags().stream()
                        .map(ph -> ph.getHashtag().getHashtag())
                        .collect(Collectors.toList()))
                .build();
    }
}
