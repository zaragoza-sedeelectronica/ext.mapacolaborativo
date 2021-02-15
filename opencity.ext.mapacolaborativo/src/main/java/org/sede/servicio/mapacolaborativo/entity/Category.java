/* Copyright (C) 2020 Oficina Técnica de Participación, Transparenica y Gobierno Abierto del Ayuntamiento de Zaragoza
 * 
 * Este fichero es parte del "Mapas Colaborativos - Open City Zaragoza".
 *
 * "Mapas Colaborativos - Open City Zaragoza" es un software libre; usted puede utilizar esta obra respetando la licencia GNU General Public License, versión 3 o posterior, publicada por Free Software Foundation
 *
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas.
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 *
 * Para más información, puede contactar con los autores en: gobiernoabierto@zaragoza.es, sedelectronica@zaragoza.es
 */

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
