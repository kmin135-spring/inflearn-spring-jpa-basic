package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter @Getter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    private Long memberId;
    // 그냥 JPA만 쓰면 db 칼럼 네이밍이 그대로 camelcase로 들어가는데
    // spring boot는 기본적으로 order_date 로 언더바 방식을 기본전략으로 쓴다.
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
