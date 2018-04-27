package HomeWork.Countries;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "country")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString(callSuper = true, exclude = {"cities"})
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "country", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<City> cities = new ArrayList<>();

    public Country(String name) {
        this.name = name;
    }
}
