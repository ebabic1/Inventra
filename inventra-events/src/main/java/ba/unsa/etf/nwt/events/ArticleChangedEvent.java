package ba.unsa.etf.nwt.events;

import ba.unsa.etf.nwt.EventAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleChangedEvent implements Serializable {
    private Long id;
    private String name;
    private Double price;
    private String category;
    private Integer quantity;
    private EventAction action;
}