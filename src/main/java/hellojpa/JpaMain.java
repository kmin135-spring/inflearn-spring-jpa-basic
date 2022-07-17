package hellojpa;

import hellojpa.entity.Order;
import hellojpa.entity.OrderItem;
import hellojpa.entity.items.Movie;
import hellojpa.entity2.Mem;
import hellojpa.entity2.Team;
import hellojpa.entity3.Child;
import hellojpa.entity3.Parent;
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
            Child c1 = new Child();
            c1.setName("c1");
            Child c2 = new Child();
            c2.setName("c2");

            Parent p = new Parent();
            p.setName("p1");
            p.addChild(c1);
            p.addChild(c2);

            em.persist(p);
//            em.persist(c1);
//            em.persist(c2);

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
