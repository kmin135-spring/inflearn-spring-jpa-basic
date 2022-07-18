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
  * 연결 테이블이 단순히 연결만 하고 끝나지 않는다.
  * 커스텀 필드를 넣을 수가 없다.
  * 주문-아이템 테이블이라면 주문시간, 수량 등의 추가 테이블이 필요하다.
  * 중간 테이블이 숨겨진 구조라 쿼리 예측이 어렵다.
  * `@OneToMany`, `@ManyToOne` 으로 풀어써라 = 연결 테이블을 정규 엔티티로 승격해라
  * 승격된 중간 테이블도 그냥 대체키를 써라
    * 양쪽 테이블의 FK 를 조합해서 PK를 쓰고 그러지 말자.
* 모든 테이블이 ID에 `@GeneratedValue`를 이용해 대체키를 쓴다고 생각하자.
  * 그 편이 JPA 매핑도 심플하고 미래에 유연하게 대응하기 좋다.

## 상속관계매핑

* JOIN : 
  * 장점 : 정규화 우수(저장공간 효율화), 객체의 구조와 유사하게 매칭됨, 외래키 사용
  * 단점 : 조인을 많이 해야하니 성능 저하. 구조 복잡, 조회 쿼리 복잡.
* SINGLE_TABLE : 
  * 장점 : 조인 필요없으니 성능상 유리. 단순함 
  * 단점 : 정규화가 떨어짐. 필요없는 필드가 null이 되버림. 테이블이 쓸데없이 커져서 오히려 느려질 여지가 있음.
* TABLE_PER_CLASS : 슈퍼타입 속성을 안 만들고 서브타입에 중복시켜서 만듬
  * 쓰지마
  * JPA관점이든 DBA관점이든 구린 방식
  * 나중에 슈퍼타입으로 select하면 서브타입 테이블에 대한 UNION 발생함
  * 자식 테이블을 통합해서 쿼리하기 어려움
  * 새로운 타입 추가할 때 많은 변경 필요
* 결론은 JOIN 과 SINGLE_TABLE의 trade-off 를 고려하여 결정
* 강사님은 기본은 JOIN으로 가고 정말 단순하거나 확장가능성이 낮다고 판단되면 단일 테이블을 쓴다고 함
  * 근데 운영하다가 테이블이 빠르게 커지면서 성능상 한계에 부딪히면 싱글 테이블로 바꾸기도 한다고 함
  * 하루에 수백만건씩 쌓이고 파티셔닝 하고 하다보면 테이블을 단순하게 유지하는게 중요해지기 때문


## MappedSuperclass

* 공통 매핑 정보가 필요할 때 사용(id, name)
  * 등록자, 등록일시, 수정자, 수정일시 같은 거 : 근데 이건 jpa 기능으로 자동화 가능함
* 상속관계는 아니고 공통으로 사용되는 속성의 중복을 제거하고 싶을 때
* 테이블이랑 상관없음. 엔티티 아님. 조회도 불가능함.
* 직접 생성할 일이 없으니 추상 클래스 권장

## 프록시, 지연로딩

* 실무에서는 항상 LAZY 로딩을 사용하자
  * `@ManyToOne`, `@OneToOne` 은 기본이 `EAGER` 이니 주의
  * 단, 동일 영속성 컨텍스트를 벗어난 뒤 초기화하면 프록시 초기화에 실패하게되니 초기화 시점에 주의하자
* EAGER 는 실무에서는 쓰지마라! 대안이 있다.
  * 즉시 로딩을 적용하면 실행되는 SQL을 예상하기 어려움 - 객체 그래프가 전부 EAGER 로 연결되어있다면...
  * JPQL에서 N+1 문제를 일으키기 쉬움
  * 대안 : JPQL fetch join, 엔티티 그래프

```
N+1 문제
EAGER일 때 
select m from Member m 이라는 JPQL로
10건의 member를 select하면 
member만 10건 select 후 EAGER 설정이 걸려있으니까
연관된 Team을 얻기 위해 Team에 10번 쿼리가 추가로 나감

member 100건을 select한다면 member 쿼리1번 + team 쿼리100번
100+1 = 101번이 나가게됨

em.find(Member.class, 1L); 은
JPA가 알아서 Team과 join해서 얻어오는 것과의 다름

JPQL의 대안은 아래와 같은 fetch join (inner join이 사용됨)
select m from Member m join fetch m.team
```

## 영속성 전이 : CASCADE

* 특정 엔티티를 영속 상태로 만들 때 연관 엔티티도 자동으로 같이 영속화하는 기능
* 실무에서 쓸까? -> 쓴다.
* 다만 Parent - Child의 관계에서 Child를 관리하는 Parent가 유일할 때 쓰는걸 권장.
  * ex. 게시글 과 글에 속한 첨부파일의 관계 (첨부파일이 단일 게시글에 종속됨)
  * 소유자가 유일할 때! (단일 소유자)
  * 라이프사이클이 동일, 거의 유사할 때
* Child 와 연관관계가 있는 엔티티가 여럿이면 쓰지마라
  * 예측이 힘들어짐

## 고아 객체

* 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능
* 영속성 전이와 마찬가지로 단일 소유하는 관계일 때만 쓰자
* `@OneToOne`, `@OneToMany` 만 가능
* 활성화하면 부모가 제거되면 자식도 제거됨
  * `CascadeType.REMOVE` 처럼 동작하게됨

## 영속성 전이 + 고아 객체

* `cascade = CascadeType.ALL, orphanRemoval = true` 처럼 다 켜주면
* 부모 엔티티로 자식 엔티티의 라이플 사이클 전체를 컨트롤 가능
* DDD의 Aggregate Root 개념을 구현할 때 유용 

## 임베디드 타입

* 값이 추상화하여 표현할 수 있는 장점
* 회원은 city, street 값을 가진다. -> 회원은 주소를 가진다.
* 중복되는 경우 (ex. 집주소, 회사 주소를 동시에 가짐) 에는 @AttributeOverrides, @AttributeOverride 사용
* 실무에서 그렇게 자주 쓰게되진 않으나 적용가능한 상황에서는 쓰면 좋다.

---

## 값 타입 세부사항

* 값 타입은 불변 객체로 정의해서 쓰자
* 예를들어 동일한 address 객체를 m1, m2에 모두 할당해놓고
  * `m1.getAddress().setCity("newCity")` 를 해버리면 m2의 주소까지 변경되는 사이드 이펙이 발생하는데
  * address를 불변객체로 만듬으로써 이런 일을 원천차단할 수 있다.
* 나라면 정적 팩토리 메서드를 만들 것 같다.
* 값 타입을 동등성을 갖도록 하자
  * 따로 생성할 경우 동일성 `(a == b)` 을 가질 수는 없으니
  * equals, hashcode 를 구현해서 동등성을 보장하자
* @Embeddable 달아주면 @Embedded 는 안 달아도 되긴 하는데 가독성을 위해 둘 다 달아주자

## 값 타입 컬렉션

* 엔티티를 영속화하면 같이 저장됨
  * 라이프사이클을 소속된 엔티티에 의존함
  * 1:N 관계의 `cascadeType.ALL, 고아처리 = true` 같은 느낌
* 값 타입 컬렉션은 기본 LAZY 임.
* 값 타입은 기본적으로 식별성이 없으므로 추적이 안 된다.
* 값 타입을 수정하면 전부 삭제한 뒤 다시 저장한다.
  * 대안으로 @OrderColumn 같은걸 써서 update 방식으로 하게 할 수도 있긴 하다.
  * 그럴듯하지만 이것도 번호가 꼬이거나 하면 위험
* 정 쓰려면 값 타입의 모든 필드를 묶어서 PK로 만들어야함 (null, 중복 방지)
* 결론은 값 타입 컬렉션은 쓰지 말고 일대다 엔티티로 풀어쓰는 걸 고려해라
  * 값 타입을 엔티티로 승격
* 값 타입은 정말 단순할 때 정도 한정적으로 사용
  * 추적할 필요도 없는 중요도가 낮을 때 정도?
* 식별자가 필요하고 지속해서 값을 추적, 변경해야한다면 그건 값 타입이 아니라 엔티티다.
  * ex. 주소 히스토리는 별도로 관리되야하므로 엔티티다

## equals 등 프록시 구조를 고려한 메서드 생성 주의사항

* equals 를 만들 때 getter 메서드로 만들어야한다.
* JPA에서 엔티티가 프록시로 들어올 경우 getter를 쓰지 않으면 원본 객체 (부모) 의 private 필드들을 볼 수가 없기 때문
* equals 뿐만 아니라 프록시 구조를 상정한 경우에 공통으로 적용되는 주의사항

## 복잡도별 권장 기술

* 간단 : spring-data-jpa
* 조금 복잡 : JPQL
  * **결국은 JPQL을 잘 알아야 다른것도 잘 함**
  * 그리고 JPQL은 최종적으로 SQL로 변환되므로 당연히 SQL도 잘 알아야함
* 꽤 복잡 : QueryDSL (JPQL 빌더 역할)
* 많이 복잡 or 벤더 종속적인 쿼리 : SpringJdbcTemplate, MyBatis 등
  * 강사님은 JPA의 NativeQuery는 안 쓰고 SpringJdbcTemplate 을 선호한다고함
  * 주의! : JPA의 NativeQuery를 제외하고 다른 기술들은 당연히 JPA 영속성 컨텍스트와 상관없으므로 동일 트랜잭션에 내에서 쓴다면 수동으로 영속성 컨텍스트를 flush 하고 사용해야함
    * 참고로 자동 flush는 tx commit할 때도 나가지만 nativeQuery가 나갈 때도 발생함. (그래야 쿼리가 가능하니까)

---

* JPA표준 Criteria 는 복잡도가 높고 가독성이 너무 떨어져서 실무 사용 X
* 강사님은 실무에서 95%는 JPQL, QueryDSL로 커버가 된다고 하고 정 안 되는 나머지들을 다른 기술들을 쓴다고 함

## JPQL

### 기본

* 엔티티, 속성은 대소문자 구분하니 주의
* JPQL 키워드는 대소문자 구분 X (select, WHERE)
* 테이블 이름이 아니고 (member) 엔티티 이름을 사용 (Member)
* 별칭 필수 (as 생략 가능)
* 파라미터는 이름 기반만 사용 (위치 기반은 순서 밀리면 꼬인다.)

### 프로젝션

* SELECT 절에 조회할 대상을 지정하는 것
* 프로젝션 대상 : 엔티티, 임베디드 타입, 스칼라 타입
  * 엔티티 프로젝션은 영속성 컨텍스트로 관리됨
* `select m.team from Member m` 이런식으로 암시적으로 join이 되게 하지 말고
  * `select t from Member m join m.team t` 와 같이 명시적으로 짜는 것을 권장
  * JQPL만 보고 빠르게 실제 수행되는 SQL쿼리를 알 수 있도록
* 스칼라 타입 결과 얻기
  * QUERY
  * Object[]
  * 전용 DTO 정의 : 좀 길어지지만 무난한 방법
    * `select new jpabook.jpql.MemberDTO(m.username, m.age) from Member m`