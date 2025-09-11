package com.microservice.task_service.service;

import com.microservice.task_service.model.entity.Comment;
import com.microservice.task_service.model.entity.Task;
import com.microservice.task_service.repository.CommentRepository;
import com.microservice.task_service.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Comment save(Comment comment) {

        Task task = taskRepository.findById(comment.getTask().getId()).orElseThrow();
        comment.setTask(task);
        return commentRepository.save(comment);
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow();
    }

    @Override
    public Comment update(Long id, Comment comment) {
        return commentRepository.findById(id).map(c -> {
            Task task = taskRepository.findById(comment.getTask().getId()).orElseThrow();
            c.setTask(task);
            c.setAuthorId(comment.getAuthorId());
            c.setContent(comment.getContent());
            c.setUpdated(true);
            return commentRepository.save(c);
        }).orElseThrow();
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
}
