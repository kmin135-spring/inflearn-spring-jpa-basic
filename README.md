# 개요

## Entity 정의

* entity가 만든 DDL을 운영에 그대로 쓸 수는 없으나
* 칼럼의 length 라거나 nullable같은거나 테이블의 유니크, 각종 인덱스도 entity에 정의해두는게 좋다.
* 그래야 실제 db를 까보지 않고도 개발자가 entity 객체만 보고 제약조건이나 JPQL을 작성할 때 어떤 인덱스를 쓸지 빠르게 결정할 수 있다.

## PK

* Long 이나 UUID 와 같은 대체키를 사용해라
  * 자연키는 언젠가 바뀔 수 있다!

## 연관관계

* 외래키가 있는 쪽을 주인으로 정해라
  * 테이블이 있는 쪽에 생기니 직관적이다.
* 단방향 매핑만으로 이미 연관관계 매핑은 완료된다.
  * 최초에는 단방향 매핑으로 설계를 끝내라.
  * 양방향 매핑은 필요할 때 정의해서 써라 (JPQL 짤 때 필요하면, 객체 그래프 탐색이 필요해서, ...)
    * 단방향으로도 모두 가능하지만 비즈니스적으로 양방향이 필요하다고 판단될 때 양방향으로 만든다.
  * 양방향 매핑을 나중에 추가하는건 쉬운 작업이다. (테이블에 영향을 주지 않음)
  * 왜냐하면 양방향은 연관관계 매핑, 무한 참조등 고려할 사항이 늘어나기 때문

## 연관관계2

* `@ManyToOne` 을 가장 많이 쓰게된다.
  * 일단 이걸 써라. 안 되면 다른걸 고민해라.
  * 설계는 단순해야한다.
* `@OneToMany` 에서 One쪽이 연관관계 주인이 되도록 할 수 있긴한데 DB입장에서는 N쪽에 FK가 들어가므로 직관적이지 않다.
  * Team과 Member가 있는데 Team이 연관관계의 주인이면 Member의 FK를 수정하기 위해 Team을 컨트롤해야한다.
  * Team을 수정했더니 Member에 dml이 날라가는 등 헷갈린다.
  * FK 수정 update 쿼리도 따로 나가게되서 성능면에서도 안 좋다.
  * 그래서 `@OneToMany` 에서 One이 연관관계의 주인이 되도록 설계할 일은 거의 없다.
  * 객체레벨에서 어색하더라도 `@ManyToOne` 을 써라 (객체지향을 좀 포기하고 DB에 맞추는 것)
  * 정말 꼭 필요할때만 쓰자...
* `@OneToOne`
  * 1:1이므로 외래키를 어느쪽에 넣어도되는데 주 테이블에 넣으면 좋다.
    * 또한 1:1관계 유지를 위해 외래키+unique 조건을 걸어야함.
  * 1:1도 기본은 주테이블에 단방향 매핑만 해두는게 기본
  * 필요할 때 대상 테이블에도 양방향 매핑을 해주자
  * 대상 테이블에 FK가 있는경우는 단방향일 때는 지원불가 = 즉, 내 FK는 나만 관리할 수 있다.
  * 시나리오 : 회원과 사물함이 1:1이다.
    * 개발자는 회원객체가 주 엔티티라 회원쪽에 사물함 FK를 두고 싶다.
    * DBA는 db테이블만 보면 사물함쪽에 회원 FK가 있는게 자연스럽다고 본다.
    * 미래에 회원이 N개 사물함을 가지게 된다면 DBA의 주장이 맞고
      * 테이블 fk 필드에서 unique 조건만 제거하면 해결 가능
    * 반면 1개의 사물함이 여러 회원에게 속할 수 있게되면 개발자의 결정이 맞을 수도 있다.
    * 강사님은 성능 및 개발 용이성 면에서 주 테이블인 회원쪽에 있게 설계하는 편이라고 함.
      * 현시점에서 명확하게 1:1이라면 너무 미래까지 고민하지 않는다.
    * 정답은 없으므로 협의 후 결정하자
* `@ManyToMany` 는 실무에서 쓰지 마라
  * 커스텀 필드를 넣을 수가 없다.