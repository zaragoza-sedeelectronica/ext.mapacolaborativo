package org.sede.servicio.mapacolaborativo;

import org.sede.core.PropertyFileInterface;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "mapacolaborativoCfg")
public class ConfigMapaColaborativo implements PropertyFileInterface {
	
	public static final String ESQUEMA = "participacion";
	public static final String TM = "transactionManagerParticipacion";
	
	public String getSchema() {
		return ESQUEMA;
	}
	public String getJndi() {
		return "WebParticipacionDS";
	}

	public String getEntity() {
		return "org.sede.servicio.mapacolaborativo.entity";
	}

}
