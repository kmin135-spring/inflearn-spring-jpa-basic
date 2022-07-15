package hellojpa;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter @Getter
@ToString
@NoArgsConstructor
public class Member {
    @Id
    private Long id;
    private String username;
    private Integer age;
    @Enumerated(EnumType.STRING) // 기본값인 ORDINAL 은 사용하지 말자 (값 꼬이면 장애)
    private RoleType roleType;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    @Lob
    private String description;
}
