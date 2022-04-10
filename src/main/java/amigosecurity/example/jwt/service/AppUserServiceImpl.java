package amigosecurity.example.jwt.service;

import amigosecurity.example.jwt.Repository.AppUserRepository;
import amigosecurity.example.jwt.Repository.RoleRepository;
import amigosecurity.example.jwt.model.AppUser;
import amigosecurity.example.jwt.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private AppUserRepository appUserRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         AppUser user = appUserRepository.findByUsername(username);
         if(user == null){
             log.error("User not found in the database");
             throw new UsernameNotFoundException("User not found in the database");
         } else {
             log.info("User found in the database: {}", username);
         }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
         user.getRoles().forEach(role -> {
             authorities.add(new SimpleGrantedAuthority(role.getName()));
         });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
    @Override
    public AppUser addUser(AppUser user) {
         log.info("Adding new user {} to the database", user.getName());
         user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    @Override
    public Role addRole(Role role) {
         log.info("Adding new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Assigning role {} to user {}", roleName, username );
        AppUser user = appUserRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        user.getRoles().add(role);
    }

    @Override
    public AppUser getUser(String username) {
         log.info("Fetching user {} from the database", username);
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> getAllUsers() {
         log.info("fetching all the users from the database");
        return appUserRepository.findAll();
    }
}
