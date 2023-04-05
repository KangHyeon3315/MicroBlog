package composite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface PostCompositeController {
    @GetMapping(value = "/composite/{postId}", produces = "application/json")
    Mono<PostComposite> getPost(@PathVariable int postId) throws Exception;
}
