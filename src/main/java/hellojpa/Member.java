package hellojpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter @Getter
@ToString
public class Member {
    @Id
    private Long id;
    private String name;
}
