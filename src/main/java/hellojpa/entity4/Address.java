package hellojpa.entity4;

import lombok.*;

import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
@EqualsAndHashCode
public class Address {
    private String city;
    private String street;
}
