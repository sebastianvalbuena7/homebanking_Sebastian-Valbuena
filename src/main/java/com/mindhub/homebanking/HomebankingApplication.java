package com.mindhub.homebanking;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	public double calculatePercentage(String loanName) {
		double percentageLoan = 0;
		if(loanName.equals("Personal")) {
			percentageLoan = 1.20;
		} else if(loanName.equals("Mortgage")) {
			percentageLoan = 1.30;
		} else if(loanName.equals("Automotive")) {
			percentageLoan = 1.40;
		} else {
			percentageLoan = 1.50;
		}
		return percentageLoan;
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientrepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			Client melba = new Client("Melba", "Lorenzo", passwordEncoder.encode("password123"), "melbalorenzo@mindhub.com");
			clientrepository.save(melba);

			Client admin = new Client("admin", "admin", passwordEncoder.encode("admin") ,"admin@correo.com");
			clientrepository.save(admin);

			Loan loanMortgage = new Loan("Mortgage", 500000, List.of(12, 24, 36, 48, 60),calculatePercentage("Mortgage"));
			loanRepository.save(loanMortgage);
			Loan loanPersonal = new Loan("Personal", 100000, List.of(6, 12, 24), calculatePercentage("Personal"));
			loanRepository.save(loanPersonal);
			Loan loanAutomotive = new Loan("Automotive", 300000, List.of(6, 12, 24, 36), calculatePercentage("Automotive"));
			loanRepository.save(loanAutomotive);
		};
	}
}