package core.post;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface PostController {
    @GetMapping(value="/post/{postId}", produces="application/json")
    Post getPost(@PathVariable int postId) throws Exception;
}
