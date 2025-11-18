package com.efioco.ticketsystem.exceptions;

public class TicketServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TicketServiceException() {
	}

	public TicketServiceException(String message) {
		super(message);
	}

	public TicketServiceException(Throwable cause) {
		super(cause);
	}

	public TicketServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public TicketServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
