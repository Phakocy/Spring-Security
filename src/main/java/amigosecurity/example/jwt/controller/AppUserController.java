package amigosecurity.example.jwt.controller;
//Eseadi
import amigosecurity.example.jwt.model.AppUser;
import amigosecurity.example.jwt.model.Role;
import amigosecurity.example.jwt.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppUserController {

    private AppUserService appUserService;
  @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }
@GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers(){
      return ResponseEntity.ok().body(appUserService.getAllUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser> addUser(@RequestBody AppUser user){
      //Use ResponseEntity.created(uri) which is HTTPCODE 201 instead of .ok() Which is HTTP 200 because we are adding resources to the database
        URI uriLocation = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uriLocation).body(appUserService.addUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> addRole(@RequestBody Role role){
      URI roleUriLocation = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
      return ResponseEntity.created(roleUriLocation).body(appUserService.addRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUSer(@RequestBody AddRoleToUserForm form){
        appUserService.addRoleToUser(form.getUsername(),form.getRoleName());
      return ResponseEntity.ok().build();
    }
}
