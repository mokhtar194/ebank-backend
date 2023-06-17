package org.sid.ebankbackend;

import org.sid.ebankbackend.dtos.BankAccountDTO;
import org.sid.ebankbackend.dtos.CurrentBankAccountDTO;
import org.sid.ebankbackend.dtos.CustomerDTO;
import org.sid.ebankbackend.dtos.SavingBankAccountDTO;
import org.sid.ebankbackend.entities.*;
import org.sid.ebankbackend.enums.AccountStatus;
import org.sid.ebankbackend.enums.OperationType;
import org.sid.ebankbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebankbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankbackend.repositories.BankOpperationRepository;
import org.sid.ebankbackend.repositories.BankRepository;
import org.sid.ebankbackend.repositories.CustomerRepository;
import org.sid.ebankbackend.services.BankAccoutService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(EbankBackendApplication.class, args);
    }
    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(new String[]{"http://localhost:4200"}).allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*");;
            }
        };
    }
    @Bean
    CommandLineRunner commandLineRunner (BankAccoutService bankAccoutService){
        return args -> {
            Stream.of("Hassan","Imane","Mohamed").forEach(name->{
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccoutService.saveCustomer(customer);
            });
            bankAccoutService.listCustomers().forEach(customer -> {
                try {
                    bankAccoutService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                    bankAccoutService.saveSavingBankAccount(Math.random()*120000, 5.5,customer.getId());
                    List<BankAccountDTO> bankAccounts = bankAccoutService.bankAccountList();
                    for (BankAccountDTO bankAccount:bankAccounts){
                        for (int i = 0; i <10 ; i++){
                            String accountId;
                            if(bankAccount instanceof SavingBankAccountDTO){
                                accountId=((SavingBankAccountDTO) bankAccount).getId();
                            } else{
                                accountId=((CurrentBankAccountDTO) bankAccount).getId();
                            }
                            bankAccoutService.credit(accountId,10000+Math.random()*120000,"Credit");
                            bankAccoutService.debit(accountId,1000+Math.random()*9000,"Debit");
                            }
                    }
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
                    e.printStackTrace();
                }
            });
        };
    }
//    @Bean
    CommandLineRunner commandLineRunner(BankRepository bankRepository){
        return args -> {
            BankAccount bankaccount = bankRepository.findById("432d8547-a08e-4c10-9826-93bf664e6961").orElse(null);
            System.out.println("************************************");
            if(bankaccount!=null) {
                System.out.println(bankaccount.getId());
                System.out.println(bankaccount.getBalance());
                System.out.println(bankaccount.getStatus());
                System.out.println(bankaccount.getCreatedAt());
                System.out.println(bankaccount.getCustomer().getName());
                if (bankaccount instanceof CurrentAccount) {
                    System.out.println("Over Draft =>" + ((CurrentAccount) bankaccount).getOverDraft());
                } else if (bankaccount instanceof SavingAccount) {
                    System.out.println("Rate =>" + ((SavingAccount) bankaccount).getInterestRate());
                }
                bankaccount.getAccountOperations().forEach(op -> {
                    System.out.println("-------------");
                    System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());
                });
            }
        };
    }
    //    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankRepository bankRepository,
                            BankOpperationRepository bankOpperationRepository){
        return  args -> {
            Stream.of("Hassan","Rim","Ibtihal").forEach(name->{
                Customer customer= new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*9000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                bankRepository.save(savingAccount);
            });
            bankRepository.findAll().forEach(acc ->{
                for(int i=0; i<10;i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);

                    bankOpperationRepository.save(accountOperation);
                }
            });

        };
    }

}
