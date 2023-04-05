package core.comment;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;

public interface CommentController {
    @GetMapping(value = "/comment/{postId}", produces = MediaType.APPLICATION_NDJSON_VALUE)
    Flux<Comment> getComments(@PathVariable int postId);
}