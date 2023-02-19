package core.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Post {
    private final int postId;
    private final String title;
    private final String author;
    private final String contents;

    Post() {
        postId = 0;
        title = null;
        author = null;
        contents = null;
    }

}
