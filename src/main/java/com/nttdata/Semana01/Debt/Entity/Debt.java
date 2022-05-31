package com.nttdata.Semana01.Debt.Entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nttdata.Semana01.Debt.DTO.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
 
@AllArgsConstructor
@Document
@Data
@Builder
public class Debt {

	@Id
	private String id; 
	  
	// Monto De la Deuda
	private double debtBalance;
	 
	@JsonFormat(pattern="dd-MM-yyyy" , timezone="GMT-05:00")
	private Date dateRegister;
	
	@JsonFormat(pattern="dd-MM-yyyy" , timezone="GMT-05:00")
	private Date expirateDate;
	 
	// Estado 
	private boolean statusDebt; 
	
	private Customer customer;
	
}
