package com.efioco.ticketsystem.response;

public class ErrorResponse {

	private String code;
	private String customerMessage;
	
	public ErrorResponse() {
		
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCustomerMessage() {
		return customerMessage;
	}
	public void setCustomerMessage(String customerMessage) {
		this.customerMessage = customerMessage;
	}
}
