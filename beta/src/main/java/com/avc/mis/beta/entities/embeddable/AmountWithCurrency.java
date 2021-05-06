/**
 * 
 */
package com.avc.mis.beta.entities.embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.avc.mis.beta.validation.groups.PositiveOrZeroAmount;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Zvi
 *
 */
@Data
@NoArgsConstructor
@Embeddable
public class AmountWithCurrency implements Cloneable {
	
	public static final int SCALE = 3;

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.###");


	@Column(precision = 19, scale = SCALE)
	@NotNull(message = "Amount is required")
	@PositiveOrZero(message = "Amount can't be negative", groups = PositiveOrZeroAmount.class)
	private BigDecimal amount;
	
	@Setter(value = AccessLevel.NONE)
	@NotNull(message = "Currency is required")
	private Currency currency;

	public AmountWithCurrency(BigDecimal amount, Currency currency) {
		super();
		this.amount = amount.setScale(SCALE, RoundingMode.HALF_DOWN);
		this.currency = currency;
	}
	
	public AmountWithCurrency(String amount, String currencyCode) {
		super();
		this.amount = (new BigDecimal(amount)).setScale(SCALE, RoundingMode.HALF_DOWN);
		this.currency = Currency.getInstance(currencyCode);
	}

	
	@Override
	public AmountWithCurrency clone() {
		return new AmountWithCurrency(amount, currency);
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount.setScale(SCALE, RoundingMode.HALF_DOWN);
	}
	
	public void setCurrency(String currencyCode) {
		this.currency = Currency.getInstance(currencyCode);
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
	public String getCurrency() {
		return Optional.ofNullable(this.currency).map(c -> c.getCurrencyCode()).orElse(null);
	}
	
	public String getValue() {
		if(!isFilled()) {
			return null;
		}
		return String.format("%s %s", 
				DECIMAL_FORMAT.format(this.amount), 
				this.currency);
	}
	
	public boolean isFilled() {
		return this.amount != null && this.currency != null;
	}
	
	public int signum() {
		if(amount == null) {
			throw new NullPointerException();
		}
		return amount.signum();
	}

}
