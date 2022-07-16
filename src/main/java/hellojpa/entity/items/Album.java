package hellojpa.entity.items;

import hellojpa.entity.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("a")
@Setter @Getter
public class Album extends Item {
    private String artist;
}
