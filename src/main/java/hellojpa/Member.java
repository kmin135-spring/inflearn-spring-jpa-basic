package hellojpa;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter @Getter
@ToString
@NoArgsConstructor
public class Member {
    // IDENTITY 방식은 DB에 넣어야 PK가 결정되므로 어쩔 수 없이 persist할때 바로 insert가 수행됨 (SEQUENCE 방식과의 차이점)
    // 반면에 시퀀스는 persist 시점에 시퀀스에서 값을 얻어와야함. allocationSize로 튜닝 가능
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
}
