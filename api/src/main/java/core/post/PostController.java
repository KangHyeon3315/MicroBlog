package core.post;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface PostController {
    @GetMapping(value="/post/{postId}", produces="application/json")
    Mono<Post> getPost(@PathVariable int postId) throws Exception;
}
