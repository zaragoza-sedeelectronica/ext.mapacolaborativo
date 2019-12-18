package org.sede.servicio.mapacolaborativo;

import org.sede.core.PropertyFileInterface;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "mapacolaborativoCfg")
public class ConfigMapaColaborativo implements PropertyFileInterface {
	
	public static final String ESQUEMA = "GENERAL";
	public static final String TM = "transactionManagerGeneral";
	
	public String getSchema() {
		return "general";
	}
	public String getJndi() {
		return "WebGeneralDS";
	}

	public String getEntity() {
		return "org.sede.servicio.mapacolaborativo.entity";
	}

}
