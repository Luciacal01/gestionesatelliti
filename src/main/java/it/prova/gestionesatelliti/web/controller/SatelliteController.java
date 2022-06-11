package it.prova.gestionesatelliti.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.SatelliteValidator;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {
	
	@Autowired
	private SatelliteService satelliteService;
	
	@Autowired
	private SatelliteValidator satelliteValidator;
	
	@GetMapping("/listAll")
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/search")
	public String search() {
		return "satellite/search";
	}
	
	@PostMapping("/list")
	public String listByExample(Satellite example, ModelMap model) {
		List<Satellite> reslts= satelliteService.findByExample(example);
		model.addAttribute("satellite_list_attribute", reslts);
		return "satellite/list";
	}
	
	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("insert_satellite_attribute", new Satellite());
		return "satellite/insert";
	}
	
	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attribute") Satellite satellite, BindingResult result, RedirectAttributes redirectAttributes) {
		//if(satellite.getDataLancio()!=null || satellite.getDataRientro()!=null) {
			satelliteValidator.validate(satellite, result);
		//}
		
		if(result.hasErrors()) 
			return "satellite/insert";

		
		satelliteService.inserisciNuovo(satellite);
		redirectAttributes.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite/listAll";
	}
	
	@GetMapping("/show/{idSatellite}")
	public String show(@PathVariable(required =true) Long idSatellite, Model model) {
		model.addAttribute("show_satellite_attribute", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/show";
	}
	
	@GetMapping("/delete/{idSatellite}")
	public String delete(@PathVariable(required =true) Long idSatellite, Model model) {
		model.addAttribute("delete_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/delete";
	}
	
	@PostMapping("/remove")
	public String remove(@Valid @RequestParam(required = true) Long idSatellite, Model model, BindingResult result, RedirectAttributes redirectAttributes) {
		if(result.hasErrors())
			return "satellite/delete/{idSatellite}";
		Satellite satellite= satelliteService.caricaSingoloElemento(idSatellite);
		
		//result
		
		if(satellite.getDataLancio()!=null && satellite.getDataRientro()==null ) {
			result.rejectValue("dataLancio", "", "impossibile eliminare un satellite non ancora rientrato");
			return "satellite/delete/{idSatellite}";
		}
		if(satellite.getStato().equals(StatoSatellite.DISATTIVATO) && satellite.getDataRientro()==null ) {
			result.rejectValue("dataRientro", "", "impossibile eliminare un satellite non ancora rientrato");
			return "satellite/delete/{idSatellite}";
		}
		
		
		satelliteService.rimuovi(idSatellite);
		
		redirectAttributes.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite/listAll";
		
	}
	
}
