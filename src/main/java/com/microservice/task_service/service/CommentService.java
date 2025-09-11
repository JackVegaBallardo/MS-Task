package com.microservice.task_service.service;

import com.microservice.task_service.model.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment save(Comment comment);
    Comment findById(Long id);
    Comment update(Long id, Comment comment);
    void delete(Long id);
    List<Comment> findAll();
}
