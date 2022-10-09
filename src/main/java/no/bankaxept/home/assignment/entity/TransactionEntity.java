package no.bankaxept.home.assignment.entity;

import java.sql.Timestamp;

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
@Table(name = "TRANSACTION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

	@Id
	@GeneratedValue
    @Column(name = "ID")
    private int ID;
	@Column(name = "UUID")
	private String UUID;
	@Column(name = "CARD")
	private String cardNumber;
	@Column(name = "BANK")
	private String bank;
	@Column(name = "AMOUNT")
	private int amount;
	@Column(name = "DATE")
    private Timestamp date;
	@Column(name = "TYPE")
	private String type;
}
