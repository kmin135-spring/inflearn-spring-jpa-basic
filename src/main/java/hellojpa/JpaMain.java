package hellojpa;

import hellojpa.entity.Order;
import hellojpa.entity.OrderItem;
import hellojpa.entity.items.Movie;
import hellojpa.entity2.Mem;
import hellojpa.entity2.Team;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // 앱당 1개
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 1개 쓰레드 단위
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team t = new Team();
            t.setTeamName("t1");

            em.persist(t);

            Mem m = new Mem();
            m.setName("m1");
            m.setTeam(t);

            em.persist(m);
            em.flush();
            em.clear();

            Mem findMem = em.find(Mem.class, m.getId());

            System.out.println(findMem.getName());
            Hibernate.initialize(findMem.getTeam());
            System.out.println("===");
            System.out.println(findMem.getTeam().getClass());
            System.out.println(findMem.getTeam().getTeamName());

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
