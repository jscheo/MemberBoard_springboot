package com.example.memberboard.service;

import com.example.memberboard.dto.BoardDTO;
import com.example.memberboard.entity.BoardEntity;
import com.example.memberboard.entity.BoardFileEntity;
import com.example.memberboard.repository.BoardFileRepository;
import com.example.memberboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    public Long save(BoardDTO boardDTO) throws IOException {
        if(boardDTO.getBoardFile().get(0).isEmpty()){
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            return boardRepository.save(boardEntity).getId();
        }else{
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            BoardEntity saveEntity = boardRepository.save(boardEntity);
            for(MultipartFile memberFile : boardDTO.getBoardFile()){
                String originalFilename = memberFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis()+ "_" + originalFilename;
                String savePath = "C:\\boardFile_img" + storedFileName;
                memberFile.transferTo(new File(savePath));
                BoardFileEntity boardFileEntity =
                        BoardFileEntity.toSaveEntity(saveEntity, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
            return saveEntity.getId();
        }
    }
}
