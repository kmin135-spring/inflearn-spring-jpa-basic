package hellojpa.entity2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Setter @Getter
/*
* ToString을 양방향관계 객체에 모두 정의하면 무한루프 생김!! -> 장애
  * ToString 양쪽에 다 걸지말던가 무한루프 안 생기게 정의해서 쓴다.
* JSON 생성 라이브러리도 마찬가지
  * JSON 이슈는 애초에 컨트롤러단에서 entity를 그냥 반환하지 마라
  * API에 맞는 전용 DTO 를 만들어주면 무한루프 문제도 같이 해결되고 API에 특정 entity에 종속성을 갖지 않게 된다.
*/
@ToString
public class Mem {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    // 연관관계 매핑 편의메소드 (단순 setter가 아니라 의미를 가지는 전용 이름 정의)
    // 매핑메서드는 양쪽에 만들지말고 한쪽에 만들자 (무한 루프 방지)
    // 어디에 만들지는 시나리오에 따라 다르므로 그때그때 정의
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
