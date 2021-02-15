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

package org.sede.servicio.mapacolaborativo.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.sede.core.rest.Mensaje;
import org.sede.core.rest.Peticion;
import org.sede.servicio.mapacolaborativo.entity.Category;
import org.sede.servicio.mapacolaborativo.entity.MapaColaborativo;
import org.springframework.http.ResponseEntity;

import com.googlecode.genericdao.search.SearchResult;

/**
 * Interfaz MapaColaborativoDAO
 *
 * @author Ayuntamiento Zaragoza
 *
 */
public interface MapaColaborativoDAO {
	/**
	 * Metodo listado
	 */
	public SearchResult<MapaColaborativo> listado(int start, int rows, String sort, String ids, String title, String type, String category, String accountId, Date startDate, Date endDate);
	/**
	 * Metodo listado de categorias
	 */
	public SearchResult<Category> listadoCategorias();
	/**
	 * Metodo detalle
	 */
	public Object detalle(BigDecimal id, String usuario, String icono, String srsname) throws SQLException;	
	/**
	 * Metodo busqueda
	 */
	public SearchResult<MapaColaborativo> searchAndCountMapaColaborativo(int rows, 
			int start, 
			String sort, 
			String ids, 
			String title,
			String type,
			String category,
			String accountId,
			Date startDate, 
			Date endDate
			) throws SQLException;
	/**
	 * Metodo parametros para BBDD
	 */
	public String nombresParaBBDD(String sort);
	/**
	 * Metodo crear
	 */
	public ResponseEntity<?> crear(MapaColaborativo req, String idUsuario, Peticion p);
	/**
	 * Metodo guardar
	 */
	public ResponseEntity<?> guardar(MapaColaborativo req, String idUsuario, Peticion p);
	/**
	 * Metodo eliminar
	 */
	public ResponseEntity<Mensaje> eliminar(String accountId, BigDecimal id, String ids) throws SQLException;
	/**
	 * Metodo lista de categorias
	 */
	public List<Category> getCategoriasEdit(String gczuser);
	/**
	 * Metodo busqueda
	 */
	public String find(String tipo, String identifier);
	/**
	 * Metodo busqueda de id
	 */
	public Integer findId(String tipo, String id);
	/**
	 * Metodo asociar tipo
	 */
	public void asociarTipo(BigDecimal id, String tipo, String associatedId);
}
