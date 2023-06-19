/**
 * Archie
 */
package com.vinplay.dto.esport;

import java.util.List;

/**
 * @author Archie
 *
 */
public class EsportRespose<T> {
	private Integer count;
	private Integer next;
	private Integer previous;
	private List<T> results;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getNext() {
		return next;
	}

	public void setNext(Integer next) {
		this.next = next;
	}

	public Integer getPrevious() {
		return previous;
	}

	public void setPrevious(Integer previous) {
		this.previous = previous;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "EsportRespose [count=" + count + ", next=" + next + ", previous=" + previous + ", results=" + results
				+ "]";
	}

}
