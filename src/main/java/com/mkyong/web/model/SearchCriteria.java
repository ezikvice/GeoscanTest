package com.mkyong.web.model;

import java.util.List;

public class SearchCriteria {

	List <Integer> data;

	public List <Integer> getData() {
		return data;
	}

	public void setData(List <Integer> data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return "SearchCriteria [data=" + data + "]";
	}

	public SearchCriteria() { }
}
