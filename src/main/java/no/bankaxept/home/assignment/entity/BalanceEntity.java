package no.bankaxept.home.assignment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BALANCE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceEntity {

	@Id
	@GeneratedValue
    @Column(name = "ID")
    private int ID;
	@Column(name = "CARDNUMBER")
	private String cardNumber;
	@Column(name = "AMOUNT")
	private int amount;
}
