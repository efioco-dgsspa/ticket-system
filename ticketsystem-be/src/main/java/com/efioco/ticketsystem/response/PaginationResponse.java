package com.efioco.ticketsystem.response;

import com.efioco.ticketsystem.bean.Pagination;

public class PaginationResponse extends Pagination {

	private int totalPage;
	private long totalRows;
	
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public long getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}
	
}
