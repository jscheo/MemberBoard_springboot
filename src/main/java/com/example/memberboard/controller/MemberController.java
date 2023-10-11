package com.example.memberboard.controller;

import com.example.memberboard.dto.MemberDTO;
import com.example.memberboard.entity.MemberEntity;
import com.example.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            return "/boardPages/boardList";
        }else{
            return "redirect:/member/login";
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
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("loginEmail");
        return "index";
    }
    @GetMapping("/admin")
    public String admin(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        System.out.println("memberDTOList = " + memberDTOList);
        model.addAttribute("memberList", memberDTOList);
        return "memberPages/memberList";
    }
    @GetMapping("/{id}")
    public String delete(@PathVariable("id") Long id){
        memberService.delete(id);
        return "index";
    }
}
