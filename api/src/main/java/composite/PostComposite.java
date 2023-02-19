package composite;

import core.comment.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PostComposite {
    private final int postId;
    private final String title;
    private final String author;
    private final String contents;
    private final List<Comment> comments;
}
