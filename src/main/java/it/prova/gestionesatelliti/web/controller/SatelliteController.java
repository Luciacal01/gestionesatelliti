package it.prova.gestionesatelliti.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {
	
	@Autowired
	private SatelliteService satelliteService;
	
	@GetMapping
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
}
