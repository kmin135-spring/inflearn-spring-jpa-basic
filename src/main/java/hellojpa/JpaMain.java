package hellojpa;

import hellojpa.entity4.Address;
import hellojpa.entity4.Period;
import hellojpa.entity4.MyUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {
    public static void main(String[] args) {
        // 앱당 1개
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 1개 쓰레드 단위
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            MyUser u = new MyUser();
            u.setName("u1");
            u.setAddress(new Address("c1", "s1"));
            u.setPeriod(new Period(LocalDateTime.now(), LocalDateTime.now().plusDays(1L)));

            em.persist(u);

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
