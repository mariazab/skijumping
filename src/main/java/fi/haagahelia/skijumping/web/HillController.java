package fi.haagahelia.skijumping.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.haagahelia.skijumping.domain.Hill;
import fi.haagahelia.skijumping.domain.HillRepository;

@Controller
public class HillController {

	@Autowired
	HillRepository repository;
	
	// Showing all hills
	@RequestMapping("/hills")
	public String showHills(Model model) {
		model.addAttribute("hills", repository.findAll());
		return "hills";
	}
	
	// Adding new hill
	@RequestMapping("/addHill")
	public String addHill(Model model) {
		model.addAttribute("hill", new Hill());
		return "addHill";
	}
	
	// Saving new hill
	@RequestMapping("/saveHill")
	public String saveHill(Hill hill) {
		repository.save(hill);
		return "redirect:hills";
	}
	
	// Editing hill
	@RequestMapping("/editHill/{id}")
	public String editHill(@PathVariable("id") Long hillId, Model model) {
		model.addAttribute("hill", repository.findOne(hillId));
		return "editHill";
	}

	// Delete hill
	@RequestMapping("/deleteHill/{id}")
	public String deleteHill(@PathVariable("id") Long hillId, Model model) {
		repository.delete(hillId);
		return "redirect:../hills";
	}
	
	//RESTful service to get all hills
	@RequestMapping(value="/hillsApi", method=RequestMethod.GET)
	public @ResponseBody List<Hill> hillListRest() {
		return (List<Hill>) repository.findAll();
	}
	
	//RESTful service to get hill by id
		@RequestMapping(value="/hillsApi/{id}", method=RequestMethod.GET)
		public @ResponseBody Hill findHillRest(@PathVariable("id") Long hillId) {
			return repository.findOne(hillId);
		}
}
