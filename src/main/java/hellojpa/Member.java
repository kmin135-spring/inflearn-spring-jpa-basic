package hellojpa;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter @Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    private Long id;
    private String name;
    private int age;
}
