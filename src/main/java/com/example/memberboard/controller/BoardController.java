package com.example.memberboard.controller;

import com.example.memberboard.dto.BoardDTO;
import com.example.memberboard.service.BoardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/save")
    public String save(){
        return "boardPages/boardSave";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/list")
    public String findAll(Model model,
                          @RequestParam(value = "page", required = false, defaultValue = "1")int page,
                          @RequestParam(value = "type", required = false, defaultValue = "boardTitle")String type,
                          @RequestParam(value = "q", required = false, defaultValue = "")String q){
        Page<BoardDTO> boardDTOList = boardService.findAll(page, type, q);

        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardDTOList.getTotalPages()) ? startPage + blockLimit - 1 : boardDTOList.getTotalPages();
        model.addAttribute("boardList", boardDTOList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("page", page);
        model.addAttribute("type", type);
        model.addAttribute("q", q);
        return "/boardPages/boardList";
    }
    @GetMapping("{id}")
    public String findById(@PathVariable("id")Long id, Model model,
                           @RequestParam(value = "page", required = false, defaultValue = "1")int page,
                           @RequestParam(value = "type", required = false, defaultValue = "boardTitle")String type,
                           @RequestParam(value = "q", required = false, defaultValue = "")String q){
        boardService.increaseHits(id);

        System.out.println("id = " + id);

        model.addAttribute("page", page);
        model.addAttribute("type", type);
        model.addAttribute("q", q);
        try{
            BoardDTO byId = boardService.findById(id);
            System.out.println("byId = " + byId);
            model.addAttribute("board", byId);
            return "boardPages/boardDetail";
        }catch (Exception e){
            return "boardPages/boardNotFound";
        }
    }
}
