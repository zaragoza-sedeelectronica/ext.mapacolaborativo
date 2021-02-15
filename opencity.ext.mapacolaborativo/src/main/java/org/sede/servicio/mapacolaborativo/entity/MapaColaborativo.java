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
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.AvoidLastModifiedHeader;
import org.sede.core.anotaciones.InList;
import org.sede.core.anotaciones.PathId;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.RequiredSinValidacion;
import org.sede.core.dao.EntidadBase;
import org.sede.core.utils.Funciones;
import org.sede.servicio.mapacolaborativo.MapaColaborativoController;


/**
 *  clase MapaColaborativo
 *  
 *  @author Ayuntamiento Zaragoza
 *
 */
@XmlRootElement(name="mapa")
@PathId("/" + MapaColaborativoController.MAPPING)
@AvoidLastModifiedHeader
public class MapaColaborativo extends EntidadBase {

	/**
	 *  variable id
	 */
	@RequiredSinValidacion
	private BigDecimal id;
	/**
	 *  variable title
	 */
	@RequiredSinValidacion
	private String title;
	/**
	 *  variable type
	 */
	@InList({"public", "private", "collaborative"})
	@RequiredSinValidacion
	private String type;
	/**
	 *  variable owned 
	 */
	@Permisos(Permisos.DET)
	Boolean owned;
	/**
	 *  variable icon
	 */
	private String icon;
	/**
	 *  variable lastUpdated
	 */
	private Date lastUpdated;
	/**
	 *  variable pois
	 */
	private List<Poi> pois;
	/**
	 *  variable categories
	 */
	private List<Category> categories;
	
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
	public List<Poi> getPois() {
		return pois;
	}
	public void setPois(List<Poi> pois) {
		this.pois = pois;
	}
	
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Boolean getOwned() {
		return owned;
	}
	public void setOwned(Boolean owned) {
		this.owned = owned;
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Override
	public String toString() {
		return "MapaColaborativo [id=" + id + ", title=" + title + ", type="
				+ type + ", owned=" + owned + ", lastUpdated=" + lastUpdated
				+ ", icon=" + icon
				+ ", pois=" + pois + "]";
	}
	
	public String getUri() {
		return Funciones.obtenerPath(this.getClass()) + getId();
	}
	
}
