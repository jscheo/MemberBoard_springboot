package com.example.memberboard.service;

import com.example.memberboard.dto.BoardDTO;
import com.example.memberboard.entity.BoardEntity;
import com.example.memberboard.entity.BoardFileEntity;
import com.example.memberboard.entity.MemberEntity;
import com.example.memberboard.repository.BoardFileRepository;
import com.example.memberboard.repository.BoardRepository;
import com.example.memberboard.repository.MemberRepository;
import com.example.memberboard.util.UtilClass;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final MemberRepository memberRepository;

    public Long save(BoardDTO boardDTO) throws IOException {
        MemberEntity memberEntity = memberRepository.findByMemberEmail(boardDTO.getBoardWriter()).orElseThrow(() -> new NoSuchElementException());
        if(boardDTO.getBoardFile().get(0).isEmpty()){
            BoardEntity boardEntity = BoardEntity.toSaveEntity(memberEntity, boardDTO);
            return boardRepository.save(boardEntity).getId();
        }else{
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(memberEntity, boardDTO);
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

    public Page<BoardDTO> findAll(int page, String type, String q) {
        page = page -1;
        int pageLimit = 5;
        Page<BoardEntity> boardEntities = null;

        if(q.equals("")){
            boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        }else {
            if((type.equals("boardTitle"))){
                boardEntities = boardRepository.findByBoardTitleContaining(q, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"id")));
            }else if(type.equals("boardWriter")){
                boardEntities = boardRepository.findByBoardWriterContaining(q, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"id")));
            }
        }

        Page<BoardDTO> boardList = boardEntities.map(boardEntity ->
                BoardDTO.builder()
                        .id(boardEntity.getId())
                        .boardWriter(boardEntity.getBoardWriter())
                        .boardTitle(boardEntity.getBoardTitle())
                        .boardContents(boardEntity.getBoardContents())
                        .boardHits(boardEntity.getBoardHits())
                        .createdAt(UtilClass.dateTimeFormat(boardEntity.getCreatedAt()))
                        .build());
        return boardList;
     }

    @Transactional
    public void increaseHits(Long id) {
        boardRepository.increaseHits(id);
    }

    @Transactional
    public BoardDTO findById(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        return BoardDTO.toSaveDTO(boardEntity);
    }

    public void update(BoardDTO boardDTO) {
        MemberEntity memberEntity = memberRepository.findByMemberEmail(boardDTO.getBoardWriter()).orElseThrow(() -> new NoSuchElementException());
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(memberEntity, boardDTO);
        boardRepository.save(boardEntity);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
