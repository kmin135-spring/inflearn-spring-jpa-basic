package hellojpa;

import hellojpa.entity4.Address;
import hellojpa.entity4.Period;
import hellojpa.entity4.MyUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
            u.setHomeAddress(new Address("c1", "s1"));
            u.setPeriod(new Period(LocalDateTime.now(), LocalDateTime.now().plusDays(1L)));

            u.getFavoriteFoods().add("치킨");
            u.getFavoriteFoods().add("피자");

            u.getAddressHistory().add(new Address("old-c1", "old-s1"));
            u.getAddressHistory().add(new Address("old-c2", "old-s2"));

            em.persist(u);

            em.flush();
            em.clear();

            System.out.println("========== 영속성 컨텍스트 다시 시작 ===========");

            MyUser findUser = em.find(MyUser.class, u.getId());
            System.out.println(findUser.getFavoriteFoods());

            // 값 타입은 바꾸고 싶으면 새로 만들어라 (불변 객체, 사이드 이펙 방지)
            findUser.setHomeAddress(new Address("c2", findUser.getHomeAddress().getStreet()));

            // 치킨을 한식으로 바꾸고 싶다면?
            findUser.getFavoriteFoods().remove("치킨");
            findUser.getFavoriteFoods().add("한식");

            // old1 을 new1으로 바꾸려면?
            findUser.getAddressHistory().remove(new Address("old-c1", "old-s1"));
            findUser.getAddressHistory().add(new Address("new-c1", "old-s1"));

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
