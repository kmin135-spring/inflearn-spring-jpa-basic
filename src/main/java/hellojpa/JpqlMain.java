package hellojpa;

import hellojpa.entity.Member;
import hellojpa.jpql.Member2;

import javax.persistence.*;

public class JpqlMain {
    public static void main(String[] args) {
        // 앱당 1개
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 1개 쓰레드 단위
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member2 m = new Member2();
            m.setUsername("m1");
            m.setAge(10);

            em.persist(m);

            TypedQuery<Member2> typeQuery = em.createQuery("select m from Member2 m where m.age = :age", Member2.class);
            typeQuery.setParameter("age", 10);

            typeQuery.getResultList();

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
