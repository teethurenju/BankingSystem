package no.bankaxept.home.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.bankaxept.home.assignment.entity.TransactionEntity;

/**
 * Repository for Transaction Entity
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity,Integer> {

}
