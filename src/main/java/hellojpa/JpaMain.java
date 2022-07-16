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
            m1.changeTeam(t1);

            em.persist(m1);

            // 순수 객체 상태를 고려하여 양방향 매핑을 모두 잡아주자
            // 이렇게 수동으로 하기 보다는 연관관계 편의 메서드를 잡아서 원자적으로 처리하자
//            t1.getMembers().add(m1);

//            em.flush();
//            em.clear();

//            Mem findMem = em.find(Mem.class, m1.getId());
//            boolean result = findMem.getTeam() == t1;
//            System.out.println(result);

            Team findTeam = em.find(Team.class, t1.getId());
            System.out.println(findTeam.getMembers());

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
