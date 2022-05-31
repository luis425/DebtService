package com.nttdata.Semana01.Debt.Repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.Semana01.Debt.Entity.Debt;

@Repository
public interface DebtRepository extends ReactiveCrudRepository<Debt, String>{
}

