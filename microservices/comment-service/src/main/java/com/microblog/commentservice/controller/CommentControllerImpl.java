package com.microblog.commentservice.controller;

import core.comment.Comment;
import core.comment.CommentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentControllerImpl implements CommentController {
    private static final Logger LOG = LoggerFactory.getLogger(CommentControllerImpl.class);

    @Override
    public List<Comment> getComments(int postId) {
        LOG.debug("Comment Service 요청 수신 후 응답 - post id : {}", postId);

        List<Comment> list = new ArrayList<>();
        list.add(new Comment(postId, 1, "작성자1", "내용1"));
        list.add(new Comment(postId, 2, "작성자2", "내용2"));

        return list;
    }
}