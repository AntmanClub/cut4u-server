package antmanclub.cut4userver.user.service;

import antmanclub.cut4userver.config.SecurityConfig;
import antmanclub.cut4userver.follow.domain.Follow;
import antmanclub.cut4userver.follow.repository.FollowRepository;
import antmanclub.cut4userver.user.SemiToken.CurrentUser;
import antmanclub.cut4userver.user.domain.User;
import antmanclub.cut4userver.user.dto.*;
import antmanclub.cut4userver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig = new SecurityConfig();
    private final CurrentUser currentUser;
    private final FollowRepository followRepository;

    @Transactional
    public String login(LoginRequestDto loginRequestDto) {
        userRepository.findByEmail(loginRequestDto.getEmail()).ifPresent(m -> {
            if(!securityConfig.getPasswordEncoder().matches(loginRequestDto.getPassword(), m.getPassword())){
                throw new IllegalStateException("비밀번호가 틀렸습니다");
            }
        });
        //비밀번호가 맞으면 해당 이메일은 고유하므로 로그인 성공
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다"));
        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());
        return user.getName();
    }

    @Transactional
    public SuccessResponseDto join(JoinRequestDto requestDto) {
        userRepository.findByName(requestDto.getName()).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 이름입니다.");
        });
        userRepository.findByEmail(requestDto.getEmail()).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
        if (!Objects.equals(requestDto.getPassword(), requestDto.getConfirmPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        System.out.println(requestDto.getPassword());
        String encodePw = securityConfig.getPasswordEncoder().encode(requestDto.getPassword());
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(encodePw);
        user.setName(requestDto.getName());
        user.setProfileimg(""); //나중에 기본 이미지로 바꿔줘야함
        userRepository.save(user);
        return SuccessResponseDto.builder().success(true).build();
    }

    @Transactional
    public SuccessResponseDto emailDupleCheck(String email) {
        userRepository.findByEmail(email).ifPresent(m -> {
            throw new IllegalArgumentException("중복된 이메일이 있습니다.");
        });
        return SuccessResponseDto.builder().success(true).build();
    }

    @Transactional
    public UserProfileUpdateResponseDto editProfile(UserProfileUpdateRequestDto userProfileUpdateRequestDto) {
        userRepository.findByName(userProfileUpdateRequestDto.getName())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("이미 있는 이름입니다.");
                });
        User user = userRepository.findByEmail(userProfileUpdateRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이에일을 가진 유저가 없습니다"));
        user.setName(userProfileUpdateRequestDto.getName());
        user.setProfileimg(userProfileUpdateRequestDto.getProfileimg());
        return UserProfileUpdateResponseDto.builder()
                .name(userProfileUpdateRequestDto.getName())
                .profileimg(userProfileUpdateRequestDto.getProfileimg())
                .build();
    }

    @Transactional
    public SuccessResponseDto userFollow(UserFollowRequestDto userFollowRequestDto) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("접속중인 유저가 존재하지 않습니다."));
        User followingUser = userRepository.findById(userFollowRequestDto.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 id의 유저가 존재하지 않습니다."));
        if (user.getId().equals(followingUser.getId())) {
            throw new IllegalStateException("같은 유저는 팔로우할 수 없습니다.");
        }
        Follow follow = new Follow();
        follow.setFollowee(user);
        follow.setFollower(followingUser);
        followRepository.findByFolloweeAndFollower(user, followingUser)
                .ifPresent(m -> {
                    throw new IllegalArgumentException("이미 팔로우한 사용자 입니다.");
                });
        followRepository.save(follow);
        user.addFollowing(follow);
        user.addFollower(follow);
        followingUser.addFollower(follow);
        followingUser.addFollowing(follow);
        return SuccessResponseDto.builder().success(true).build();
    }
    @Transactional
    public SuccessResponseDto userUnfollow(UserFollowRequestDto userFollowRequestDto) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("접속중인 유저가 존재하지 않습니다."));
        User followingUser = userRepository.findById(userFollowRequestDto.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 id의 유저가 존재하지 않습니다."));
        if (user.getId().equals(followingUser.getId())) {
            throw new IllegalStateException("같은 유저는 언팔할 수 없습니다.");
        }
        Follow follow = new Follow();
        follow = followRepository.findByFolloweeAndFollower(user, followingUser)
                .orElseThrow(()-> new IllegalArgumentException("팔로우 하지 않은 사용자입니다."));
        followRepository.delete(follow);
        return SuccessResponseDto.builder().success(true).build();
    }
    @Transactional
    public List<FollowingListResponseDto> followingList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));
        List<User> followerUsers = user.getFollowers().stream()
                .map(Follow::getFollower)
                .toList();
        return followerUsers.stream()
                .map(FollowingListResponseDto::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<FollowerListResponseDto> followerList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));
        List<User> followingUsers = user.getFollowing().stream()
                .map(Follow::getFollowee)
                .toList();
        return followingUsers.stream()
                .map(FollowerListResponseDto::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<UserListResponseDto> searchName(String name) {
        List<User> users = userRepository.findByNameContaining(name);
        //팔로워 순으로 내림차순 정렬
        users.sort((u1, u2)-> u2.getFollowing().size() - u1.getFollowing().size());
        return users.stream().map(user->{
            UserListResponseDto dto = new UserListResponseDto();
            dto.setEmail(user.getEmail());
            dto.setName(user.getName());
            dto.setProfileImg(user.getProfileimg());
            return dto;
        }).collect(Collectors.toList());
    }
}
