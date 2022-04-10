package amigosecurity.example.jwt.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)//Eager because everytime you load a user you want to load the roles with it
    private Collection<Role>  roles = new ArrayList<>();
}
