package amigosecurity.example.jwt.service;

import amigosecurity.example.jwt.Repository.AppUserRepository;
import amigosecurity.example.jwt.Repository.RoleRepository;
import amigosecurity.example.jwt.model.AppUser;
import amigosecurity.example.jwt.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Transactional
@Slf4j

public class AppUserServiceImpl implements AppUserService{

    private AppUserRepository appUserRepository;
    private RoleRepository roleRepository;

     @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public AppUser addUser(AppUser user) {
         log.info("Adding new user {} to the database", user.getName());
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
