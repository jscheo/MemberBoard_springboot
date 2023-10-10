package com.example.memberboard.service;

import com.example.memberboard.dto.MemberDTO;
import com.example.memberboard.entity.MemberEntity;
import com.example.memberboard.entity.MemberFileEntity;
import com.example.memberboard.repository.MemberFileRepository;
import com.example.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberFileRepository memberFileRepository;

    public Long save(MemberDTO memberDTO) throws IOException {
        if(memberDTO.getMemberProfile().get(0).isEmpty()){
            MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
            return memberRepository.save(memberEntity).getId();
        }else{
            MemberEntity memberEntity = MemberEntity.toSaveFileEntity(memberDTO);
            MemberEntity saveEntity = memberRepository.save(memberEntity);

            for(MultipartFile memberFile : memberDTO.getMemberProfile()){
                String originalFileName = memberFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
                String savePath = "C:\\memberBoard_img" + storedFileName;
                memberFile.transferTo(new File(savePath));
                MemberFileEntity memberFileEntity =
                        MemberFileEntity.toSaveMemberFile(saveEntity, originalFileName, storedFileName);
                memberFileRepository.save(memberFileEntity);
            }
            return saveEntity.getId();
        }

    }
    public boolean findByEmail(String memberEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if(byMemberEmail.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public boolean login(MemberDTO memberDTO) {
        Optional<MemberEntity> optionalMemberEntity=
                memberRepository.findByMemberEmailAndMemberPassword(memberDTO.getMemberEmail(), memberDTO.getMemberPassword());
        if(optionalMemberEntity.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public void findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();

    }
}
