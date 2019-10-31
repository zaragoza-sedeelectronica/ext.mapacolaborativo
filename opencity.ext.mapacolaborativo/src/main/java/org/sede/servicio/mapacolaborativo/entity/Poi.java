package org.sede.servicio.mapacolaborativo.entity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.solr.common.SolrDocument;
import org.sede.core.anotaciones.CampoSolr;
import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.GeoJson;
import org.sede.core.anotaciones.HtmlContent;
import org.sede.core.anotaciones.Interno;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RdfMultiple;
import org.sede.core.anotaciones.RequiredSinValidacion;
import org.sede.core.anotaciones.ValidHTML;
import org.sede.core.dao.EntidadBase;
import org.sede.core.dao.Solr;
import org.sede.core.geo.Geometria;
import org.sede.core.geo.LineString;
import org.sede.core.geo.Polygon;
import org.sede.core.geo.Punto;
import org.sede.core.rest.solr.Faceta;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;

/**
 *  clase Poi (point of interest)
 *  
 *  @author Ayuntamiento Zaragoza
 *
 */
@XmlRootElement(name="poi")
@GeoJson(title = "Puntos de Interés", link = "/ciudad/servicios/buscar_Web",
icon = "generico", description = "Puntos de interés")
//@ResultsOnly(xmlroot="pois")
@Rdf(uri = "/" + "datosabiertos/def" + "/urbanismo-infraestructura/Equipamientos#PoI", prefijo = "poi")
public class Poi extends EntidadBase {
	/**
	 *  id
	 */
	@CampoSolr
	@Id	
	@Rdf(contexto = Context.DCT, propiedad = "identifier")
	private String id;
	
	/**
	 *  title
	 */
	@CampoSolr
	@RdfMultiple({@Rdf(contexto = Context.GEONAMES, propiedad = "name"), @Rdf(contexto = Context.RDFS, propiedad = "label")})
	@RequiredSinValidacion
	private String title;
	
	/**
	 * link 
	 */
	@CampoSolr("uri")
	@Rdf(contexto = Context.SCHEMA, propiedad = "url")
	private String link;
	
	/**
	 * description  
	 */
	@CampoSolr("texto_t")
	@RdfMultiple({@Rdf(contexto = Context.RDFS, propiedad = "comment"),@Rdf(contexto = Context.DCT, propiedad = "description")})
	@ValidHTML @HtmlContent
	private String description;
	
	/**
	 * category 
	 */
	@CampoSolr("category")
	@Rdf(contexto = Context.VCARD, propiedad = "category")
	private String category;
	
	/**
	 *  lastUpdated
	 */
	@CampoSolr
	@Rdf(contexto = Context.DCT, propiedad = "modified")
	private Date lastUpdated;
	
	/**
	 * icon 
	 */
	@Rdf(contexto = Context.FOAF, propiedad = "depiction")
	private String icon;
	
	/**
	 * icongeneral  
	 */
	@Interno
	private String icongeneral;
	
	/**
	 * style 
	 */
	private Style style;
	
	/**
	 * coordenadas 
	 */
	@Interno
	private String coordenadas;
	
	/**
	 * updateable 
	 */
//	@Permisos(Permisos.DET)
	private Boolean updateable;
	
	/**
	 * comments 
	 */
	private List<Comment> comments;
	
	/**
	 * geometry 
	 */
	@CampoSolr("x_coordinate,y_coordinate")
	@Rdf(contexto = Context.GEO, propiedad = "geometry")
	private Geometria geometry;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Style getStyle() {
		return style;
	}
	public void setStyle(Style style) {
		this.style = style;
	}
	public Geometria getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometria geometry) {
		if (geometry != null) {
			if (geometry.getType().equals(Geometria.LINESTRING)) {
				LineString linea = new LineString();
				linea.setType(Geometria.LINESTRING);
				if (geometry.getCoordinates() instanceof ArrayList) {
					List<?> e = (ArrayList<?>)geometry.getCoordinates();				
					Double[][] l = new Double[e.size()][2];
					for (int i = 0; i < e.size(); i++) {
						List<?> interno = (ArrayList<?>)e.get(i);
						l[i][0] = (Double)interno.get(0);
						l[i][1] = (Double)interno.get(1);
						if (l[i][0] < 0) {// formato wgs84
							Double[] coords = Punto.pasarAUTM30(
									l[i][0],
									l[i][1]);
							l[i][0] = coords[1];
							l[i][1] = coords[0];
						}					
					}
					
					linea.setCoordinates(l);
					this.geometry = linea;
				} else {
					this.geometry = geometry;
				}
			} else if (geometry.getType().equals(Geometria.POLIGON)) {
				Polygon poligono = new Polygon();
				poligono.setType(Geometria.POLIGON);
				if (geometry.getCoordinates() instanceof ArrayList) {
					List<?> a = (ArrayList<?>)geometry.getCoordinates();
					List<?> e = (ArrayList<?>)a.get(0);
					Double[][] l = new Double[e.size()][2];
					for (int i = 0; i < e.size(); i++) {
						List<?> interno = (ArrayList<?>)e.get(i);
						l[i][0] = (Double)interno.get(0);
						l[i][1] = (Double)interno.get(1);
						if (l[i][0] < 0) {// formato wgs84
							Double[] coords = Punto.pasarAUTM30(
									l[i][0],
									l[i][1]);
							l[i][0] = coords[1];
							l[i][1] = coords[0];
						}
					}
					poligono.setCoordinates(l);
					this.geometry = poligono;
				} else {
					this.geometry = geometry;
				}
				
			} else {
				Punto punto = new Punto();
				if (geometry.getCoordinates() instanceof ArrayList) {
					List<?> a = (ArrayList<?>)geometry.getCoordinates();
					Double[] coords = new Double[]{(Double)a.get(0), (Double)a.get(1)};
					punto.setCoordinates(coords);
					
					if (punto.formatoWgs84()) {
						Double[] intercambiada = Punto.pasarAUTM30(
								punto.getCoordinates()[0],
								punto.getCoordinates()[1]);
						punto.setCoordinates(new Double[]{intercambiada[1], intercambiada[0]});
					}
					
					this.geometry = punto;
				} else {
					this.geometry = geometry;		
				}
			}
		}else{
			this.geometry = geometry;
		}
	}
	
	public String getCoordenadas() {
		return coordenadas;
	}
	public void setCoordenadas(String coordenadas) {
		if (coordenadas != null) {
			String[] ret = coordenadas.split("#");
			String[] coord = ret[0].split(",");
			if (ret[1].equals("3")) {
				this.setGeometry(new Punto(new Double[]{Double.parseDouble(coord[1].trim()),Double.parseDouble(coord[0].trim())}));
			} else if (ret[1].equals("5")) {
				this.setGeometry(new LineString(Funciones.stringToCoords(ret[0])));
			} else if (ret[1].equals("6")) {
				this.setGeometry(new Polygon(Funciones.stringToCoords(ret[0])));
			}
		}
	}
	public Boolean getUpdateable() {
		return updateable;
	}
	public void setUpdateable(Boolean updateable) {
		this.updateable = updateable;
	}
	
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getLink() {
		return link == null || "".equals(link.trim()) ? null : link;
	}
	public void setLink(String publicUri) {
		this.link = publicUri;
	}
	
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getIcon() {
		return icon == null || icon.length() == 0 ? getIcongeneral() : icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	public String getIcongeneral() {
		return icongeneral;
	}
	public void setIcongeneral(String icongeneral) {
		this.icongeneral = icongeneral;
	}

	/**
	 * constructor 
	 */
	public Poi() {
		super();
	}
	
	@Override
	public String toString() {
		return "Poi [id=" + id + ", title=" + title + ", link=" + link
				+ ", description=" + description + ", category=" + category
				+ ", lastUpdated=" + lastUpdated + ", icon=" + icon
				+ ", icongeneral=" + icongeneral 
				+ ", style=" + style
				+ ", coordenadas=" + coordenadas
				+ ", updateable=" + updateable + ", geometry=" + geometry + "]";
	}
	
	/**
	 * constructor 
	 */
	public Poi fromSolr(SolrDocument doc) {
		Poi est = new Poi();
		est.setId(doc.getFieldValue("id") == null ? null : doc.getFieldValue("id").toString());
		est.setLink(doc.getFieldValue("uri") == null ? null : doc.getFieldValue("uri").toString());
		est.setTitle(doc.getFieldValue("title") == null ? null : doc.getFieldValue("title").toString());
		est.setDescription(doc.getFieldValue("texto_t") == null ? null : doc.getFieldValue("texto_t").toString());
		if (doc.getFieldValue("category") != null) {
			String categoria = doc.getFieldValue("category").toString().replace("[", "").replace("]", "");
			est.setCategory(categoria);
			est.setIcon(Solr.obtenerIcono(categoria));
		}
		
		if (doc.getFieldValue("x_coordinate") != null) {
			Punto p = new Punto();
			p.setCoordinates(new Double[]{Double.parseDouble(doc.getFieldValue("x_coordinate").toString()),Double.parseDouble(doc.getFieldValue("y_coordinate").toString())});
			est.setGeometry(p);
		}
		try {
			if (doc.getFieldValue(Faceta.FACET_FECHA_MODIFICADO) != null) {
				est.setLastUpdated(ConvertDate.string2Date(doc.getFieldValue(Faceta.FACET_FECHA_MODIFICADO).toString(), ConvertDate.DATEQUERY, Locale.US));
			}
		} catch (ParseException e) {

		}
		return est;
	}
	public String getUri() {
		return Funciones.obtenerPath(this.getClass()) + getId();
	}
}
