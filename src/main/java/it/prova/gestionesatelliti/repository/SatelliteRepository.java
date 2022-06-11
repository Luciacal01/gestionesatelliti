package it.prova.gestionesatelliti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite> {
	List<Satellite> findAllByDataLancioBeforeAndStatoNot(Date data, StatoSatellite stato);
	
	List<Satellite> findAllByStatoAndDataRientroIsNull(StatoSatellite stato);
	
	List<Satellite> findAllByDataLancioAndDataRientroAndStato(Date dataLancio, Date dataRientro, StatoSatellite stato);
}
