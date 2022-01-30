package amigosecurity.example.jwt;

import amigosecurity.example.jwt.model.AppUser;
import amigosecurity.example.jwt.model.Role;
import amigosecurity.example.jwt.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class JwtApplication {

	public static void main(String[] args) {

		SpringApplication.run(JwtApplication.class, args);
	}
@Bean
	CommandLineRunner run(AppUserService appUserService){
		return args -> {
			appUserService.addRole(new Role(null, "ROLE_USER"));
			appUserService.addRole(new Role(null, "ROLE_MANAGER"));
			appUserService.addRole(new Role(null, "ROLE_ADMIN"));
			appUserService.addRole(new Role(null, "RLOE_SUPER_ADMIN"));

			appUserService.addUser(new AppUser(null, "Warith", "phakocy", "1234", new ArrayList<>()));
			appUserService.addUser(new AppUser(null, "Omojola", "ekwe", "1234", new ArrayList<>()));
			appUserService.addUser(new AppUser(null, "Motunrayo", "horlahomotomi", "1234", new ArrayList<>()));
			appUserService.addUser(new AppUser(null, "Nafisat", "abeke", "1234", new ArrayList<>()));

			appUserService.addRoleToUser("phakocy", "ROLE_MANAGER");
			appUserService.addRoleToUser("ekwe","ROLE_SUPER_ADMIN");
			appUserService.addRoleToUser("abeke", "ROLE_ADMIN");
			appUserService.addRoleToUser("horlahomotomi", "ROLE_USER");
			appUserService.addRoleToUser("ekwe", "ROLE_MANAGER");
			appUserService.addRoleToUser("abeke", "ROLE_MANAGER");
		};

	}
}
