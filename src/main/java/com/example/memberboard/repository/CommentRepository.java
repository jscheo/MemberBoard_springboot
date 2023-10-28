package com.example.memberboard.repository;

import com.example.memberboard.entity.BaseEntity;
import com.example.memberboard.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByBoardEntityOrderByIdDesc(BaseEntity baseEntity);
}