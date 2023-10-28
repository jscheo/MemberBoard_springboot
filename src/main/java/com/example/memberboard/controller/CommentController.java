package com.example.memberboard.controller;


import com.example.memberboard.dto.CommentDTO;
import com.example.memberboard.dto.LikeDTO;
import com.example.memberboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody CommentDTO commentDTO, HttpSession session) {
        Long memberId = (Long) session.getAttribute("loginId");
        try {
            commentService.save(commentDTO);
            List<CommentDTO> commentDTOList = commentService.findAll(memberId, commentDTO.getBoardId());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/like")
    public ResponseEntity like(@RequestBody LikeDTO likeDTO) {
        boolean checkResult = commentService.likeCheck(likeDTO);
        if (checkResult)
            commentService.like(likeDTO);
        List<CommentDTO> commentDTOList = commentService.findAll(likeDTO.getMemberId(), likeDTO.getBoardId());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("/unlike")
    public ResponseEntity unLike(@RequestBody LikeDTO likeDTO) {
        commentService.unLike(likeDTO);
        List<CommentDTO> commentDTOList = commentService.findAll(likeDTO.getMemberId(), likeDTO.getBoardId());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

}