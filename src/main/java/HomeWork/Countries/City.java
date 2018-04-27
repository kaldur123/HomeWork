package HomeWork.Countries;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "city")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, exclude = {"userList"})
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "city", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<User> userList = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    public City(String name, Country country) {
        this.name = name;
        this.country = country;
    }
}
