package composite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface PostCompositeController {
    @GetMapping(value = "/composite/{postId}", produces = "application/json")
    PostComposite getPost(@PathVariable int postId) throws Exception;
}
