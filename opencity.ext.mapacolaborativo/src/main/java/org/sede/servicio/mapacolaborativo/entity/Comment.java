package org.sede.servicio.mapacolaborativo.entity;

import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.dao.EntidadBase;

/**
 *  clase Comment
 *
 * @author Ayuntamiento Zaragoza
 *
 */
@XmlRootElement(name="comment")
public class Comment extends EntidadBase {
	/**
	 *  variable screen_name
	 */
	private String screen_name;
	/**
	 * variable icon 
	 */
	private String icon;
	/**
	 * variable description 
	 */
	private String description;
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Comment [screen_name=" + screen_name + ", icon=" + icon
				+ ", description=" + description + "]";
	}
	
	
}
