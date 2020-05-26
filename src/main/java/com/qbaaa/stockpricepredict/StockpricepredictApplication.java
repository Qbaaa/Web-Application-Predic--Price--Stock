package com.qbaaa.stockpricepredict;

import com.qbaaa.stockpricepredict.models.*;
import com.qbaaa.stockpricepredict.repository.CompanyRepository;
import com.qbaaa.stockpricepredict.repository.RoleRepository;
import com.qbaaa.stockpricepredict.repository.StatusUpdateCompanyRepository;
import com.qbaaa.stockpricepredict.repository.UserRepository;
import com.qbaaa.stockpricepredict.service.ApiFinancial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class StockpricepredictApplication implements ApplicationRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ApiFinancial apiFinancial;

	@Autowired
	private StatusUpdateCompanyRepository statusUpdateCompanyRepository;

	private static final Logger logger = LoggerFactory.getLogger(StockpricepredictApplication.class);

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

			User user1 = new User("user1","user1@o2.pl",passwordEncoder.encode("user234"));
			user1.addRole(roleUser);
			userRepository.save(user1);

			User user2 = new User("user2","user2@o2.pl",passwordEncoder.encode("user345"));
			user2.addRole(roleUser);
			userRepository.save(user2);

			User user3 = new User("user3","user3@o2.pl",passwordEncoder.encode("user456"));
			user3.addRole(roleUser);
			userRepository.save(user3);

			User user4 = new User("Jakub","jakub@gmail.pl",passwordEncoder.encode("xaume12"));
			user4.addRole(roleUser);
			userRepository.save(user4);
		}

		if (statusUpdateCompanyRepository.existsById(1L))
		{
			LocalDate dateEnd;

			if(LocalDate.now().minusDays(1).getDayOfWeek().getValue() == 6)
				dateEnd = LocalDate.now().minusDays(2);
			else if(LocalDate.now().minusDays(1).getDayOfWeek().getValue() == 7)
				dateEnd = LocalDate.now().minusDays(3);
			else
				dateEnd = LocalDate.now().minusDays(1);

			if (!statusUpdateCompanyRepository.existsByDate(dateEnd))
			{
				logger.info("Aktualizacja Tabeli companies.");

				StatusUpdateCompany status = statusUpdateCompanyRepository.findById(1L).orElseThrow(() -> new RuntimeException("Brak statusu o podanym ID!!!"));
				updateCompanies("AAPL", status.getDate().plusDays(1), dateEnd);
				updateCompanies("MSFT", status.getDate().plusDays(1), dateEnd);
				updateCompanies("AMZN", status.getDate().plusDays(1), dateEnd);
				updateCompanies("TSLA", status.getDate().plusDays(1), dateEnd);
				updateCompanies("INTC", status.getDate().plusDays(1), dateEnd);
				updateCompanies("HPQ", status.getDate().plusDays(1), dateEnd);
				updateCompanies("ORCL", status.getDate().plusDays(1), dateEnd);
				updateCompanies("AMD", status.getDate().plusDays(1), dateEnd);
				updateCompanies("EBAY", status.getDate().plusDays(1), dateEnd);
				updateCompanies("V", status.getDate().plusDays(1), dateEnd);
				updateCompanies("NVDA", status.getDate().plusDays(1), dateEnd);

				status.setDate(dateEnd);
				statusUpdateCompanyRepository.save(status);
			}
			else{
				logger.info("Tabela companies jest już zaktualizowana.");
			}

		}
		else {
			logger.info("Tworzenie Tabeli companies.");
			LocalDate dateEnd;
			if(LocalDate.now().minusDays(1).getDayOfWeek().getValue() == 6)
				dateEnd = LocalDate.now().minusDays(2);
			else if(LocalDate.now().minusDays(1).getDayOfWeek().getValue() == 7)
				dateEnd = LocalDate.now().minusDays(3);
			else
				dateEnd = LocalDate.now().minusDays(1);

			updateCompanies("AAPL", LocalDate.of(2015,1,1), dateEnd);
			updateCompanies("MSFT", LocalDate.of(2015,1,1), dateEnd);
			updateCompanies("AMZN", LocalDate.of(2015,1,1), dateEnd);
			updateCompanies("TSLA", LocalDate.of(2015,1,1), dateEnd);
			updateCompanies("INTC", LocalDate.of(2015,1,1), dateEnd);
			updateCompanies("HPQ", LocalDate.of(2015,1,1), dateEnd);
			updateCompanies("ORCL", LocalDate.of(2015,1,1), dateEnd);
			updateCompanies("AMD", LocalDate.of(2015,1,1), dateEnd);
			updateCompanies("EBAY", LocalDate.of(2015,1,1), dateEnd);
			updateCompanies("V", LocalDate.of(2015,1,1), dateEnd);
			updateCompanies("NVDA", LocalDate.of(2015,1,1), dateEnd);
			statusUpdateCompanyRepository.save(new StatusUpdateCompany(dateEnd));
		}
	}

	@Scheduled(cron = "0 1 0 * * 2-6")
	public void scheduleUpdateCompany() {
		logger.info("Aktualizacja(Automatyczna) tabeli companies do daty:" + LocalDate.now().minusDays(1).toString());

		StatusUpdateCompany status = statusUpdateCompanyRepository.findById(1L).orElseThrow(() -> new RuntimeException("Brak statusu o podanym ID!!!"));

		updateCompanies("AAPL", status.getDate().plusDays(1), LocalDate.now().minusDays(1));
		updateCompanies("MSFT", status.getDate().plusDays(1), LocalDate.now().minusDays(1));
		updateCompanies("AMZN", status.getDate().plusDays(1), LocalDate.now().minusDays(1));
		updateCompanies("TSLA", status.getDate().plusDays(1), LocalDate.now().minusDays(1));
		updateCompanies("INTC", status.getDate().plusDays(1), LocalDate.now().minusDays(1));
		updateCompanies("HPQ", status.getDate().plusDays(1), LocalDate.now().minusDays(1));
		updateCompanies("ORCL", status.getDate().plusDays(1), LocalDate.now().minusDays(1));
		updateCompanies("AMD", status.getDate().plusDays(1), LocalDate.now().minusDays(1));
		updateCompanies("EBAY", status.getDate().plusDays(1), LocalDate.now().minusDays(1));
		updateCompanies("V", status.getDate().plusDays(1), LocalDate.now().minusDays(1));
		updateCompanies("NVDA", status.getDate().plusDays(1), LocalDate.now().minusDays(1));

		status.setDate(LocalDate.now().minusDays(1));
		statusUpdateCompanyRepository.save(status);
	}


	public void updateCompanies(String symbolStock, LocalDate dateStrat, LocalDate dateEnd)
	{
		List<Company> listCompany = apiFinancial.ReadingHistorialPriceStock(symbolStock, dateStrat, dateEnd);
		companyRepository.saveAll(listCompany);
	}

}
