package amigosecurity.example.jwt;

import amigosecurity.example.jwt.model.AppUser;
import amigosecurity.example.jwt.model.Role;
import amigosecurity.example.jwt.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableWebSecurity
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
			appUserService.addRole(new Role(null, "ROLE_SUPER_ADMIN"));

			appUserService.addUser(new AppUser(null, "Warith", "phakocy", "1234", new ArrayList<>()));
			appUserService.addUser(new AppUser(null, "Omojola", "ekwe", "1234", new ArrayList<>()));
			appUserService.addUser(new AppUser(null, "Motunrayo", "jepay", "1234", new ArrayList<>()));
			appUserService.addUser(new AppUser(null, "Nafisat", "tyson", "1234", new ArrayList<>()));

			appUserService.addRoleToUser("phakocy", "ROLE_MANAGER");
			appUserService.addRoleToUser("ekwe","ROLE_SUPER_ADMIN");
			appUserService.addRoleToUser("tyson", "ROLE_ADMIN");
			appUserService.addRoleToUser("jepay", "ROLE_USER");
			appUserService.addRoleToUser("ekwe", "ROLE_MANAGER");
			appUserService.addRoleToUser("tyson", "ROLE_MANAGER");
		};

	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
