package it.prova.gestionesatelliti.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SatelliteValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Satellite.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Satellite satellite= (Satellite) target;
		
		if(satellite.getDataLancio()==null && satellite.getDataRientro()!=null) {
			errors.rejectValue("dataLancio", "", "Inserire una data di Lancio");
		}
		
		if(satellite.getStato()==StatoSatellite.DISATTIVATO && satellite.getDataRientro()== null) {
			errors.rejectValue("stato","", "Inserire una data di rientro, modificare");
		}
		
		if(satellite.getDataLancio()!=null && satellite.getDataRientro()!=null && satellite.getDataLancio().after(satellite.getDataRientro())) {
			errors.rejectValue("dataLancio","", "Data di rientro precedente alla data di lancio, modificare");
		}
		
		if((satellite.getStato()==StatoSatellite.IN_MOVIMENTO || satellite.getStato()==StatoSatellite.FISSO) && satellite.getDataRientro()!= null ) {
			errors.rejectValue("dataRientro","", "La data di rientro deve essere a null, modificare");
		}
		
	}
	
	public boolean isValidatorForDelete(Object target) {
		Satellite satellite= (Satellite) target;
		
		if(satellite.getDataLancio()!=null ) {
			return true;
		}
		if(satellite.getStato()==StatoSatellite.DISATTIVATO && satellite.getDataRientro()==null ) {
			return true;
		}
		
		return false;
	}

}
