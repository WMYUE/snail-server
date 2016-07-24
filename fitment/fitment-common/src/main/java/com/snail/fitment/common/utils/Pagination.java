package com.snail.fitment.common.utils;

import java.util.List;
public class Pagination<T> {
	/** 每页显示条数 */
	private int pageSize = -1;
	/** 当前页 */
	private int currentPage = -1;
	/** 总页数 */
	private int totalPage = -1;
	/** 查询到的总数据量 */
	private int totalNumber = 0;
	/** 数据集 */
	private List<T> items;

	public Pagination(int pageSize, int currentPage, int totalPage,int totalCount) {
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalPage=totalPage;
		this.totalNumber = totalCount;
	}
	
	public Pagination() {
	}
	
	public Pagination(int pageSize, int currentPage, int totalCount) {
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalNumber = totalCount;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * 处理查询后的结果数据
	 * 
	 * @param items
	 *            总数
	 */
	public void build(List<T> items) {
		this.setItems(items);
		if(pageSize > 0 && currentPage > 0){
			int count = this.getTotalNumber();
			int divisor = count / this.getPageSize();
			int remainder = count % this.getPageSize();
			this.setTotalPage(remainder == 0 ? divisor == 0 ? 1 : divisor
					: divisor + 1);
		}
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
