package core.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Comment {
    private final int postId;
    private final int commentId;
    private final String author;
    private final String comment;

    Comment() {
        this.postId = 0;
        this.commentId = 0;
        this.author = null;
        this.comment = null;
    }
}
