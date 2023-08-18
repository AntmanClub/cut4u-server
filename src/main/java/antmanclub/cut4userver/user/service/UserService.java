package antmanclub.cut4userver.user.service;

import antmanclub.cut4userver.config.SecurityConfig;
import antmanclub.cut4userver.follow.domain.Follow;
import antmanclub.cut4userver.follow.repository.FollowRepository;
import antmanclub.cut4userver.global.error.ErrorCode;
import antmanclub.cut4userver.global.error.exception.EntityNotFoundException;
import antmanclub.cut4userver.user.SemiToken.CurrentUser;
import antmanclub.cut4userver.user.domain.User;
import antmanclub.cut4userver.user.dto.*;
import antmanclub.cut4userver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EmptyStackException;
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
                throw new EntityNotFoundException(ErrorCode.NOT_CORRECT_PASSWORD,
                        "이메일이 또는 비밀번호가 틀렸습니다.");
            }
        });
        //비밀번호가 맞으면 해당 이메일은 고유하므로 로그인 성공
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "이메일이 또는 비밀번호가 틀렸습니다."));
        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());
        return user.getName();
    }

    @Transactional
    public String join(JoinRequestDto requestDto) {
        userRepository.findByName(requestDto.getName()).ifPresent(m -> {
            throw new EntityNotFoundException(ErrorCode.ALREADY_EXIST_NAME,
                    "이미 존재하는 이름 name: "+requestDto.getName());
        });
        userRepository.findByEmail(requestDto.getEmail()).ifPresent(m -> {
            throw new EntityNotFoundException(ErrorCode.ALREADY_EXIST_EMAIL,
                    "이미 존재하는 이메일 email: "+requestDto.getEmail());
        });
        if (!Objects.equals(requestDto.getPassword(), requestDto.getConfirmPassword())) {
            throw new EntityNotFoundException(ErrorCode.NOT_EQUAL_PASSWORD,
                    "비밀번호가 일치하지 않습니다.");
        }
        System.out.println(requestDto.getPassword());
        String encodePw = securityConfig.getPasswordEncoder().encode(requestDto.getPassword());
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(encodePw);
        user.setName(requestDto.getName());
        user.setProfileimg(""); //나중에 기본 이미지로 바꿔줘야함
        userRepository.save(user);
        return user.getName();
    }

    @Transactional
    public String emailDupleCheck(String email) {
        userRepository.findByEmail(email).ifPresent(m -> {
            throw new EntityNotFoundException(ErrorCode.ALREADY_EXIST_EMAIL,
                    "이미 존재하는 이메일 입니다.");
        });
        return email;
    }

    @Transactional
    public UserProfileUpdateResponseDto editProfile(UserProfileUpdateRequestDto userProfileUpdateRequestDto) {
        userRepository.findByName(userProfileUpdateRequestDto.getName())
                .ifPresent(m -> {
                    throw new EntityNotFoundException(ErrorCode.ALREADY_EXIST_NAME,
                            "이미 존재하는 이름 name: "+userProfileUpdateRequestDto.getName());
                });
        User user = userRepository.findByEmail(userProfileUpdateRequestDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "해당 이에일을 가진 유저가 없습니다 email: "+userProfileUpdateRequestDto.getEmail()));
        user.setName(userProfileUpdateRequestDto.getName());
        user.setProfileimg(userProfileUpdateRequestDto.getProfileimg());
        return UserProfileUpdateResponseDto.builder()
                .name(userProfileUpdateRequestDto.getName())
                .profileimg(userProfileUpdateRequestDto.getProfileimg())
                .build();
    }

    @Transactional
    public String userFollow(UserFollowRequestDto userFollowRequestDto) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "접속중인 유저가 존재하지 않습니다."));
        User followingUser = userRepository.findByName(userFollowRequestDto.getName())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "해당 name의 유저가 존재하지 않습니다. id"+userFollowRequestDto.getName()));
        if (user.getId().equals(followingUser.getId())) {
            throw new EntityNotFoundException(ErrorCode.CAN_NOT_FOLLOW_MYSELF,
                    "접속한 유저 이름: "+user.getName()+"팔로우하려는 유저 이름: "+followingUser.getName());
        }
        Follow follow = new Follow();
        follow.setFollowee(user);
        follow.setFollower(followingUser);
        followRepository.findByFolloweeAndFollower(user, followingUser)
                .ifPresent(m -> {
                    throw new EntityNotFoundException(ErrorCode.ALREADY_FOLLOW_USER,
                            "이미 팔로우한 유저 이름: "+followingUser.getName());
                });
        followRepository.save(follow);
        user.addFollowing(follow);
        user.addFollower(follow);
        followingUser.addFollower(follow);
        followingUser.addFollowing(follow);
        return followingUser.getName();
    }
    @Transactional
    public String userUnfollow(UserFollowRequestDto userFollowRequestDto) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "접속중인 유저가 존재하지 않습니다."));
        User followingUser = userRepository.findByName(userFollowRequestDto.getName())
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "해당 이름의 유저가 존재하지 않습니다. name: "+userFollowRequestDto.getName()));
        if (user.getId().equals(followingUser.getId())) {
            throw new EntityNotFoundException(ErrorCode.CAN_NOT_UNFOLLOW_MYSELF,
                    "접속한 유저 이름: "+user.getName()+"언팔로우하려는 유저 이름: "+followingUser.getName());
        }
        Follow follow = new Follow();
        follow = followRepository.findByFolloweeAndFollower(user, followingUser)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.NOT_FOLLOW_USER,
                        "팔로우 하려는 유저 이름: "+followingUser.getName()));
        followRepository.delete(follow);
        return followingUser.getName();
    }
    @Transactional
    public List<FollowingListResponseDto> followingList(String userName) {
        User user = userRepository.findByName(userName)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "해당 이름의 유저가 존재하지 않습니다. name: "+userName));
        List<User> followerUsers = user.getFollowers().stream()
                .map(Follow::getFollower)
                .toList();
        return followerUsers.stream()
                .map(FollowingListResponseDto::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<FollowerListResponseDto> followerList(String userName) {
        User user = userRepository.findByName(userName)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "해당 이름의 유저가 존재하지 않습니다. name: "+userName));
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
    @Transactional
    public UserListResponseDto searchHardName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "해당 이름의 유저가 없습니다. name: "+name));
        UserListResponseDto userListResponseDto = new UserListResponseDto();
        userListResponseDto.setName(user.getName());
        userListResponseDto.setEmail(user.getEmail());
        userListResponseDto.setProfileImg(user.getProfileimg());
        return userListResponseDto;
    }
}
