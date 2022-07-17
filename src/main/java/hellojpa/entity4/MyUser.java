package hellojpa.entity4;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter @Getter
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private String name;

    @Embedded
    private Period period;
    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "favorite_food", joinColumns = @JoinColumn(name="user_id"))
    @Column(name = "food_name") // 단일타입일때는 column 으로 필드명 지정 가능
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "address", joinColumns = @JoinColumn(name="user_id"))
    private List<Address> addressHistory = new ArrayList<>();
}