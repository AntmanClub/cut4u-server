package antmanclub.cut4userver.comment.service;

import antmanclub.cut4userver.comment.domain.Comment;
import antmanclub.cut4userver.comment.dto.*;
import antmanclub.cut4userver.comment.repository.CommentRepository;
import antmanclub.cut4userver.global.error.ErrorCode;
import antmanclub.cut4userver.global.error.exception.EntityNotFoundException;
import antmanclub.cut4userver.posts.domain.Posts;
import antmanclub.cut4userver.posts.repository.PostsRepository;
import antmanclub.cut4userver.user.SemiToken.CurrentUser;
import antmanclub.cut4userver.user.domain.User;
import antmanclub.cut4userver.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.EscapedErrors;

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
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CommentsListResponseDto addComment(CommentsAddRequestDto commentsAddRequestDto) {
        // comment 연관관계 mapping
        Posts targetPosts = postsRepository.findById(commentsAddRequestDto.getPostsId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POSTS_NOT_FOUND,
                        "해당 id의 게시물이 없습니다. id: "+commentsAddRequestDto.getPostsId()));

        User owner = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "접속중인 유저가 없습니다."));
        Comment newComment = null;
        String content = commentsAddRequestDto.getComment();
        if(commentsAddRequestDto.getParentCommentId() == 0L){
            // comment has no parent
            newComment = new Comment(null, targetPosts, owner, content);
            commentRepository.save(newComment);
        }else{
            // comment has parent
            Comment parentComment = commentRepository.findById(commentsAddRequestDto.getParentCommentId())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CAN_NOT_REPLY_COMMENT,
                            "해당 id의 댓글이 존재하지 않습니다. id: "+commentsAddRequestDto.getParentCommentId()));
            newComment = new Comment(parentComment, null, owner, content);
            commentRepository.save(newComment);
        }
        targetPosts.addCommentsCount();
        return viewComments(commentsAddRequestDto.getPostsId());
    }

    @Transactional
    public CommentsListResponseDto viewComments(Long postsId) {
        // find comments list by posts id
        Posts targetPosts = postsRepository.findById(postsId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.POSTS_NOT_FOUND,
                        "해당 id의 게시물이 존재하지 않습니다. id: "+postsId));
        List<Comment> commentsList = targetPosts.getComments();
        List<CommentsDto> commentsDtoList = convertToDtoList(commentsList);

        return new CommentsListResponseDto(commentsDtoList);
    }

    private List<CommentsDto> convertToDtoList(List<Comment> commentsList) {
        return commentsList.stream()
                .map(comment -> CommentsDto.builder()
                        .commentId(comment.getId())
                        .comment(comment.getContent())
                        .userName(comment.getUser().getName())
                        .userEmail(comment.getUser().getEmail())
                        .profileImg(comment.getUser().getProfileimg())
                        .replyCommentList(comment.getReplyComments().stream()
                                .map(replyComment -> CommentsDto.builder()
                                        .commentId(replyComment.getId())
                                        .comment(replyComment.getContent())
                                        .userName(replyComment.getUser().getName())
                                        .userEmail(replyComment.getUser().getEmail())
                                        .profileImg(replyComment.getUser().getProfileimg())
                                        .replyCommentList(null)
                                        .replyCommentsCount(0)
                                        .build())
                                .collect(Collectors.toList()))
                        .replyCommentsCount(comment.getReplyComments().size())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentsListResponseDto deleteComment(Long commentId) {
        // get comment by id
        Comment deleteComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND,
                        "해당 id의 댓글(답글)이 존재하지 않습니다. id: "+commentId));

        // authorization
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "현재 접속중인 유저가 없습니다."));
        if(!user.equals(deleteComment.getUser())){
            throw new EntityNotFoundException(ErrorCode.CAN_NOT_DELETE_COMMENT,
                    "댓글(답글)의 작성자가 아닙니다. 댓글(답글) 작성자: "+deleteComment.getUser().getName());
        }

        // find post by comment
        Posts targetPosts = null;
        if(deleteComment.getParentComment() == null){   // has no parent
            targetPosts = deleteComment.getPost();
        }else{
            targetPosts = deleteComment.getParentComment().getPost();   // has parent
        }

        // delete comment
        commentRepository.delete(deleteComment);
        entityManager.flush();  // 트랜잭션 지연을 일단 임시 해결하기 위함 근데 관행적으로 안 좋대서 바꿔야 하는데 뭘로 바꾸지

        targetPosts.subCommentsCount();
        return viewComments(targetPosts.getId());
    }

    @Transactional
    public CommentsListResponseDto modifyComment(CommentsModifyRequestDto commentsModifyRequestDto) {
        // get comment by id
        Comment modifyComment = commentRepository.findById(commentsModifyRequestDto.getCommentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND,
                        "해당 id의 댓글(답글)이 존재하지 않습니다. id: "+commentsModifyRequestDto.getCommentId()));

        // authorization
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND,
                        "현재 접속중인 유저가 없습니다."));
        if(!user.equals(modifyComment.getUser())){
            throw new EntityNotFoundException(ErrorCode.CAN_NOT_MODIFY_COMMENT,
                    "댓글(답글)의 작성자만이 수정할 수 있습니다. 댓글(답글) 작성자: "+modifyComment.getUser().getName());
        }

        // find post by comment
        Posts targetPosts = null;
        if(modifyComment.getParentComment() == null){   // has no parent
            targetPosts = modifyComment.getPost();
        }else{
            targetPosts = modifyComment.getParentComment().getPost();   // has parent
        }

        // update comment
        modifyComment.setContent(commentsModifyRequestDto.getContent());
        entityManager.flush();

        return viewComments(targetPosts.getId());
    }
}