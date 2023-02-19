package com.microblog.composite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import composite.PostComposite;
import composite.PostCompositeController;
import core.comment.Comment;
import core.post.Post;
import com.microblog.exception.ErrInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@RestController
public class CompositeControllerImpl implements PostCompositeController {
    private static final Logger LOG = LoggerFactory.getLogger(CompositeControllerImpl.class);

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    private final String POST_URL;
    private final String COMMENT_URL;

    public CompositeControllerImpl(
            @Value("${app.post.host}") String postHost,
            @Value("${app.post.port}") String postPort,
            @Value("${app.comment.host}") String commentHost,
            @Value("${app.comment.port}") String commentPort
    ) {
        POST_URL = String.format("http://%s:%s/post", postHost, postPort);
        COMMENT_URL = String.format("http://%s:%s/comment", commentHost, commentPort);

        restTemplate = new RestTemplate();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public PostComposite getPost(int postId) throws Exception {
        try {
            Post post = getPostFromPostService(postId);
            List<Comment> comments = getCommentFromCommentService(postId);

            return new PostComposite(postId, post.getTitle(), post.getAuthor(), post.getContents(), comments);
        } catch (HttpClientErrorException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            if (HttpStatus.NOT_FOUND.equals(statusCode)) {
                throw new IllegalAccessException(getErrorMessage(ex));
            } else if (HttpStatus.BAD_REQUEST.equals(statusCode)) {
                throw new IllegalArgumentException(getErrorMessage(ex));
            }
            LOG.warn("Unexpected error: {}", ex.getStatusCode());
            LOG.warn("Error: {}", ex.getResponseBodyAsString());
            throw ex;
        }

    }

    // 추가 (다른 마이크로서비스에서 발생한 예외를 ErrInfo에 매핑해서 메시지를 가져오는 기능 구현)
    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), ErrInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }

    private Post getPostFromPostService(int postId) throws IllegalAccessException {
        String url = String.format("%s/%d", POST_URL, postId);
        LOG.debug("POST URL : {}", url);

        Post post = restTemplate.getForObject(url, Post.class);
        if (post == null) {
            throw new IllegalAccessException("해당 Post를 찾을 수 없습니다.");
        }

        LOG.debug("Post 반환 - postId: {}", post.getPostId());

        return post;
    }

    public List<Comment> getCommentFromCommentService(int postId) {
        String url = String.format("%s/%d", COMMENT_URL, postId);

        LOG.debug("COMMENT URL : {}", url);

        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {
        }).getBody();
    }
}
