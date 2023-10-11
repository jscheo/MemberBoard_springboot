package com.example.memberboard.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private int fileAttached;
    private String createdAt;
    private List<MultipartFile> boardFile;
}
