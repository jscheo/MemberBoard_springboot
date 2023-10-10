package com.example.memberboard.service;

import com.example.memberboard.dto.MemberDTO;
import com.example.memberboard.entity.MemberEntity;
import com.example.memberboard.entity.MemberFileEntity;
import com.example.memberboard.repository.MemberFileRepository;
import com.example.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
}
