package hellojpa.entity4;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
@EqualsAndHashCode
public class Address {
    @Column(length = 10)
    private String city;
    private String street;
    private String zipcode;

    // 이런 값 타입만의 의미 있는 메서드를 가질 수 있는 점이 장점
    private String fullAddress() {
        return getCity() + " " + getStreet() + " " + getZipcode();
    }

    private boolean isValid() {
        return true;
    }
}
