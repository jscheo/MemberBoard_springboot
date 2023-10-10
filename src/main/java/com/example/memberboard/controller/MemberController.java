package com.example.memberboard.controller;

import com.example.memberboard.dto.MemberDTO;
import com.example.memberboard.entity.MemberEntity;
import com.example.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("save")
    public String save(){
        return "memberPages/memberSave";
    }

    @PostMapping("/save")
    public String saveMember(@ModelAttribute MemberDTO memberDTO) throws IOException {
        memberService.save(memberDTO);
        return "index";
    }
    @PostMapping("/dup")
    public ResponseEntity dup_check(@RequestBody String memberEmail){
        System.out.println("memberEmail = " + memberEmail);
       boolean byEmail = memberService.findByEmail(memberEmail);
        if(byEmail){
            return new ResponseEntity<>("사용가능",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("사용불가", HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/login")
    public String loginForm(){
        return "memberPages/memberLogin";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        boolean loginResult = memberService.login(memberDTO);
        if(loginResult){
            return "/boardPages/boardList";
        }else{
            return "/boardPages/boardNotFound";
        }
    }
    @PutMapping("/loginCheck")
    public ResponseEntity loginCheck(@RequestBody String memberEmail){
        boolean byEmail = memberService.findByEmail(memberEmail);
        if(byEmail){
            return new ResponseEntity("사용가능", HttpStatus.OK);
        }else{
            return new ResponseEntity("사용불가", HttpStatus.CONFLICT);
        }
    }
}
