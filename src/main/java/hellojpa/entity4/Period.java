package hellojpa.entity4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class Period {
    private LocalDateTime startDt;
    private LocalDateTime endDt;
}
