package hellojpa.jpql;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Address2 {
    private String city;
    private String street;
    private String zipcode;
}
