package com.efioco.ticketsystem.bean;

public class Pagination {
	
	private int page;
	private int size;
	
	public Pagination() {
		
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
}
