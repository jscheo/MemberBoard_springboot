package com.example.memberboard.repository;

import com.example.memberboard.entity.BoardEntity;
import com.example.memberboard.entity.CommentEntity;
import com.example.memberboard.entity.LikeEntity;
import com.example.memberboard.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByMemberEntityAndCommentEntity(MemberEntity memberEntity, CommentEntity commentEntity);
    List<LikeEntity> findByMemberEntityAndBoardEntity(MemberEntity memberEntity, BoardEntity boardEntity);
    void deleteByMemberEntityAndCommentEntity(MemberEntity memberEntity, CommentEntity commentEntity);
}
