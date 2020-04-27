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
import java.time.LocalTime;
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
		}

		if (statusUpdateCompanyRepository.existsById(1L)) {

			StatusUpdateCompany status = statusUpdateCompanyRepository.findById(1L).orElseThrow(() -> new RuntimeException("Brak statusu o podanym ID!!!"));

			if (!statusUpdateCompanyRepository.existsByDate(LocalDate.now()))
			{
				List<Company> listGoogl = apiFinancial.ReadingHistorialPriceStock("GOOGL", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listGoogl);

				List<Company> listAAPL = apiFinancial.ReadingHistorialPriceStock("AAPL", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listAAPL);

				List<Company> listMSFT = apiFinancial.ReadingHistorialPriceStock("MSFT", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listMSFT);

				List<Company> listAMZN = apiFinancial.ReadingHistorialPriceStock("AMZN", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listAMZN);

				List<Company> listTSLA = apiFinancial.ReadingHistorialPriceStock("TSLA", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listTSLA);

				List<Company> listINTC = apiFinancial.ReadingHistorialPriceStock("INTC", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listINTC);

				List<Company> listCSCO = apiFinancial.ReadingHistorialPriceStock("CSCO", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listCSCO);

				List<Company> listHPQ = apiFinancial.ReadingHistorialPriceStock("HPQ", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listHPQ);

				List<Company> listORCL = apiFinancial.ReadingHistorialPriceStock("ORCL", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listORCL);

				List<Company> listAMD = apiFinancial.ReadingHistorialPriceStock("AMD", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listAMD);

				List<Company> listEBAY = apiFinancial.ReadingHistorialPriceStock("EBAY", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listEBAY);

				List<Company> listV = apiFinancial.ReadingHistorialPriceStock("V", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listV);

				List<Company> listNVDA = apiFinancial.ReadingHistorialPriceStock("NVDA", status.getDate().plusDays(1), LocalDate.now());
				companyRepository.saveAll(listNVDA);

				logger.info("Aktualizacja Tabeli companies.");
				status.setDate(LocalDate.now());
				statusUpdateCompanyRepository.save(status);
			}
			else {
				logger.info("Tabela companies jest już zaktualizowana.");
			}


		}
		else {
			logger.info("Tworzenie Tabeli companies.");
			List<Company> listGoogl = apiFinancial.ReadingHistorialPriceStock("GOOGL", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listGoogl);

			List<Company> listAAPL = apiFinancial.ReadingHistorialPriceStock("AAPL", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listAAPL);

			List<Company> listMSFT = apiFinancial.ReadingHistorialPriceStock("MSFT", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listMSFT);

			List<Company> listAMZN = apiFinancial.ReadingHistorialPriceStock("AMZN", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listAMZN);

			List<Company> listTSLA = apiFinancial.ReadingHistorialPriceStock("TSLA", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listTSLA);

			List<Company> listINTC = apiFinancial.ReadingHistorialPriceStock("INTC", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listINTC);

			List<Company> listCSCO = apiFinancial.ReadingHistorialPriceStock("CSCO", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listCSCO);

			List<Company> listHPQ = apiFinancial.ReadingHistorialPriceStock("HPQ", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listHPQ);

			List<Company> listORCL = apiFinancial.ReadingHistorialPriceStock("ORCL", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listORCL);

			List<Company> listAMD = apiFinancial.ReadingHistorialPriceStock("AMD", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listAMD);

			List<Company> listEBAY = apiFinancial.ReadingHistorialPriceStock("EBAY", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listEBAY);

			List<Company> listV = apiFinancial.ReadingHistorialPriceStock("V", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listV);

			List<Company> listNVDA = apiFinancial.ReadingHistorialPriceStock("NVDA", LocalDate.of(2015,1,1), LocalDate.now());
			companyRepository.saveAll(listNVDA);

			statusUpdateCompanyRepository.save(new StatusUpdateCompany(LocalDate.now()));
		}
	}

	@Scheduled(cron = "0 0 10 * * 2-6")
	public void scheduleUpdateCompany() {
		StatusUpdateCompany status = statusUpdateCompanyRepository.findById(1L).orElseThrow(() -> new RuntimeException("Brak statusu o podanym ID!!!"));

		List<Company> listGoogl = apiFinancial.ReadingHistorialPriceStock("GOOGL", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listGoogl);

		List<Company> listAAPL = apiFinancial.ReadingHistorialPriceStock("AAPL", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listAAPL);

		List<Company> listMSFT = apiFinancial.ReadingHistorialPriceStock("MSFT", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listMSFT);

		List<Company> listAMZN = apiFinancial.ReadingHistorialPriceStock("AMZN", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listAMZN);

		List<Company> listTSLA = apiFinancial.ReadingHistorialPriceStock("TSLA", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listTSLA);

		List<Company> listINTC = apiFinancial.ReadingHistorialPriceStock("INTC", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listINTC);

		List<Company> listCSCO = apiFinancial.ReadingHistorialPriceStock("CSCO", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listCSCO);

		List<Company> listHPQ = apiFinancial.ReadingHistorialPriceStock("HPQ", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listHPQ);

		List<Company> listORCL = apiFinancial.ReadingHistorialPriceStock("ORCL", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listORCL);

		List<Company> listAMD = apiFinancial.ReadingHistorialPriceStock("AMD", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listAMD);

		List<Company> listEBAY = apiFinancial.ReadingHistorialPriceStock("EBAY", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listEBAY);

		List<Company> listV = apiFinancial.ReadingHistorialPriceStock("V", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listV);

		List<Company> listNVDA = apiFinancial.ReadingHistorialPriceStock("NVDA", status.getDate(), LocalDate.now());
		companyRepository.saveAll(listNVDA);

		status.setDate(LocalDate.now());
		statusUpdateCompanyRepository.save(status);

		logger.info("Aktualizacja(Automatyczna) tabeli companies :" + LocalTime.now().toString());
	}

}
