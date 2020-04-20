package org.sede.servicio.mapacolaborativo.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.sede.core.dao.Solr;
import org.sede.core.exception.FormatoNoSoportadoException;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Formato;
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.MimeTypes;
import org.sede.core.rest.Peticion;
import org.sede.core.rest.view.TransformadorXml;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.Propiedades;
import org.sede.servicio.mapacolaborativo.ConfigMapaColaborativo;
import org.sede.servicio.mapacolaborativo.entity.Category;
import org.sede.servicio.mapacolaborativo.entity.MapaColaborativo;
import org.sede.servicio.mapacolaborativo.entity.Poi;
import org.sede.servicio.mapacolaborativo.entity.UtilsMapaColaborativo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.SearchResult;

/**
 * Clase que implementa la interfaz MapaColaborativoDAO
 *
 * @author Ayuntamiento Zaragoza
 *
 */
@Repository
@Transactional(ConfigMapaColaborativo.TM)
public class MapaColaborativoGenericDAOImpl implements MapaColaborativoDAO {
	/**
	 *  variable logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(MapaColaborativoGenericDAOImpl.class);
	
	/**
	 *  Metodo setEntityManager
	 */
	private EntityManager entityManager;

	@Autowired
	private MessageSource messageSource;
	
	/**
	 *  Metodo setEntityManager
	 */
	@PersistenceContext(unitName=ConfigMapaColaborativo.ESQUEMA)
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	/**
	 *  Metodo get entity manager
	 */
	private EntityManager em() {
		return entityManager;
	}
	/**
	 *  variable mapeos
	 */
	private static HashMap<String, String> mapeos = new HashMap<String, String>();
	static {
		mapeos.put("id", "id_aportacion");
		mapeos.put("title", "nombre");		
	}

	/**
	 * Metodo listado 
	 */
	public SearchResult<MapaColaborativo> listado(int start, int rows, String sort, String ids, String title, String type, String category, String accountId, Date startDate, Date endDate) {
		 try {
				sort = this.nombresParaBBDD(sort);
				// Rango
				if (startDate != null || endDate != null) {
					startDate = UtilsMapaColaborativo.ajustarFechaInicio(startDate);
					endDate = UtilsMapaColaborativo.ajustarFechaFin(startDate, endDate);
				}
				// Como mucho 1000 resultados
				rows = rows > 1000 ? 1000 : rows;
				return this.searchAndCountMapaColaborativo(rows,
						start, sort, ids, title, type, category,
						accountId  == null ? null : accountId,
						startDate, endDate);
			} catch (Exception e) {
				return new SearchResult<MapaColaborativo>();
			} 
	 } 
	
	/**
	 * Metodo detalle 
	 */
	public Object detalle(final BigDecimal id, final String usuario, final String icono, final String srsname) throws SQLException {
		return em().unwrap(Session.class).doReturningWork(new ReturningWork<Object>() {					
			public Object execute(Connection connection) throws SQLException {
				CallableStatement st = null;
				try {
					st = connection.prepareCall(crearStatement("MAPA", "DETALLE", 4));
					if (id == null) {
						st.setNull(1, OracleTypes.NUMBER);
					} else {
						st.setBigDecimal(1, id);	
					}
					if (usuario == null) {
						st.setNull(2, OracleTypes.VARCHAR);
					} else {
						st.setString(2, usuario);	
					}
					if (icono == null) {
						st.setNull(3, OracleTypes.VARCHAR);
					} else {
						st.setString(3, icono);	
					}
					st.registerOutParameter(4, OracleTypes.CLOB);
		            st.executeQuery();
		            TransformadorXml trans = new TransformadorXml();
					MapaColaborativo results =  completarDatosConSolr((MapaColaborativo)trans.pasarAObjeto(st.getString(4), false, MapaColaborativo.class, Poi.class));
					SearchResult<Poi> resultado = new SearchResult<Poi>();
					resultado.setResult(results.getPois() == null ? new ArrayList<Poi>(): results.getPois());
					resultado.setRows(results.getPois() == null ? 0 : results.getPois().size());
					resultado.setTotalCount(results.getPois() == null ? 0 : results.getPois().size());
					
					
					HashMap<String, Object> propiedades = new HashMap<String, Object>();
					propiedades.put("id", results.getId());
					if (usuario != null) {
						propiedades.put("owned", results.getOwned());
					}
					propiedades.put("title", results.getTitle());
					propiedades.put("type", results.getType());
					propiedades.put("icon", StringUtils.isEmpty(results.getIcon()) ? "generico" : results.getIcon());
					propiedades.put("lastUpdated", results.getLastUpdated());
					
					if (results.getCategories() != null && !results.getCategories().isEmpty()) {
						StringBuilder category = new StringBuilder();
						boolean primero = true;
						for (Category c : results.getCategories()) {
							if (!primero) {
								category.append(",");	
							}
							primero = false;
							category.append(c.getId());
						}
						propiedades.put("categories", category.toString());
					}
					
					resultado.setPropiedades(propiedades);
					
					return resultado;
				} catch (SQLException e) {
					if (e.getMessage().indexOf("ORA-01403") >= 0) {
						return new Mensaje(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale()));
					}
					return new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage());
				} catch (FormatoNoSoportadoException e) {
					return new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage()); 
				} finally {
					if (st != null) {
						st.close();
					}
				}
			}
		});
	}
	
	/**
	 * Metodo resultado de busqueda
	 */
	public SearchResult<MapaColaborativo> searchAndCountMapaColaborativo(final int rows, 
			final int start, 
			final String sort, 
			final String ids, 
			final String title,
			final String type,
			final String category,
			final String account_id,
			final Date startDate, 
			final Date endDate
			) throws SQLException {
		
		return em().unwrap(Session.class).doReturningWork(new ReturningWork<SearchResult<MapaColaborativo>>() {					
			public SearchResult<MapaColaborativo> execute(Connection connection) throws SQLException {
				CallableStatement st = null;
				try {
					st = connection.prepareCall(crearStatement("MAPA", "GET", 12));
					st.setInt(1, rows);
					st.setInt(2, start);
					if (sort == null) {
						st.setNull(3, OracleTypes.VARCHAR);
					} else {
						st.setString(3, sort);	
					}
					
					if (ids == null) {
						st.setNull(4, OracleTypes.VARCHAR);
					} else {
						st.setString(4, ids);	
					}
					if (title == null) {
						st.setNull(5, OracleTypes.VARCHAR);
					} else {
						st.setString(5, title);	
					}
					if (type == null) {
						st.setNull(6, OracleTypes.VARCHAR);
					} else {
						st.setString(6, type);	
					}
					if (category == null) {
						st.setNull(7, OracleTypes.VARCHAR);
					} else {
						st.setString(7, category);	
					}
					if (account_id == null) {
						st.setNull(8, OracleTypes.NUMBER);
					} else {
						st.setString(8, account_id);	
					}
					if (startDate == null) {
						st.setNull(9, OracleTypes.DATE);
					} else {
						st.setDate(9, new java.sql.Date(startDate.getTime()));
					}
					
					if (endDate == null) {
						st.setNull(10, OracleTypes.DATE);
					} else {
						st.setDate(10, new java.sql.Date(endDate.getTime()));
					}
		
					st.registerOutParameter(11, OracleTypes.CLOB);
					st.registerOutParameter(12, OracleTypes.NUMBER);
		            st.executeQuery();
		            TransformadorXml trans = new TransformadorXml();
		
		            @SuppressWarnings("unchecked")
					SearchResult<MapaColaborativo> results =  (SearchResult<MapaColaborativo>)trans.pasarAObjeto(st.getString(11), true, SearchResult.class, MapaColaborativo.class);
					
					if (results.getResult() == null) {
						results.setResult(new ArrayList<MapaColaborativo>());
						results.setRows(0);
						results.setTotalCount(0);
					} else {
						results.setRows(rows);
						results.setStart(start);
						results.setTotalCount(st.getInt(12));
					}
					return results;
				} catch (SQLException e) {
					return null;
				} catch (FormatoNoSoportadoException e) {
					return null; 
				} finally {
					if (st != null) {
						st.close();
					}
				}
			}
		});
	}

	/**
	 * Metodo nombres para BBDD 
	 */
	public String nombresParaBBDD(String sort) {
		StringBuilder retorno = new StringBuilder();
		if (sort != null && sort.length() > 0) {
			
			String[] palabras = sort.split(","); 
			for (int i = 0; i < palabras.length; i++) {
				if (i > 0){
					retorno.append(",");
				}
				
				String atributo = palabras[i].trim().substring(0, palabras[i].trim().indexOf(' '));
				String valor = palabras[i].trim().substring(palabras[i].trim().indexOf(' ') + 1, palabras[i].trim().length());
				if (mapeos.containsKey(atributo.trim())) {
					retorno.append(mapeos.get(atributo.trim()));
				} else {
					retorno.append(atributo.trim());
				}
				if (valor.trim().length() > 1) {
					retorno.append(" " + valor.trim());
				} else {
					retorno.append(" asc");
				}
			}
			return retorno.toString();	
		}
		return null;
	}
	
	/**
	 * Metodo crear consulta 
	 */
	protected String crearStatement(String modulo,String proc,int numParametros) {
		StringBuilder sb = new StringBuilder("{call ");
		sb.append("PCK_COLABORATIVO_" + modulo);
		sb.append(".");
		sb.append(proc);
		sb.append("(");
		for (int i = 1; i <= numParametros; i++) {
			if (i > 1)
				sb.append(",");
			sb.append("?");
		}
		return sb.append(")}").toString();
		
	   }

	/**
	 * Metodo crear 
	 */
	public ResponseEntity<?> crear(final MapaColaborativo req, final String idUsuario, final Peticion p) {
		return em().unwrap(Session.class).doReturningWork(new ReturningWork<ResponseEntity<?>>() {					
			public ResponseEntity<?> execute(Connection connection) throws SQLException {
				CallableStatement st = null;
				try {
					TransformadorXml trans = new TransformadorXml();
					StringBuilder respuesta = new StringBuilder();
					tratarCoordenadas(req, p.getSrsName());
					Formato formatoOriginal = p.getFormato();
					String srsOriginal = p.getSrsName();
					
					p.setFormato(MimeTypes.XML_FORMATO);
					p.setSrsName(CheckeoParametros.SRSUTM30N);
					
					trans.transformarObjeto(respuesta, req, p, true, null);
				
					int i = 0;
					st = connection.prepareCall(crearStatement("MAPA", "CREAR", 3));
					st.setString(++i, idUsuario);
					st.setString(++i, respuesta.toString());
					
					st.registerOutParameter(++i, OracleTypes.CLOB);
		            st.executeQuery();
		            p.setFormato(formatoOriginal);
		            p.setSrsName(srsOriginal);
		            
		            MapaColaborativo results =  (MapaColaborativo)trans.pasarAObjeto(st.getString(i), true, SearchResult.class, MapaColaborativo.class);
					return ResponseEntity.ok(completarDatosConSolr(results));
				} catch (Exception e) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(HttpStatus.BAD_REQUEST.value(), "ERROR: " + e.getMessage()));
				} finally {
					if (st != null) {
						try {
							st.close();
						} catch (SQLException e) {
							logger.error(Funciones.getStackTrace(e));
						}
					}
				}
			}
		});
	}
	
	/**
	 * Metodo guardar 
	 */
	public ResponseEntity<?> guardar(final MapaColaborativo req, final String idUsuario, final Peticion p) {
		return em().unwrap(Session.class).doReturningWork(new ReturningWork<ResponseEntity<?>>() {					
			public ResponseEntity<?> execute(Connection connection) throws SQLException {
				CallableStatement st = null;
				try {
					TransformadorXml trans = new TransformadorXml();
					StringBuilder respuesta = new StringBuilder();
					tratarCoordenadas(req, p.getSrsName());
					Formato formatoOriginal = p.getFormato();
					String srsOriginal = p.getSrsName();
					
					p.setFormato(MimeTypes.XML_FORMATO);
					p.setSrsName(CheckeoParametros.SRSUTM30N);
					trans.transformarObjeto(respuesta, req, p, true, null);
					
					int i = 0;
					st = connection.prepareCall(crearStatement("MAPA", "GUARDAR", 3));
					st.setString(++i, idUsuario);
					st.setString(++i, respuesta.toString());
					st.registerOutParameter(++i, OracleTypes.CLOB);
		            st.executeQuery();
		            p.setFormato(formatoOriginal);
		            p.setSrsName(srsOriginal);
		            
		            MapaColaborativo results =  (MapaColaborativo)trans.pasarAObjeto(st.getString(i), true, SearchResult.class, MapaColaborativo.class);
					return ResponseEntity.ok(completarDatosConSolr(results));
				} catch (Exception e) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(HttpStatus.BAD_REQUEST.value(), "ERROR: " + e.getMessage()));
				} finally {
					if (st != null) {
						try {
							st.close();
						} catch (SQLException e) {
							;
						}
					}
				}
			}
		});
	}
	
	/**
	 * Metodo completar datos solr 
	 */
	private MapaColaborativo completarDatosConSolr(MapaColaborativo results) {
		if (results == null) {
			return null;
		} else {
			StringBuilder ids = new StringBuilder();
			boolean primero = true;
			if (results.getPois() != null) {
				for (int i = 0; i < results.getPois().size(); i++) {
					Poi req = results.getPois().get(i);
					if (req.getId().indexOf('-') >= 0) {
						if (!primero) {
							ids.append(" OR ");
						}
						ids.append("id:" + req.getId());
						primero = false;
					}
				}
			}
			if (ids.length() > 0) {
				HashMap<String,String> fl = new HashMap<String, String>();
				fl.put("id", "id");
				fl.put("title", "title");
				fl.put("geometry", "geometry");
				@SuppressWarnings("unchecked")
				SearchResult<Poi> res = (SearchResult<Poi>) Solr.getInstance().query(ids.toString(), null, null, null, 1000, 0, fl, null, null, Poi.class, true);
				
				for (int i = 0; i < results.getPois().size(); i++) {
					Poi req = results.getPois().get(i);
					if (req.getId().indexOf('-') >= 0 && res != null) {
						for (int j = 0; j < res.getResult().size(); j++) {
							Poi deSolr = res.getResult().get(j);
							if (req.getId().equals(deSolr.getId())) {
								req.setTitle(deSolr.getTitle());
								req.setGeometry(deSolr.getGeometry());
								results.getPois().remove(i);
								results.getPois().add(i,req);
							}
						}
					}
				}
			}
			return results;
		}
	}

	/**
	 * Metodo tratar coordenadas 
	 */
	private void tratarCoordenadas(MapaColaborativo results, String srsname) {
		if (results.getPois() != null) {
			for (int i = 0; i < results.getPois().size(); i++) {
				Poi req = results.getPois().get(i);
				if (req.getGeometry() != null && CheckeoParametros.SRSWGS84.equals(srsname)) {
					/*Double[] geo = Geometria.pasarAUTM30(req.getGeometry().getCoordinates()[0], req.getGeometry().getCoordinates()[1]);
					req.getGeometry().setCoordinates(new Double[]{geo[1], geo[0]});
					results.getPois().remove(i);
					results.getPois().add(i,req);*/
				} else if (req.getGeometry() != null && CheckeoParametros.SRSETRS89.equals(srsname)) {
					/*Double[] geo = Geometria.pasarETRS89AUTM30(req.getGeometry().getCoordinates()[0], req.getGeometry().getCoordinates()[1]);
					req.getGeometry().setCoordinates(new Double[]{geo[1], geo[0]});
					results.getPois().remove(i);
					results.getPois().add(i,req);*/
				} else if (req.getGeometry() != null) {
					/*req.getGeometry().setCoordinates(new Double[]{req.getGeometry().getCoordinates()[1] , req.getGeometry().getCoordinates()[0]});
					results.getPois().remove(i);
					results.getPois().add(i,req);*/
				}
			}
		}
	}

	/**
	 * Metodo eliminar 
	 */
	public ResponseEntity<Mensaje> eliminar(final String account_id, final BigDecimal id, final String ids) throws SQLException {
		return em().unwrap(Session.class).doReturningWork(new ReturningWork<ResponseEntity<Mensaje>>() {					
			public ResponseEntity<Mensaje> execute(Connection connection) throws SQLException {
				CallableStatement st = null;
				try {
					st = connection.prepareCall(crearStatement("MAPA", "ELIMINAR", 4));
					st.setString(1, account_id);
					st.setInt(2, id.intValue());
		
					if (ids == null) {
						st.setNull(3, OracleTypes.VARCHAR);
					} else {
						st.setString(3, ids);	
					}
					st.registerOutParameter(4, OracleTypes.NUMBER);
		            st.executeQuery();
					if (st.getInt(4) > 0) {
						return ResponseEntity.status(HttpStatus.OK).body(new Mensaje(HttpStatus.OK.value(), "Borrado realizado correctamente"));
					} else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha eliminado ningún registro"));
					}
				} catch (SQLException e) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(HttpStatus.BAD_REQUEST.value(), "ERROR: " + e.getMessage()));
				} finally {
					if (st != null) {
						st.close();
					}
				}
			}
		});
	}
	
	/**
	 * Metodo listado de categorias 
	 */
	@Override
	public SearchResult<Category> listadoCategorias() {
		return em().unwrap(Session.class).doReturningWork(new ReturningWork<SearchResult<Category>>() {					
			public SearchResult<Category> execute(Connection connection) throws SQLException {
				CallableStatement st = null;
				try {
					st = connection.prepareCall(crearStatement("MAPA", "CATEGORIAS", 2));
					
					st.registerOutParameter(1, OracleTypes.CLOB);
					st.registerOutParameter(2, OracleTypes.NUMBER);
		            st.executeQuery();
		            TransformadorXml trans = new TransformadorXml();
		
		            @SuppressWarnings("unchecked")
					SearchResult<Category> results =  (SearchResult<Category>)trans.pasarAObjeto(st.getString(1), true, SearchResult.class, Category.class);
					
					if (results.getResult() == null) {
						results.setResult(new ArrayList<Category>());
						results.setRows(0);
						results.setTotalCount(0);
					} else {
						results.setRows(results.getResult().size());
						results.setTotalCount(st.getInt(2));
					}
					return results;
				} catch (SQLException e) {
					return null;
				} catch (FormatoNoSoportadoException e) {
					return null; 
				} finally {
					if (st != null) {
						st.close();
					}
				}
			}
		});
	}
	
	/**
	 * Metodo lista de categorias 
	 */
	public List<Category> getCategoriasEdit(String gczuser) {
		@SuppressWarnings("unchecked")
		List<Object> lista = em().createNativeQuery("select id,valor from POI_CATEGORIA where gcz_usuario is null or gcz_usuario=? order by valor asc").setParameter(1, gczuser).getResultList();
		List<Category> categorias = new ArrayList<Category>();
		for (Iterator<Object> iterador = lista.iterator(); iterador.hasNext();) {
			Object[] row = (Object[])iterador.next();
			Category c = new Category();
			c.setId(new BigDecimal(row[0].toString()));
			c.setTitle(row[1].toString());
			categorias.add(c);
		}
		return categorias;
	}
	
	/**
	 * Metodo busqueda
	 */
	@Override
	public String find(String tipo, String identifier) {
		try {
			BigDecimal id = null;
			Query q = em().createNativeQuery("select id_agrupacion "
					+ "from POI_AGRUPACION_ASSOCIATED_TYPE " 
					+ "where  tipo = ? and ASSOCIATED_ID = ?");
			id = (BigDecimal)q.setParameter(1, tipo).setParameter(2, identifier).getSingleResult();
			if (id == null) {
				return null;
			} else {
				return Propiedades.getContexto() + "/servicio/mapa-colaborativo/" + identifier + "/" + tipo;
			}
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Metodo busqueda de id
	 */
	@Override
	public Integer findId(String tipo, String identifier) {
		try {
			BigDecimal id = null;
			Query q = em().createNativeQuery("select id_agrupacion "
					+ "from POI_AGRUPACION_ASSOCIATED_TYPE " 
					+ "where  tipo = ? and ASSOCIATED_ID = ?");
			id = (BigDecimal)q.setParameter(1, tipo).setParameter(2, identifier).getSingleResult();
			if (id == null) {
				return null;
			} else {
				return id.intValue();
			}
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Metodo asociar tipo
	 */
	@Override
	public void asociarTipo(BigDecimal id, String tipo, String associatedId) {
		Query update = em().createNativeQuery(
				"insert into POI_AGRUPACION_ASSOCIATED_TYPE(ID_AGRUPACION,TIPO,ASSOCIATED_ID) values (?,?,?)");
		int x = 0;
		update.setParameter(++x, id);
		update.setParameter(++x, tipo);
		update.setParameter(++x, associatedId);
		update.executeUpdate();		
	}
}