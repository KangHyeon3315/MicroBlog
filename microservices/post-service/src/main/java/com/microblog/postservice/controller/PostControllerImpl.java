package com.microblog.postservice.controller;

import core.post.Post;
import core.post.PostController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PostControllerImpl implements PostController {
    private static final Logger LOG = LoggerFactory.getLogger(PostControllerImpl.class);

    @Override
    public Mono<Post> getPost(int postId) throws Exception {
        if (postId >= 100) {
            throw new IllegalArgumentException("Post ID는 100이하여야 합니다.");
        }

        LOG.debug("Post Service 요청 수신 후 응답 - post id : {}", postId);

        return Mono.just(new Post(postId, "post title " + postId, "post author " + postId, "contents " + postId));
    }
}
