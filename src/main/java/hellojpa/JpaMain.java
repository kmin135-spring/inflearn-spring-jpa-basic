package hellojpa;

import hellojpa.entity.Order;
import hellojpa.entity.OrderItem;
import hellojpa.entity.items.Movie;
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
            Movie m = new Movie();
            m.setActor("a");
            m.setDirector("d");
            m.setName("n");
            m.setPrice(1);
            m.setStockQuantity(2);

            em.persist(m);

            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, m.getId());

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
