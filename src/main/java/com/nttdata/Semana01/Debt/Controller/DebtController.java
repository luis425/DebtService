package com.nttdata.Semana01.Debt.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nttdata.Semana01.Debt.DTO.Bank;
import com.nttdata.Semana01.Debt.DTO.Customer;
import com.nttdata.Semana01.Debt.DTO.CustomerType;
import com.nttdata.Semana01.Debt.Entity.Debt;
import com.nttdata.Semana01.Debt.Response.CustomerResponse;
import com.nttdata.Semana01.Debt.Response.DebtResponse;
import com.nttdata.Semana01.Debt.Service.DebtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/debt")
public class DebtController {

	@Autowired
	DebtService debtService;

	private String codigoValidator;

	@PostMapping
	public Mono<Debt> createDebt(@RequestBody Debt debt) throws InterruptedException {

		boolean validationvalue = this.validationRegisterDebtRequest(debt);

		if (validationvalue) {

			List<Customer> customerList = new ArrayList<>();

			CustomerResponse  endpointResponseCustomer = this.debtService.comunicationWebClientObtenerCustomerbyDniResponse(debt.getCustomer().getDniCustomer());
			
			/*
			 * Descomentar para consumir Servicio de CustomerService
			 * 
			 * Sin mock
			 * 
			 * Flux<Customer> customerServiceClientResponse = this.debtService
			 * .comunicationWebClientObtenerCustomerbyDni(debt.getCustomer().getDniCustomer(
			 * ));
			 * 
			 * customerServiceClientResponse.collectList().subscribe(customerList::addAll);
			 * 
			 */

			/*  ---------- Con Mock -------------
			 
			customerList = this.comunicationWebClientObtenerCustomerMock();
			
			*/

			try {

				// Se agrego un Sleep para detener el proceso para obtener el valor deseado del
				// Atributo list1

				long temporizador = (3 * 1000);
				Thread.sleep(temporizador);

				//if (customerList.isEmpty()) {
				if (endpointResponseCustomer == null) {	
					codigoValidator = "";
				} else {
					codigoValidator = endpointResponseCustomer.getDniCustomer();
					//codigoValidator = customerList.get(0).getDniCustomer();
				}

				log.info("Validar Codigo Repetido --->" + codigoValidator);

				// 1. Validar si existe el Usuario

				if (codigoValidator.equals("")) {
					return Mono
							.error(new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "El Usuario no existe"));
				} else {

					debt.setId(UUID.randomUUID().toString());
					debt.setDateRegister(new Date());
					//debt.setCustomer(customerList.get(0));
					debt.setCustomer(endpointResponseCustomer);
					return this.debtService.createDebt(debt);
				}

			} catch (InterruptedException e) {
				log.info(e.toString());
				Thread.currentThread().interrupt();
				return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()));
			}

		} else {
			return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Parametro de Entrada no obligatorio no completado, o "
							+ "parametro de Entrada enviado incorrectamente."));
		}

	}

	@GetMapping(value = "/debtbyId/{id}")
	public Mono<ResponseEntity<Debt>> getDebtById(@PathVariable String id) {
		var debt = this.debtService.getDebtbyId(id);
		return debt.map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@GetMapping(value = "/debtbyCodeCustomer/{codeCustomer}")
	public Mono<ResponseEntity<Debt>> getDebtByCodeCustomer(@PathVariable String codeCustomer) {

		try {

			Flux<Debt> debtflux = this.debtService.getAllDebtByCodeCustomer(codeCustomer);

			List<Debt> list1 = new ArrayList<>();

			debtflux.collectList().subscribe(list1::addAll);

			long temporizador = (5 * 1000);

			Thread.sleep(temporizador);

			if (list1.isEmpty()) {
				return null;

			} else {
				return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list1.get(0)))
						.defaultIfEmpty(ResponseEntity.notFound().build());
			}

		} catch (InterruptedException e) {
			log.info(e.toString());
			Thread.currentThread().interrupt();
			return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()));
		}
	}

	@GetMapping(value = "/debtbyDniCustomerResponse/{dni}")
	public Mono<ResponseEntity<DebtResponse>> getDebtByDNICustomerResponse(@PathVariable String dni) {

		try {

			Flux<DebtResponse> debtflux = this.debtService.getAllDebtResponseByDNICustomer(dni);

			List<DebtResponse> list1 = new ArrayList<>();

			debtflux.collectList().subscribe(list1::addAll);

			long temporizador = (5 * 1000);

			Thread.sleep(temporizador);

			if (list1.isEmpty()) {
				return null;

			} else {
				return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list1.get(0)))
						.defaultIfEmpty(ResponseEntity.notFound().build());
			}

		} catch (InterruptedException e) {
			log.info(e.toString());
			Thread.currentThread().interrupt();
			return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()));
		}
	}
	
	@GetMapping(value = "/debtbyDniCustomer/{dni}")
	public Mono<ResponseEntity<Debt>> getDebtByDNICustomer(@PathVariable String dni) {

		try {

			Flux<Debt> debtflux = this.debtService.getAllDebtByDNICustomer(dni);

			List<Debt> list1 = new ArrayList<>();

			debtflux.collectList().subscribe(list1::addAll);

			long temporizador = (7 * 1000);

			Thread.sleep(temporizador);

			if (list1.isEmpty()) {
				return null;

			} else {
				return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list1.get(0)))
						.defaultIfEmpty(ResponseEntity.notFound().build());
			}

		} catch (InterruptedException e) {
			log.info(e.toString());
			Thread.currentThread().interrupt();
			return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()));
		}
	}

	@DeleteMapping("/deleteDebt/{id}")
	public Mono<ResponseEntity<Void>> deleteDebtById(@PathVariable String id) {

		try {
			return this.debtService.deleteBank(id).map(r -> ResponseEntity.ok().<Void>build())
					.defaultIfEmpty(ResponseEntity.notFound().build());

		} catch (Exception e) {
			log.info(e.toString());
			return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()));
		}

	}

	@PutMapping(value = "/{id}") 
	public Mono<Debt> updateDebt(@RequestBody Debt debt) throws InterruptedException {

		// Condicion para validar que no se puede actualizar el ID

		if (debt.getCustomer() != null) {
			return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"El Atributo Customer no puede actualizarse por ser un dato unico"));
		}

		if (debt.getDateRegister() != null) {
			return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"El Atributo DateRegister no puede actualizarse."));
		}

		if (debt.getExpirateDate() != null) {
			return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"El Atributo ExpirateDate no puede actualizarse."));
		}

		List<Debt> debtList = new ArrayList<>();

		var debtResponse = this.debtService.getDebtbyId(debt.getId());

		debtResponse.flux().collectList().subscribe(debtList::addAll);

		try {

			// Se agrego un Sleep para detener el proceso para obtener el valor deseado del
			// Atributo list1

			long temporizador = (6 * 1000);
			Thread.sleep(temporizador);

			log.info("Validar Lista Debt -->" + debtList);
			
			if (debtList.isEmpty()) {
				codigoValidator = "";
			} else {
				codigoValidator = debtList.get(0).getId();
			}

			log.info("Validar Codigo --->" + codigoValidator);

			// 1. Validar si existe el Usuario

			if (codigoValidator.equals("")) {
				return Mono.error(new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "El Usuario no existe"));
			} else {

				debt.setId(debtList.get(0).getId());
				debt.setDateRegister(debtList.get(0).getDateRegister());
				debt.setExpirateDate(debtList.get(0).getExpirateDate());
				debt.setStatusDebt(debtList.get(0).isStatusDebt());
				debt.setCustomer(debtList.get(0).getCustomer());
				return this.debtService.createDebt(debt);
			}

		} catch (InterruptedException e) {
			log.info(e.toString());
			Thread.currentThread().interrupt();
			return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()));
		}

	}

	// Validaciones para Regitrar Debt

	public boolean validationRegisterDebtRequest(Debt debt) {

		boolean validatorbank;

		if (debt.getDebtBalance() == 0) {
			validatorbank = false;
		} else if (debt.getExpirateDate() == null) {
			validatorbank = false;
		} else if (debt.getCustomer() == null || debt.getCustomer().getDniCustomer().equals("")) {
			validatorbank = false;
		} else {
			validatorbank = true;
		}

		return validatorbank;
	}

	// Metodo para Mock

	public List<Customer> comunicationWebClientObtenerCustomerMock() {

		List<Customer> customers = new ArrayList<>();

		customers.add(new Customer("6288256a24f51675daabff60", "CP1", "PRUEBACLIENTEACTUALIZAR", "APELLIDOCLIENTE4",
				"DIRECCIONCLIENTE4", "EMAIL322@PRUEBA.COM", "2132132100", new Date(), new Date(), "213210011",
				new CustomerType(1, "Personal"), new Bank("628570778f9e833491ad8ba4", "cb1", "PRUEBABANCOACTUALIZACION",
						"PRUEBADIRECCIONACTUALIZACION")));

		log.info("Vista Customer con Dni Filtrado -->" + customers);

		return customers;

	}

}
