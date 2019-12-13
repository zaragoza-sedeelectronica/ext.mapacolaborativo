package org.sede.servicio.mapacolaborativo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.sede.core.PropiedadesTest;
import org.sede.core.TestPeticion;
import org.sede.core.db.DataSourceRule;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.MimeTypes;
import org.sede.core.utils.Funciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test de pruebas unitarias para Mapas Colaborativos
 *
 * @author Ayuntamiento Zaragoza
 *
 */
@RunWith(DataSourceRule.class)
@ContextConfiguration(classes = org.sede.core.WebConfig.class)
@WebAppConfiguration
public class MapaColaborativoApiTest {
	
	/**
	 *  Web application context
	 */
	@Autowired
	private WebApplicationContext context;
	
	/**
	 *  controlador de mapas colaborativos
	 */
	@InjectMocks
	private MapaColaborativoController controller;
	private static String MAPPING = MapaColaborativoController.MAPPING;
	/**
	 *  peticion de prueba
	 */
	private TestPeticion peticion = new TestPeticion();
	
	/**
	 *  mock de model vista controlador
	 */
	private MockMvc mockMvc;

	/**
	 * Metodo setup ejecutado antes de las pruebas
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		peticion.setMock(mockMvc);
	}
	
	/**
	 * Metodo listado mapas 
	 */
	@Test
	public void testListApi() throws Exception {
		MvcResult result = peticion.get("/" + MAPPING)
				.andExpect(status().isOk())
				.andReturn()
				;
		System.out.println(result.getResponse().getContentAsString());
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	
	/**
	 * Metodo listado categorias 
	 */
	@Test
	public void testListApiCategory() throws Exception {
		MvcResult result = peticion.get("/" + MAPPING + "/category")
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	
	/**
	 * Metodo listado tipos 
	 */
	@Test
	public void testListApiType() throws Exception {
		MvcResult result = peticion.get("/" + MAPPING + "?type=collaborative")
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	
	/**
	 * Metodo listado mapas de un usuario 
	 */
	@Test
	public void testListUserMaps() throws Exception {
		peticion.setCredenciales();
		MvcResult result = peticion.get("/" + MAPPING + "/user/" + PropiedadesTest.getUserId())
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	
	/**
	 * Metodo detalle de un mapa 
	 */
	@Test
	public void testApiDetalle() throws Exception {
		MvcResult result = peticion.get("/" + MAPPING + "/41")
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	
	/**
	 * Metodo detalle de un mapa en geojson 
	 */
	@Test
	public void testApiDetalleGeoJson() throws Exception {
		MvcResult result = peticion.get("/" + MAPPING + "/41", MimeTypes.GEOJSON_MEDIA)
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	@Test
	public void testApiListJsonLd() throws Exception {
		MvcResult result = peticion.get("/" + MAPPING + "?category=3", MimeTypes.JSONLD_MEDIA)
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	/**
	 * Metodo detalle del icono de geojson de un mapa 
	 */
	@Test
	public void testApiDetalleGeoJsonIcon() throws Exception {
		MvcResult result = peticion.get("/" + MAPPING + "/41?" + CheckeoParametros.GEORSS_ICON + "=noche", MimeTypes.GEOJSON_MEDIA)
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	
	
	/**
	 * Metodo detalle de un mapa de un usuario
	 */
	@Test
	public void testApiDetalleUserMap() throws Exception {
		peticion.setCredenciales();
		MvcResult result = peticion.get("/" + MAPPING + "/user/" + PropiedadesTest.getUserId() + "/103")
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	
	/**
	 * Metodo crear mapa 
	 */
	@Test
	public void testCrear() throws Exception {
		peticion.setCredenciales();
		String body = "{"
				+ "	\"id\": 303,"
				+ "	\"title\": \"Mi Mapa\","
				+ "	\"type\": \"private\","
				+ "	\"owned\": true,"
				+ "	\"lastUpdated\": \"2016-02-23T12:28:08Z\","
				+ "	\"pois\": [{"
				+ "		\"id\": \"4461\","
				+ "		\"title\": \"mi poi\","
				+ "		\"description\": \"descripción del punto de interes\","
				+ "		\"icon\": \"http://www.zaragoza.es/contenidos/iconos/generico.png\","
				+ "		\"style\": {},"
				+ "		\"updateable\": true,"
				+ "		\"geometry\": {"
				+ "			\"type\": \"Point\","
				+ "			\"coordinates\": [-0.8857639244867359, 41.64788199360079]"
				+ "		}"
				+ "	}]"
				+ "}";
		MvcResult result = peticion.post("/" + MAPPING + "/user/" + PropiedadesTest.getUserId()
					, body)
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
		
		JsonNode json = Funciones.asJson(result.getResponse().getContentAsString());
		
		result = peticion.get("/" + MAPPING + "/user/" + PropiedadesTest.getUserId() + "/" + json.get("id"))
				.andExpect(status().isOk())
				.andReturn();
		
		Assert.assertTrue(result.getResponse().getStatus()==200);
		
		result = peticion.get("/" + MAPPING + "/user/" + PropiedadesTest.getUserId() + "/" + json.get("id") + "/remove")
				.andExpect(status().isOk())
				.andReturn();
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	
	/**
	 * Metodo crear mapa sin puntos 
	 */
	@Test
	public void testCrearSinPois() throws Exception {
		peticion.setCredenciales();
		String body = "{"
				+ "    \"title\": \"Columpios2\""
				+ "}";
		MvcResult result = peticion.post("/" + MAPPING + "/user/" + PropiedadesTest.getUserId()
					, body)
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
		
		JsonNode json = Funciones.asJson(result.getResponse().getContentAsString());
		
		result = peticion.get("/" + MAPPING + "/user/" + PropiedadesTest.getUserId() + "/" + json.get("id") + "/remove")
				.andExpect(status().isOk())
				.andReturn();
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	
	/**
	 * Metodo crear mapa en WGS84 
	 */
	@Test
	public void testCrearWgs84() throws Exception {
		peticion.setCredenciales();
		String body = "{"
				+ "    \"title\": \"Columpios\","
				+ "    \"pois\": ["
				+ "        {"
				+ "            \"id\": \"monumento-79\","
				+ "            \"description\": \"mi descripcion entera\""
				+ "        },"
				+ "        {"
				+ "            \"id\": \"671\","
				+ "            \"title\": \"dsfgsdfg\","
				+ "            \"description\": \"sdfgsdfg\","
				+ "            \"geometry\": {"
				+ "                \"type\": \"Point\","
				+ "                \"coordinates\": ["
				+ "                    -0.9298811327357448,"
				+ "                    41.65260966067856"
				+ "                ]"
				+ "            }"
				+ "        }"
				+ "    ]"
				+ "}";
		MvcResult result = peticion.post("/" + MAPPING + "/user/" + PropiedadesTest.getUserId()
					, body)
				.andExpect(status().isOk())
				.andReturn()
				;
		Assert.assertTrue(result.getResponse().getStatus()==200);
		JsonNode json = Funciones.asJson(result.getResponse().getContentAsString());
		
		result = peticion.get("/" + MAPPING + "/user/" + PropiedadesTest.getUserId() + "/" + json.get("id"))
				.andExpect(status().isOk())
				.andReturn();
		
		Assert.assertTrue(result.getResponse().getStatus()==200);
		
		result = peticion.get("/" + MAPPING + "/user/" + PropiedadesTest.getUserId() + "/" + json.get("id") + "/remove")
				.andExpect(status().isOk())
				.andReturn();
		Assert.assertTrue(result.getResponse().getStatus()==200);
	}
	
}