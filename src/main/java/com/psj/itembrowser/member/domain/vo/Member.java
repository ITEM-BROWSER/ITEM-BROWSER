package com.psj.itembrowser.member.domain.vo;


import com.psj.itembrowser.common.BaseDateTimeEntity;
import com.psj.itembrowser.member.domain.dto.request.MemberRequestDTO;
import com.psj.itembrowser.member.domain.dto.response.MemberResponseDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseDateTimeEntity {
    
    private MemberNo no;
    
    /**
     * 인증정보
     */
    private Credentials credentials;
    
    /**
     * 성. 이름
     */
    private Name name;
    
    /**
     * 휴대폰번호
     */
    private String phoneNumber;
    
    /**
     * 성별
     */
    private Gender gender;
    
    /**
     * 역할
     */
    private Role role;
    
    /**
     * 회원 상태. ACTIVE -> 활성화, READY -> 대기, DISABLED -> 비활성화
     */
    private Status status;
    
    /**
     * 주소
     */
    private Address address;
    
    /**
     * 생년월일. 생년월일
     */
    private LocalDate birthday;
    
    /**
     * 최종 로그인 일시
     */
    private LocalDateTime lastLoginDate;
    
    public MemberResponseDTO toResponseDTO() {
        MemberResponseDTO dto = new MemberResponseDTO();
        dto.setMemberNo(this.getNo().getMemberNo());
        dto.setEmail(this.getCredentials().getEmail());
        dto.setPassword(this.getCredentials().getPassword());
        dto.setFirstName(this.getName().getFirstName());
        dto.setLastName(this.getName().getLastName());
        dto.setPhoneNumber(this.getPhoneNumber());
        if (this.getAddress() != null) {
            dto.setAddressMain(this.getAddress().getAddressMain());
            dto.setAddressSub(this.getAddress().getAddressSub());
            dto.setZipCode(this.getAddress().getZipCode());
        }
        dto.setGender(this.getGender());
        dto.setRole(this.getRole());
        dto.setStatus(this.getStatus());
        dto.setBirthday(this.getBirthday());
        dto.setLastLoginDate(this.getLastLoginDate());
        dto.setCreatedDate(this.getCreatedDate());
        dto.setUpdatedDate(this.getUpdatedDate());
        dto.setDeletedDate(this.getDeletedDate());
        
        return dto;
    }
    
    public static Member create(MemberRequestDTO dto) {
        Member member = new Member();
        member.credentials = new Credentials(dto.getMemberId(), dto.getPassword());
        return member;
    }
    
    @Getter
    public enum Role {
        ROLE_CUSTOMER("일반 구매자"),
        ROLE_STORE_SELLER("상점 판매자"),
        ROLE_ADMIN("관리자");
        
        private final String name;
        private final String description;
        
        Role(String description) {
            this.description = description;
            this.name = this.name();
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
    
    @Getter
    public enum Status {
        ACTIVE("활성화"),
        READY("대기"),
        DISABLED("비활성화");
        
        private final String name;
        private final String description;
        
        Status(String description) {
            this.description = description;
            this.name = this.name();
        }
    }
    
    @Getter
    public enum Gender {
        MEN("남성"),
        WOMEN("여성");
        
        private final String name;
        private final String description;
        
        Gender(String description) {
            this.description = description;
            this.name = this.name();
        }
    }
}