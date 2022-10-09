package no.bankaxept.home.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import no.bankaxept.home.assignment.entity.BalanceEntity;

/**
 * Repository for Balance Entity
 */

@Repository
public interface BalanceRepository extends JpaRepository<BalanceEntity,Integer>{

	@Query(value="SELECT * FROM BALANCE WHERE CARDNUMBER=:cardNumber",nativeQuery = true)
    List<BalanceEntity> checkBalance(@Param("cardNumber")String cardNumber);

	@Modifying
	@Transactional
	@Query(value="UPDATE BALANCE SET AMOUNT=:balance WHERE CARDNUMBER=:cardNumber",nativeQuery = true)
	void updateBalance(@Param("balance")Long balance,@Param("cardNumber")String cardNumber);
	
}
