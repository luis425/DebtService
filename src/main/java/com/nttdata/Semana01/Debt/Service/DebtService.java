package com.nttdata.Semana01.Debt.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.Semana01.Debt.DTO.Customer;
import com.nttdata.Semana01.Debt.Entity.Debt;
import com.nttdata.Semana01.Debt.Repository.DebtRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service 
public class DebtService {

	private WebClient customerServiceClient = WebClient.builder().baseUrl("http://localhost:8081").build();
	
	@Autowired
	DebtRepository debtRepository;
	
	public Mono<Debt> createDebt(Debt debt) {
		return debtRepository.save(debt);
	}
	 
	public Flux<Debt> getAllDebtByDNICustomer(String dni) {
		return debtRepository.findAll().filter(x -> x.getCustomer().getDniCustomer().equals(dni));
	}
	
	public Flux<Debt> getAllDebtByCodeCustomer(String codeCustomer) {
		return debtRepository.findAll().filter(x -> x.getCustomer().getCodeCustomer().equals(codeCustomer));
	}
	
	public Mono<Debt> getDebtbyId(String id) {
		return debtRepository.findById(id);
	}
	
	public Flux<Customer> comunicationWebClientObtenerCustomerbyDni(String dni) throws InterruptedException {

		Flux<Customer> customerServiceClientResponse = customerServiceClient.get()
				.uri("/customer/customerbydni/".concat(dni)).accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToFlux(Customer.class).log().doOnError(ex -> {
					throw new RuntimeException("the exception message is - " + ex.getMessage());
				});
		long temporizador = (5 * 1000);
		Thread.sleep(temporizador);
		
		return customerServiceClientResponse;

	}
	
	public Mono<Debt> deleteBank(String id) {
		return debtRepository.findById(id).flatMap(existsBank -> debtRepository
				.delete(existsBank).then(Mono.just(existsBank)));
	}
	 
}
