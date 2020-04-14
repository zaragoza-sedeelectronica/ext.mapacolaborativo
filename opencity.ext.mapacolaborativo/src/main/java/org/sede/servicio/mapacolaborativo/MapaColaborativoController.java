package org.sede.servicio.mapacolaborativo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.sede.core.anotaciones.Description;
import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.Interno;
import org.sede.core.anotaciones.NoCache;
import org.sede.core.anotaciones.OpenData;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.PermisosUser;
import org.sede.core.anotaciones.ResponseClass;
import org.sede.core.anotaciones.TestValue;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.MimeTypes;
import org.sede.core.rest.Role;
import org.sede.core.utils.Funciones;
import org.sede.servicio.ModelAttr;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.mapacolaborativo.dao.MapaColaborativoDAO;
import org.sede.servicio.mapacolaborativo.entity.MapaColaborativo;
import org.sede.servicio.mapacolaborativo.entity.Poi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.googlecode.genericdao.search.SearchResult;

/**
 * Controlador mapas colaborativos
 *
 * @author Ayuntamiento Zaragoza
 *
 */
@Gcz(servicio="MAPACOLABORATIVO",seccion="MAPA")
@Transactional(ConfigMapaColaborativo.TM)
@Controller
@RequestMapping(value = "/" + MapaColaborativoController.MAPPING, method = RequestMethod.GET)
@Description("Gobierno abierto: Mapas colaborativos")
// TODO añadir anotación cache en apidetalle(hacer que no haya caché al modificar)
public class MapaColaborativoController {
	/**
	 *  Variable servicio
	 */
	private static final String SERVICIO = "mapa-colaborativo";
	/**
	 *  Variable mapping del servicio
	 */
	public static final String MAPPING = "servicio/" + SERVICIO;
	/**
	 *  Variable mapping del formulario
	 */
	private static final String MAPPING_FORM = MAPPING + "/formulario";
/**
 * Variable para obtener los mensajes del fichero de propiedades
 */
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Intial
	 */
	@Autowired
	public MapaColaborativoDAO dao;
	
	/**
	 * Metodo redirect
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String redirect() {
		return "redirect:" + SERVICIO + "/";
	}
	
	/**
	 * Metodo api listar
	 */
	@OpenData
	@NoCache
	@Description("Listado de mapas")
	@ResponseClass(value = MapaColaborativo.class, entity = SearchResult.class)
	@RequestMapping(method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiListar(
			@RequestParam(name = CheckeoParametros.PARAMSTART, defaultValue = CheckeoParametros.START) Integer start,
			@RequestParam(name = CheckeoParametros.PARAMROWS, defaultValue = CheckeoParametros.ROWS) Integer rows,
			@RequestParam(name = CheckeoParametros.PARAMSORT, defaultValue = "lastUpdated desc", required = false) String sort,
			@RequestParam(name = "ids", required = false) String ids,
			@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "start_date", required = false) Date startDate,
			@RequestParam(name = "end_date", required = false) Date endDate
			
    		) {
		
		return ResponseEntity.ok(dao.listado(start, rows, sort, ids, title, type, category, null, startDate, endDate));
    }
	
	/**
	 * Metodo api listar categorias
	 */
	@OpenData
	@NoCache
	@Description("Listado de categorías")
	@ResponseClass(value = MapaColaborativo.class, entity = SearchResult.class)
	@RequestMapping(value = "/category", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiListarCategory() {
		
		return ResponseEntity.ok(dao.listadoCategorias());
    }
	
	/**
	 * Metodo listar categorias
	 */
	@RequestMapping(value = "/category", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String listarCategory(Model model, HttpServletRequest request) {
		model.addAttribute(ModelAttr.RESULTADO, apiListarCategory());
		return MAPPING + "/category";
	}
	
	/**
	 * Metodo api listar mapas usuario 
	 */
	@Permisos(Permisos.DET)
	@NoCache
	@ResponseClass(value = MapaColaborativo.class, entity = SearchResult.class)
	@RequestMapping(value ="/user/{accountId}", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiUserMaps(
			@PathVariable String accountId,
			@RequestParam(name = CheckeoParametros.PARAMSTART, defaultValue = CheckeoParametros.START) Integer start,
			@RequestParam(name = CheckeoParametros.PARAMROWS, defaultValue = CheckeoParametros.ROWS) Integer rows,
			@RequestParam(name = CheckeoParametros.PARAMSORT, required = false) String sort,
			@RequestParam(name = "ids", required = false) String ids,
			@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "start_date", required = false) Date startDate,
			@RequestParam(name = "end_date", required = false) Date endDate
    		) {
		
		return ResponseEntity.ok(dao.listado(start, rows, sort, ids, title, type, category, accountId, startDate, endDate));
    }
	
	/**
	 * Metodo api detalle mapa de usuario
	 */
	@RequestMapping(value = "/user/{accountId}/{id}", method = RequestMethod.GET, produces = {MimeTypes.GEOJSON, MimeTypes.GEORSS, MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	@Permisos(Permisos.DET)
	@NoCache
	@ResponseClass(value = MapaColaborativo.class)
	public @ResponseBody ResponseEntity<?> apiDetalleUserMap(
			@PathVariable String accountId,
			@PathVariable BigDecimal id, HttpServletRequest request) throws SQLException {
		if (accountId == null) {
			accountId = Funciones.getAccountIdUserGestor(request);
		}
		String icono = null; 
		if (Funciones.getPeticion().getQueryParams().get("icon") != null) {
			icono = Funciones.getPeticion().getQueryParams().get("icon")[0];
		}
		return ResponseEntity.ok(dao.detalle(id, accountId, icono, Funciones.getPeticion().getSrsName()));
	}
	
	/**
	 * Metodo api detalle del tipo
	 */
	@OpenData
//	@Cache(Cache.DURACION_30MIN)
	@NoCache
	@Description("Detalle de un mapa")
	@ResponseClass(MapaColaborativo.class)
	@RequestMapping(value = "/{id}/{tipo}", method = RequestMethod.GET, produces = {MimeTypes.GEOJSON, MimeTypes.GEORSS, MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiDetalleTipo(@PathVariable String id, @PathVariable String tipo, HttpServletRequest request) {		
		Integer idAgrupacion = dao.findId(tipo, id);
		if (idAgrupacion == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale())));
		} else {
			return apiDetalle(idAgrupacion, request);
		}
	}
	
	/**
	 * Metodo api modificar tipo 
	 */
	@RequestMapping(value = "/{id}/{tipo}", method = RequestMethod.PUT, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
	@OpenData
	@Interno
	@PermisosUser
	@Description("Modificar registro")
	@ResponseClass(value = MapaColaborativo.class)
	public @ResponseBody ResponseEntity<?> apiModificarTipo(
			@PathVariable String id,
			@PathVariable String tipo,
			@RequestBody MapaColaborativo registro, HttpServletRequest request) {
		Credenciales credenciales = (Credenciales) request.getSession().getAttribute(CheckeoParametros.SESSIONGCZ);
		if (credenciales != null) {
			Integer idMapa = dao.findId(tipo, id);
			if (idMapa == null) {
				registro.setTitle(tipo + "-" + id);
				registro.setType("internal");
				ResponseEntity<?> respuesta = dao.crear(registro, Funciones.getAccountIdUserGestor(request), Funciones.getPeticion());
				if (respuesta.getStatusCode().is2xxSuccessful()) {
					dao.asociarTipo(((MapaColaborativo)respuesta.getBody()).getId(), tipo, id);
				}
				return respuesta;
			} else {
				registro.setId(new BigDecimal(idMapa));
				return dao.guardar(registro, Funciones.getAccountIdUserGestor(request), Funciones.getPeticion());
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Operación no permitida"));
		}
		
		
	}
	
	/**
	 * Metodo api detalle
	 */
	@OpenData
//	@Cache(Cache.DURACION_30MIN)
	@NoCache
	@Description("Detalle de un mapa")
	@ResponseClass(MapaColaborativo.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MimeTypes.GEOJSON, MimeTypes.GEORSS, MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiDetalle(@PathVariable @TestValue("134") Integer id, HttpServletRequest request) {
		try {
			String icono = null;
			if (Funciones.getPeticion().getQueryParams().get(CheckeoParametros.GEORSS_ICON) != null) {
				icono = Funciones.getPeticion().getQueryParams().get(CheckeoParametros.GEORSS_ICON)[0];
			}
			Object retorno = dao.detalle(new BigDecimal(id), Funciones.getAccountIdUserGestor(request), icono, Funciones.getPeticion().getSrsName());
			if (retorno instanceof SearchResult) {
				@SuppressWarnings("unchecked")
				SearchResult<Poi> mapa = (SearchResult<Poi>) retorno;
				if (Funciones.getPeticion().getTipoAcceso().equals(Role.PUBLICO) 
						&& "private".equals(mapa.getPropiedades().get("type"))) {
					if (mapa.getPropiedades().containsKey("owned") && mapa.getPropiedades().get("owned").equals(Boolean.TRUE)) {
						return ResponseEntity.ok(mapa);	
					} else {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale())));
					}
				} else {
					return ResponseEntity.ok(mapa);
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale())));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(HttpStatus.BAD_REQUEST.value(), "Error: " + e.getMessage()));
		}
	}
	
	/**
	 * Metodo api crear 
	 */
	@RequestMapping(value = "/user/{accountId}", method = RequestMethod.POST, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
	@Permisos(Permisos.NEW)
	@Description("Crear registro")
	@ResponseClass(value = MapaColaborativo.class)
	@Transactional(ConfigMapaColaborativo.TM)
	public @ResponseBody ResponseEntity<?> apiCrear(
			@PathVariable String accountId,
			@RequestBody MapaColaborativo registro) {
		return dao.crear(registro, accountId, Funciones.getPeticion());
	}
	
	/**
	 * Metodo api modificar 
	 */
	@RequestMapping(value = "/user/{account_id}/{id}", method = RequestMethod.PUT, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
	@Permisos(Permisos.MOD)
	@Description("Modificar registro")
	@ResponseClass(value = MapaColaborativo.class)
	public @ResponseBody ResponseEntity<?> apiModificar(
			@PathVariable(value = "account_id") String accountId,
			@PathVariable BigDecimal id,
			@RequestBody MapaColaborativo registro) {
		registro.setId(id);
		return dao.guardar(registro, accountId, Funciones.getPeticion());
	}

	/**
	 * Metodo api borrar 
	 */
	@RequestMapping(value = "/user/{account_id}/{id}/remove", method = RequestMethod.DELETE,
			produces = {MimeTypes.JSON, MimeTypes.XML })
	@Permisos(Permisos.DEL)
	@Description("Eliminar registro")
	@ResponseClass(value = Mensaje.class)
	public @ResponseBody ResponseEntity<?> apiDelete(
			@PathVariable(value = "account_id") String accountId,
			@PathVariable BigDecimal id,
			@RequestParam(name = "ids", required = false) String ids
			) throws SQLException {
		return dao.eliminar(accountId, id, ids); 

	}

	/**
	 * Metodo buscador 
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String buscador(Model model, @RequestParam(name = CheckeoParametros.PARAMSTART, defaultValue = CheckeoParametros.START) Integer start,
			@RequestParam(name = CheckeoParametros.PARAMROWS, defaultValue = CheckeoParametros.ROWS) Integer rows,
			@RequestParam(name = CheckeoParametros.PARAMSORT, defaultValue = "lastUpdated desc", required = false) String sort,
			@RequestParam(name = "ids", required = false) String ids,
			@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "start_date", required = false) Date startDate,
			@RequestParam(name = "end_date", required = false) Date endDate) {
	
		model.addAttribute("category", apiListarCategory());
		
		model.addAttribute(ModelAttr.RESULTADO, apiListar(start, rows, sort, ids, title, type, category, startDate, endDate));
		return MAPPING + "/index";
	}
	
	/**
	 * Metodo detalle
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String detalle(@PathVariable Integer id, Model model,
			HttpServletRequest request) {
		model.addAttribute(ModelAttr.REGISTRO, apiDetalle(id, request));
		return MAPPING + "/detalle";
	}
	
	/**
	 * Metodo mapas de usuario 
	 */
	@PermisosUser
	@RequestMapping(value = "/user", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String userMaps(Model model, HttpServletRequest request, @RequestParam(name = CheckeoParametros.PARAMSTART, defaultValue = CheckeoParametros.START) Integer start,
			@RequestParam(name = CheckeoParametros.PARAMROWS, defaultValue = CheckeoParametros.ROWS) Integer rows,
			@RequestParam(name = CheckeoParametros.PARAMSORT, required = false) String sort,
			@RequestParam(name = "ids", required = false) String ids,
			@RequestParam(name = "title", required = false) String title,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "category", required = false) String category,
			@RequestParam(name = "start_date", required = false) Date startDate,
			@RequestParam(name = "end_date", required = false) Date endDate) {
		model.addAttribute(ModelAttr.RESULTADO, apiUserMaps(Funciones.getUser(request).getAccount_id(), start, rows, sort, ids, title, type, category, startDate, endDate));
		return MAPPING + "/user";
	}
	
	/**
	 * Metodo nuevo formulario
	 */
	@PermisosUser
	@RequestMapping(value = "/new", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String newForm(MapaColaborativo dato, BindingResult bindingResult,
			Model model, HttpServletRequest request) {
		SearchResult<Poi> dat = new SearchResult<Poi>();
		dat.setPropiedades(new HashMap<String, Object>());
		dat.getPropiedades().put("id", null);
		dat.getPropiedades().put("title", null);
		dat.getPropiedades().put("type", null);
		dat.getPropiedades().put("owned", true);
		model.addAttribute("categoria", dao.getCategoriasEdit(Funciones.getUser(request).getEmail()));
		model.addAttribute(ModelAttr.DATO, dat);
		model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(dat));
		return MAPPING_FORM;
	}

	/**
	 * Metodo edición
	 */
	@PermisosUser
	@NoCache
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String edit(@PathVariable BigDecimal id, MapaColaborativo dato,
			BindingResult bindingResult, Model model, HttpServletRequest request) throws SQLException {
		ResponseEntity<?> registro = apiDetalleUserMap(Funciones.getUser(request).getAccount_id(), id, request);
		model.addAttribute("categoria", dao.getCategoriasEdit(Funciones.getUser(request).getEmail()));
		model.addAttribute(ModelAttr.DATO, registro.getBody());
		model.addAttribute(ModelAttr.REGISTRO, registro);
		return MAPPING_FORM;
	}

	/**
	 * Metodo api crear mapa publico 
	 */
	@RequestMapping(value = "/save", method = RequestMethod.PUT, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
	@OpenData
	@Interno
	@PermisosUser
	@Description("Crear registro")
	@ResponseClass(value = MapaColaborativo.class)
	@Transactional(ConfigMapaColaborativo.TM)
	public @ResponseBody ResponseEntity<?> apiCrearPublic(
			@RequestBody MapaColaborativo registro, HttpServletRequest request) {
		return dao.crear(registro, Funciones.getAccountIdUserGestor(request), Funciones.getPeticion());
	}
	
	/**
	 * Metodo api modificar mapa publico
	 */
	@RequestMapping(value = "/{id}/save", method = RequestMethod.PUT, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
	@OpenData
	@Interno
	@PermisosUser
	@Description("Modificar registro")
	@ResponseClass(value = MapaColaborativo.class)
	public @ResponseBody ResponseEntity<?> apiModificarPublic(
			@PathVariable BigDecimal id,
			@RequestBody MapaColaborativo registro, HttpServletRequest request) {
		registro.setId(id);
		return dao.guardar(registro, Funciones.getAccountIdUserGestor(request), Funciones.getPeticion());
	}
	
	/**
	 * Metodo eliminar 
	 */
	@PermisosUser
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	@Transactional(ConfigMapaColaborativo.TM)
	public String eliminar(
			@PathVariable BigDecimal id,
			@RequestParam(name = "ids", required = false) String ids, 
			Model model, RedirectAttributes attr, HttpServletRequest request) throws SQLException {
		ResponseEntity<?> resultado = apiDelete(Funciones.getUser(request).getAccount_id(), id, ids);
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro eliminado correctamente"));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, resultado.getBody());
		}		
		return "redirect:/" + MAPPING + "/user";
	}
	
	
}