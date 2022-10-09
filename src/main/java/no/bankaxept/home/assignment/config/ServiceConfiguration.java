package no.bankaxept.home.assignment.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.bankaxept.home.assignment.repository.BalanceRepository;
import no.bankaxept.home.assignment.repository.TransactionRepository;
import no.bankaxept.home.assignment.service.PaymentServiceImpl;

@Configuration
@EnableJpaRepositories(basePackages = "no.bankaxept.home.assignment.repository")
@EntityScan(basePackages = "no.bankaxept.home.assignment.entity")
@EnableTransactionManagement
@EnableAutoConfiguration
public class ServiceConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public PaymentServiceImpl paymentService(RestTemplate restTemplate, ObjectMapper objectMapper,
    		BalanceRepository balanceRepository,TransactionRepository transactionRepository) {
        return new PaymentServiceImpl(restTemplate, objectMapper,balanceRepository,transactionRepository);
    }

}
