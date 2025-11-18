package com.efioco.ticketsystem.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import com.efioco.ticketsystem.dto.CategoryDTO;

public class CustomerIdGenerator {

	private static final AtomicInteger COUNTER = new AtomicInteger(0);
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	/**
	 * Genera un customerId del tipo: CAT-YYYYMMDDHHMM-0001 dove CAT è
	 * un'abbreviazione della categoria.
	 */
	public static String generate(CategoryDTO category) {
		String prefix = "GEN";

        if (category != null && category.getCode() != null && !category.getCode().isBlank()) {
            prefix = category.getCode().toUpperCase();
        }

        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        int count = COUNTER.incrementAndGet();

        return String.format("%s-%s-%04d", prefix, timestamp, count);
	}
}
