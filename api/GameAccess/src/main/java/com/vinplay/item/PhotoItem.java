/**
 * Archie
 */
package com.vinplay.item;

import java.io.Serializable;

/**
 * @author Archie
 *
 */
public class PhotoItem implements Serializable {
	
	
	private static final long serialVersionUID = 6463270059647408266L;
	/**
	 * 
	 */
	private Long id;
	private String title;
	private Integer type;
	private String path;
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
