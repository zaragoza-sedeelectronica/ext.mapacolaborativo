package org.sede.servicio.mapacolaborativo.entity;

import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.PublicName;
import org.sede.core.dao.EntidadBase;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Clase Style
 * 
 * @author Ayuntamiento Zaragoza
 *
 */
@XmlRootElement(name="styles")
public class Style extends EntidadBase {
	/**
	 * fillColor 
	 */
	@PublicName("fillColor")
	@JsonProperty("fillColor")
	private String fillcolor;
	
	/**
	 * fillOpacity 
	 */
	@PublicName("fillOpacity")
	@JsonProperty("fillOpacity")
	private String fillopacity;
	
	/**
	 * strokeColor 
	 */
	@PublicName("strokeColor")
	@JsonProperty("strokeColor")
	private String strokecolor;
	
	/**
	 * strokeOpacity 
	 */
	@PublicName("strokeOpacity")
	@JsonProperty("strokeOpacity")
	private String strokeopacity;

	/**
	 * strokeWidth 
	 */
	@PublicName("strokeWidth")
	@JsonProperty("strokeWidth")
	private String strokewidth;	
	
	/**
	 * constructor 
	 */
	public Style() {
		super();
	}
	
	public String getFillopacity() {
		return fillopacity == null || fillopacity.length() == 0 ? null : fillopacity;
	}
	public void setFillopacity(String fillopacity) {
		this.fillopacity = fillopacity;
	}
	public String getStrokeopacity() {
		return strokeopacity == null || strokeopacity.length() == 0 ? null : strokeopacity;
	}
	public void setStrokeopacity(String strokeopacity) {
		this.strokeopacity = strokeopacity;
	}
	public String getStrokewidth() {
		return strokewidth == null || strokewidth.length() == 0 ? null : strokewidth;
	}
	public void setStrokewidth(String strokewidth) {
		this.strokewidth = strokewidth;
	}
	public String getFillcolor() {
		return fillcolor == null || fillcolor.length() == 0 ? null : fillcolor;
	}
	public void setFillcolor(String fillcolor) {
		this.fillcolor = fillcolor;
	}
	public String getStrokecolor() {
		return strokecolor == null || strokecolor.length() == 0 ? null : strokecolor;
	}
	public void setStrokecolor(String strokecolor) {
		this.strokecolor = strokecolor;
	}
	@Override
	public String toString() {
		return "Style [fillcolor=" + fillcolor + ", fillopacity=" + fillopacity
				+ ", strokecolor=" + strokecolor + ", strokeopacity="
				+ strokeopacity + ", strokewidth=" + strokewidth + "]";
	}
		
}
