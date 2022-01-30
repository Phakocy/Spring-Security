package amigosecurity.example.jwt.service;

import amigosecurity.example.jwt.model.AppUser;
import amigosecurity.example.jwt.model.Role;

import java.util.List;

public interface AppUserService {
    AppUser addUser(AppUser user);
    Role addRole(Role role);
    void addRoleToUser(String username,String roleName );
    AppUser getUser(String username);
    List<AppUser> getAllUsers();

}
