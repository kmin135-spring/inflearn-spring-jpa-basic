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
            Team2 t = new Team2();
            t.setName("ttt");
            em.persist(t);

            Member2 m = new Member2();
            m.setUsername("m1");
            m.setAge(10);
            m.setTeam(t);
            em.persist(m);

            Member2 m2 = new Member2();
            m2.setUsername("m2");
            m2.setAge(20);
            m2.setTeam(t);
            em.persist(m2);

            TypedQuery<Member2> typeQuery = em.createQuery("select m from Member2 m where m.age >= :age", Member2.class);
            typeQuery.setParameter("age", 10);

            typeQuery.getResultList();

            List<MemberDTO> resultList = em.createQuery("select new hellojpa.jpql.MemberDTO(m.username, m.age) from Member2 m order by m.age desc", MemberDTO.class)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();
            System.out.println(resultList);

            em.createQuery("select m from Member2 m join m.team", Member2.class)
                            .getResultList();
            List<String> resultList1 = em.createQuery("select group_concat(m.username) from Member2 m", String.class)
                    .getResultList();
            System.out.println(resultList1);
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
