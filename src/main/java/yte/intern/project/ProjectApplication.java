package yte.intern.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import yte.intern.project.user.service.AuthorityService;

@SpringBootApplication
public class ProjectApplication {

	private final AuthorityService authorityService;

	public ProjectApplication(AuthorityService authorityService) {
		this.authorityService = authorityService;
		authorityService.AdminAuthorityAdderToDb();
	}

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}
