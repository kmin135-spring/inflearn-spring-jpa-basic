package hellojpa.jpql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Order2 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int orderAmount;
    @Embedded
    private Address2 address;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member2 member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}