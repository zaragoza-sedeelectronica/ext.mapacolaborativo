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
