package yte.intern.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import yte.intern.project.common.configuration.AdminConstructer;
import yte.intern.project.user.service.AuthorityService;
import yte.intern.project.user.service.UserService;

@SpringBootApplication
public class ProjectApplication {


	@Autowired
	public ProjectApplication(AuthorityService authorityService, AdminConstructer adminConstructer, UserService userService) {
		authorityService.AdminAuthorityAdderToDb();

		adminConstructer.firstAdmin();

		userService.loadUserByUsername("ADMIN").getAuthorities().stream().map(Object::toString);
	}



	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);


	}

}
