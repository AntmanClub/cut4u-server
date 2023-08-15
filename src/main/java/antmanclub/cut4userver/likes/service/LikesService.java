package antmanclub.cut4userver.likes.service;

import antmanclub.cut4userver.global.error.ErrorCode;
import antmanclub.cut4userver.global.error.exception.EntityNotFoundException;
import antmanclub.cut4userver.likes.domain.Likes;
import antmanclub.cut4userver.likes.repository.LikesRepository;
import antmanclub.cut4userver.posts.domain.Posts;
import antmanclub.cut4userver.posts.repository.PostsRepository;
import antmanclub.cut4userver.user.SemiToken.CurrentUser;
import antmanclub.cut4userver.user.domain.User;
import antmanclub.cut4userver.user.dto.SuccessResponseDto;
import antmanclub.cut4userver.user.dto.UserListResponseDto;
import antmanclub.cut4userver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;
    @Transactional
    public SuccessResponseDto addLike(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.POSTS_NOT_FOUND,
                        "해당 id의 게시물이 없습니다. id: "+postId));
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "현재 접속중인 유저가 없습니다."));
        //좋아요작 방지
        Optional<Likes> likes = likesRepository.findByPostAndUser(posts, user);
        if (likes.isPresent()) {
            throw new EntityNotFoundException(ErrorCode.CAN_NOT_LIKE_POSTS,
                    "해당 유저는 이미 이 게시물을 좋아했습니다.");
        }else {
            Likes like = Likes.builder()
                    .posts(posts)
                    .user(user)
                    .build();
            likesRepository.save(like);
            posts.addLike(like);
            posts.addLikeCount();
            user.addLikes(like);
        }
        return SuccessResponseDto.builder().success(true).build();
    }
    @Transactional
    public SuccessResponseDto deleteLike(Long postId) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "현재 접속중인 유저가 없습니다."));
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.POSTS_NOT_FOUND,
                        "해당 id의 게시물이 없습니다. id: "+postId));
        Likes likes = likesRepository.findByPostAndUser(posts, user)
                .orElseThrow(()->new EntityNotFoundException(ErrorCode.CAN_NOT_UNLIKE_POSTS,
                        user.getName()+"는 해당 게시물에 좋아요를 누르지 않았습니다."));
        likesRepository.delete(likes);
        posts.subLikeCount();
        return SuccessResponseDto.builder().success(true).build();
    }
    @Transactional
    public List<UserListResponseDto> userList(Long postsId) {
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.POSTS_NOT_FOUND,
                        "해당 id의 게시물이 없습니다. id: "+postsId));
        List<Likes> likes = posts.getLikes();
        return likes.stream().map(user->{
            UserListResponseDto dto = new UserListResponseDto();
            dto.setEmail(user.getUser().getEmail());
            dto.setName(user.getUser().getName());
            dto.setProfileImg(user.getUser().getProfileimg());
            return dto;
        }).collect(Collectors.toList());
    }
}