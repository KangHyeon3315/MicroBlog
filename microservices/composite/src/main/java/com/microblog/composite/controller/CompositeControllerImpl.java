package com.microblog.composite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microblog.exception.ErrInfo;
import composite.PostComposite;
import composite.PostCompositeController;
import core.comment.Comment;
import core.post.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class CompositeControllerImpl implements PostCompositeController {
    private static final Logger LOG = LoggerFactory.getLogger(CompositeControllerImpl.class);

    private final ObjectMapper mapper;
    private final String POST_URL;
    private final String COMMENT_URL;

    @Value("${my.property}")
    private String myProperty;

    private final WebClient.Builder webclientBuilder;
    private WebClient webclient;

    public CompositeControllerImpl(WebClient.Builder webclientBuilder) {
        POST_URL = "http://post-service/post";
        COMMENT_URL = "http://comment-service/comment";
        this.webclientBuilder = webclientBuilder;
        this.webclient = null;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Test에서 Mock을 주입하기 위해 사용
     */
    private WebClient getWebclient() {
        if (webclient == null) {
            webclient = webclientBuilder.build();
        }
        return webclient;
    }

    @Override
    public Mono<PostComposite> getPost(int postId) {
        Mono<Post> post = getPostFromPostService(postId);
        Flux<Comment> comments = getCommentFromCommentService(postId);

        return Mono.zip(post, comments.collectList())
                .map(tuple -> new PostComposite(
                        postId,
                        tuple.getT1().getTitle(),
                        tuple.getT1().getAuthor(),
                        tuple.getT1().getContents() + " " + myProperty,
                        tuple.getT2()
                ));
    }

    private Mono<Post> getPostFromPostService(int postId) {
        String url = String.format("%s/%d", POST_URL, postId);
        LOG.debug("POST URL : {}", url);

        return getWebclient()
                .get()
                .uri(url).retrieve()
                .bodyToMono(Post.class).log()
                .onErrorMap(WebClientResponseException.class, this::handleException);
    }

    public Flux<Comment> getCommentFromCommentService(int postId) {
        String url = String.format("%s/%d", COMMENT_URL, postId);

        LOG.debug("COMMENT URL : {}", url);

        return getWebclient()
                .get()
                .uri(url).retrieve()
                .bodyToFlux(Comment.class);
    }

    private Throwable handleException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException resEx)) {
            LOG.warn("Unexpected Exception: {}", ex.toString());
            return ex;
        }

        HttpStatusCode statusCode = resEx.getStatusCode();
        if (HttpStatus.BAD_REQUEST.equals(statusCode)) {
            return new IllegalArgumentException(getErrorMessage(resEx));
        } else if (HttpStatus.NOT_FOUND.equals(statusCode)) {
            return new IllegalAccessException(getErrorMessage(resEx));
        }
        LOG.warn("Unexpected Exception: {}", ex.toString());
        return ex;
    }

    private String getErrorMessage(WebClientResponseException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), ErrInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
