package nl.rvkit.accountmicroservice.user;

import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String email;

    @NonNull
    @Column(nullable = false)
    private String firstName;

    @Column
    private String affix;

    @Column(nullable = false)
    private String lastName;

    @Column
    private String auth0UserId;

    public String getFullLastName(){
        if (affix == null){
            return lastName;
        }
        return affix + " " + lastName;
    }

}
