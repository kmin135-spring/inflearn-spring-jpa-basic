package hellojpa.entity.items;

import hellojpa.entity.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("b")
@Setter
@Getter
public class Book extends Item {
    private String author;
    private String isbn;
}
