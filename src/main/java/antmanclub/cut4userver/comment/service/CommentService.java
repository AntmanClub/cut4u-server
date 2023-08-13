package antmanclub.cut4userver.comment.service;

import antmanclub.cut4userver.comment.domain.Comment;
import antmanclub.cut4userver.comment.dto.CommentsAddRequestDto;
import antmanclub.cut4userver.comment.dto.CommentsDto;
import antmanclub.cut4userver.comment.dto.CommentsListResponseDto;
import antmanclub.cut4userver.comment.repository.CommentRepository;
import antmanclub.cut4userver.posts.domain.Posts;
import antmanclub.cut4userver.posts.repository.PostsRepository;
import antmanclub.cut4userver.user.SemiToken.CurrentUser;
import antmanclub.cut4userver.user.domain.User;
import antmanclub.cut4userver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    @Transactional
    public CommentsListResponseDto addComment(CommentsAddRequestDto commentsAddRequestDto) {
        // comment 연관관계 mapping
        Posts targetPosts = postsRepository.findById(commentsAddRequestDto.getPostsId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        User owner = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("접속중인 유저가 없습니다."));
        Comment newComment = null;
        String content = commentsAddRequestDto.getComment();
        if(commentsAddRequestDto.getParentCommentId() == 0L){
            // comment has no parent
            newComment = new Comment(null, targetPosts, owner, content);
            commentRepository.save(newComment);
        }else{
            // comment has parent
            Comment parentComment = commentRepository.findById(commentsAddRequestDto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("삭제된 댓글이거나 존재하지 않는 댓글에는 답글을 달 수 없습니다."));
            newComment = new Comment(parentComment, null, owner, content);
            commentRepository.save(newComment);
        }
        return viewComments(commentsAddRequestDto.getPostsId());
    }

    @Transactional
    public CommentsListResponseDto viewComments(Long postsId) {
        // find comments list by posts id
        Posts targetPosts = postsRepository.findById(postsId)
                .orElseThrow(() -> new IllegalArgumentException("삭제되었거나 존재하지 않는 게시물입니다."));
        List<Comment> commentsList = targetPosts.getComments();
        List<CommentsDto> commentsDtoList = convertToDtoList(commentsList);

        return new CommentsListResponseDto(commentsDtoList);
    }

    private List<CommentsDto> convertToDtoList(List<Comment> commentsList) {
        return commentsList.stream()
                .map(comment -> CommentsDto.builder()
                        .comment(comment.getContent())
                        .userName(comment.getUser().getName())
                        .userEmail(comment.getUser().getEmail())
                        .profileImg(comment.getUser().getProfileimg())
                        .child(comment.getReplyComments().stream()
                                .map(replyComment -> CommentsDto.builder()
                                        .comment(replyComment.getContent())
                                        .userName(replyComment.getUser().getName())
                                        .userEmail(replyComment.getUser().getEmail())
                                        .profileImg(replyComment.getUser().getProfileimg())
                                        .child(null)
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
