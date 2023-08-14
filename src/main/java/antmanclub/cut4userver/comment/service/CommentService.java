package antmanclub.cut4userver.comment.service;

import antmanclub.cut4userver.comment.dto.CommentsAddRequestDto;
import antmanclub.cut4userver.comment.dto.CommentsListResponseDto;
import antmanclub.cut4userver.comment.repository.CommentRepository;
import antmanclub.cut4userver.posts.repository.PostsRepository;
import antmanclub.cut4userver.user.SemiToken.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final CurrentUser currentUser;

    public CommentsListResponseDto addComment(CommentsAddRequestDto commentsAddRequestDto) {
        return null;
    }

    public CommentsListResponseDto viewComments(Long postsId) {
        return null;
    }

}
