package hellojpa.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
public class Member {
    // IDENTITY 방식은 DB에 넣어야 PK가 결정되므로 어쩔 수 없이 persist할때 바로 insert가 수행됨 (SEQUENCE 방식과의 차이점)
    // 반면에 시퀀스는 persist 시점에 시퀀스에서 값을 얻어와야함. allocationSize로 튜닝 가능
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String name;
    private String city;
    private String street;
    private String zipcode;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
