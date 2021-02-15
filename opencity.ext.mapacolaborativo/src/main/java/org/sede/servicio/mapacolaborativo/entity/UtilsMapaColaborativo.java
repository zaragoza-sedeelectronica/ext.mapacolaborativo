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

import java.util.Calendar;
import java.util.Date;

/**
 * Clase UtilsMapaColaborativo 
 * 
 * @author Ayuntamiento Zaragoza
 *
 */
public class UtilsMapaColaborativo {
	private UtilsMapaColaborativo() {
		super();
	}
	/**
	 * Metodo ajustarFechaInicio
	 */
	public static Date ajustarFechaInicio(Date startDate) {
		
		if (startDate == null) {
			startDate = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.DATE, -150);
			startDate = cal.getTime();
		}
		
		return startDate;
	}

	/**
	 * Metodo ajustarFechaFin
	 */
	public static Date ajustarFechaFin(Date startDate, Date endDate) {
		if (endDate == null) {
			endDate = new Date();
		} else if (calcularDias(startDate, endDate) > 150 || calcularDias(startDate, endDate) < 0) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.DATE, 150);
			endDate = cal.getTime();
		}
		return endDate;
	}

	/**
	 * metodo calcularDias 
	 */
	public static long calcularDias(Date dateEarly, Date dateLater) {  
		   return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);  
	}
	
}
