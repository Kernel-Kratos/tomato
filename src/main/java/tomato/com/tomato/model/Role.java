package tomato.com.tomato.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;

    public Role(String roleName){
        this.roleName = roleName;
    }

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new HashSet<>();
}
