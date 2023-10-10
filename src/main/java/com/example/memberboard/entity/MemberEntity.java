package com.example.memberboard.entity;

import com.example.memberboard.dto.MemberDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name="newMember_table")
public class MemberEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String memberEmail;
    @Column(length = 20, nullable = false)
    private String memberPassword;
    @Column(length = 30, nullable = false)
    private String memberName;
    @Column(length = 50)
    private String memberMobile;
    @Column(length = 50)
    private String memberBirth;
    @Column
    private int fileAttached;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MemberFileEntity> memberFileEntityList = new ArrayList<>();

    public static MemberEntity toSaveEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setMemberBirth(memberDTO.getMemberBirth());
        memberEntity.setFileAttached(0);
        return memberEntity;
    }

    public static MemberEntity toSaveFileEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setMemberBirth(memberDTO.getMemberBirth());
        memberEntity.setFileAttached(1);
        return memberEntity;
    }
}
