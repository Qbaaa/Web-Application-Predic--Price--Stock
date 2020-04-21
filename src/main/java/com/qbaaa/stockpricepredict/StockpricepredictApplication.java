package com.qbaaa.stockpricepredict;

import com.qbaaa.stockpricepredict.models.ERole;
import com.qbaaa.stockpricepredict.models.Role;
import com.qbaaa.stockpricepredict.models.User;
import com.qbaaa.stockpricepredict.repository.RoleRepository;
import com.qbaaa.stockpricepredict.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class StockpricepredictApplication implements ApplicationRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(StockpricepredictApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		if(!roleRepository.existsByRole(ERole.ROLE_ADMIN))
		{
			Role roleAdmin = roleRepository.save(new Role(ERole.ROLE_ADMIN));
			User admin = new User("admin","admin@gmail.com",passwordEncoder.encode("admin123"));
			admin.addRole(roleAdmin);
			userRepository.save(admin);
		}

		if(!roleRepository.existsByRole(ERole.ROLE_USER))
		{
			Role roleUser = roleRepository.save((new Role(ERole.ROLE_USER)));
			User user = new User("user","user@o2.pl",passwordEncoder.encode("user123"));
			user.addRole(roleUser);
			userRepository.save(user);
		}

	}
}
