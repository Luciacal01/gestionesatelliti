package it.prova.gestionesatelliti.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.repository.SatelliteRepository;

@Service
public class SatelliteServiceImpl implements SatelliteService {
	
	@Autowired
	private SatelliteRepository repository;
	//new IdNonTrovato("Questo id non è presente")
	@Override
	@Transactional(readOnly=true)
	public List<Satellite> listAllElements() {
		return (List<Satellite>) repository.findAll(); 
	}

	@Override
	@Transactional(readOnly=true)
	public Satellite caricaSingoloElemento(Long id) {
		return repository.findById(id).orElseThrow(null);
	}

	@Override
	@Transactional
	public void aggiorna(Satellite satelliteInstance) {
		repository.save(satelliteInstance);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Satellite satelliteInstance) {
		repository.save(satelliteInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long idSatellite) {
		repository.deleteById(idSatellite);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Satellite> findByExample(Satellite example) {
		Specification<Satellite> specificationCriteria=(root, quert, cb)->{
			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getDenominazione()))
				predicates.add(cb.like(cb.upper(root.get("denominazione")), "%" + example.getDenominazione().toUpperCase() + "%"));

			if (StringUtils.isNotEmpty(example.getCodice()))
				predicates.add(cb.like(cb.upper(root.get("codice")), "%" + example.getCodice().toUpperCase() + "%"));

			if (example.getDataLancio()!=null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataLancio"), example.getDataLancio()));
			
			if (example.getDataRientro() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataRientro"), example.getDataRientro()));

			if (example.getStato() != null)
				predicates.add(cb.equal(root.get("stato"), example.getStato()));

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
		
		return repository.findAll(specificationCriteria);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Satellite> cercaSatellitiInOrbitaPerPiùDiDueAnni(Date data, StatoSatellite stato) {
		return (List<Satellite>) repository.findAllByDataLancioBeforeAndStatoNot(data, stato);
	}

	@Override
	public List<Satellite> cercaSatellitiFissiErimastiInOrbitaPer(Date dataLancio, StatoSatellite stato) {
		return (List<Satellite>) repository.findAllByDataLancioBeforeAndStato(dataLancio, stato);
	}

	@Override
	public List<Satellite> cercaSatellitiNonRientrati(StatoSatellite stato) {
		return (List<Satellite>) repository.findAllByStatoAndDataRientroIsNull(stato);
	}

}
