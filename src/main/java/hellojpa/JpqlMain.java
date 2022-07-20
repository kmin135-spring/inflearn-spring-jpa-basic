package hellojpa;

import hellojpa.entity.Member;
import hellojpa.jpql.Member2;
import hellojpa.jpql.MemberDTO;
import hellojpa.jpql.Team2;

import javax.persistence.*;
import java.util.List;

public class JpqlMain {
    public static void main(String[] args) {
        // 앱당 1개
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 1개 쓰레드 단위
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team2 t1 = new Team2();
            t1.setName("ttt");
            em.persist(t1);

            Team2 t2 = new Team2();
            t2.setName("ttt222");
            em.persist(t2);

            Member2 m = new Member2();
            m.setUsername("m1");
            m.setAge(10);
            m.setTeam(t1);
            em.persist(m);

            Member2 m2 = new Member2();
            m2.setUsername("m2");
            m2.setAge(20);
            m2.setTeam(t2);
            em.persist(m2);

            Member2 m3 = new Member2();
            m3.setUsername("m3");
            m3.setAge(20);
            m3.setTeam(t1);
            em.persist(m3);

//            TypedQuery<Member2> typeQuery = em.createQuery("select m from Member2 m where m.age >= :age", Member2.class);
//            typeQuery.setParameter("age", 10);
//
//            typeQuery.getResultList();
//
//            List<MemberDTO> resultList = em.createQuery("select new hellojpa.jpql.MemberDTO(m.username, m.age) from Member2 m order by m.age desc", MemberDTO.class)
//                    .setFirstResult(0)
//                    .setMaxResults(1)
//                    .getResultList();
//            System.out.println(resultList);
//
//            em.createQuery("select m from Member2 m join m.team", Member2.class)
//                            .getResultList();
//            List<String> resultList1 = em.createQuery("select group_concat(m.username) from Member2 m", String.class)
//                    .getResultList();
//            System.out.println(resultList1);

            em.flush();
            em.clear();

            List<Member2> resultList = em.createQuery("select m from Member2 m", Member2.class)
                    .getResultList();
            // 지연로딩하면서 N+1 문제 발생
            // batchsize를 잡아두고 실행해보면 lazy 로딩을 모아서 해준다.
            for (Member2 mm : resultList) {
                System.out.println(mm.getUsername() + " / " + mm.getTeam().getName());
            }

            em.flush();
            em.clear();

            // 그냥 조인을 걸어도 N+1 문제는 동일
            // fetch join을 걸면 m을 얻을 때 team 까지 한번에 얻어지면서 N+1 문제가 해결됨
            resultList = em.createQuery("select m from Member2 m join fetch m.team", Member2.class)
                    .getResultList();
            // 지연로딩하면서 N+1 문제 발생
            for (Member2 mm : resultList) {
                System.out.println(mm.getUsername() + " / " + mm.getTeam().getName());
            }

            em.flush();
            em.clear();

            List<Team2> team2List = em.createQuery("select distinct t from Team2 t join fetch t.members", Team2.class)
                    .getResultList();
            team2List.stream().forEach(t -> {
                System.out.println(t.getName());
                t.getMembers().stream().forEach(System.out::println);
            });

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
