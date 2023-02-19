package core.comment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CommentController {
    @GetMapping(value = "/comment/{postId}", produces = "application/json")
    List<Comment> getComments(@PathVariable int postId);
}