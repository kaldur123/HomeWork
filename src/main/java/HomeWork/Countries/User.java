package HomeWork.Countries;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "age")
    private int age;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;
}
