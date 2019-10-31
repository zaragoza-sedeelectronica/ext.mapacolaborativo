package org.sede.servicio.mapacolaborativo.entity;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.dao.EntidadBase;

/**
 * Clase categoria
 *
 * @author Ayuntamiento Zaragoza
 *
 */
@XmlRootElement(name="category")
public class Category extends EntidadBase {
	/**
	 * variable id
	 */
	private BigDecimal id;
	/**
	 * variable title
	 */ 
	private String title;
	/**
	 * variable type
	 */
	private BigDecimal type;
	/**
	 * variable count
	 */
	private BigDecimal count;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getType() {
		return type;
	}

	public void setType(BigDecimal type) {
		this.type = type;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", title=" + title + ", type=" + type
				+ "]";
	}
	
	
}
