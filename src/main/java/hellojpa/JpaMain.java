package hellojpa;

import hellojpa.entity2.Mem;
import hellojpa.entity2.Team;

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
            Team t1 = new Team();
            t1.setTeamName("team1");

            em.persist(t1);

            Mem m1 = new Mem();
            m1.setName("user1");
            m1.setTeam(t1);

            em.persist(m1);

            em.flush();
            em.clear();

            Mem findMem = em.find(Mem.class, m1.getId());
            boolean result = findMem.getTeam() == t1;
            System.out.println(result);

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
