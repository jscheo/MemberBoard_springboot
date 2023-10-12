package com.example.memberboard.repository;

import com.example.memberboard.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findByBoardTitleContaining(String q, PageRequest id);

    Page<BoardEntity> findByBoardWriterContaining(String q, PageRequest id);
}
