package org.sede.servicio.mapacolaborativo;

import org.sede.core.PropertyFileInterface;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "mapacolaborativoCfg")
public class ConfigMapaColaborativo implements PropertyFileInterface {
	public String getSchema() {
		return "participacion";
	}
	public String getJndi() {
		return "WebParticipacionDS";
	}

	public String getEntity() {
		return "org.sede.servicio.mapacolaborativo.entity";
	}

}
