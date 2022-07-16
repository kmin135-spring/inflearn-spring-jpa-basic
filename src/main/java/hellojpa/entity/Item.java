package hellojpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// JOIN 전략에서는 필수는 아니나 운영편의상 있는게 좋다. (부모 테이블만 보고 어느 테이블과 연관있는지 구분 가능)
// SINGLE_TABLE 전략에서는 필수
// TABLE_PER_CLASS 는 부모테이블이 안 만들어지니 필요없음
@DiscriminatorColumn
@Setter @Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
