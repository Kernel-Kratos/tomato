package tomato.com.restaurant.modal;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Restaurant {
    /*@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;*/
    @Id
    @Column(unique = true)
    private String licenseNo;
    private String name;
    private String address;

    //move this to user,java later
    private String firstName;
    private String lastName;
    @NaturalId
    @Column(unique = true, nullable = false)
    private int phoneNumber;
    @NaturalId
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

}
