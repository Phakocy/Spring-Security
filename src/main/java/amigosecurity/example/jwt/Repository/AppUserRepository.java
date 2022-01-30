package amigosecurity.example.jwt.Repository;

import amigosecurity.example.jwt.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String Username);
}
