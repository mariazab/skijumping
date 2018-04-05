package fi.haagahelia.skijumping.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.haagahelia.skijumping.domain.Athlete;
import fi.haagahelia.skijumping.domain.AthleteRepository;

@Controller
public class AthleteController {

	@Autowired
	AthleteRepository repository;
	
	// Showing all athletes
	@RequestMapping(value="/athletes", method=RequestMethod.GET)
	public String showAthletes(Model model) {
		model.addAttribute("athletes", repository.findAll());
		return "athletes";
	}
	
	// Adding new athlete
	@RequestMapping("/addAthlete")
	public String addAthlete(Model model) {
		model.addAttribute("athlete", new Athlete());
		return "addAthlete";
	}
	
	// Saving new athlete
	@RequestMapping("/saveAthlete")
	public String saveAthlete(Athlete athlete) {
		repository.save(athlete);
		return "redirect:athletes";
	}
	
	// Deleting athlete
	@RequestMapping("/deleteAthlete/{id}")
	public String deleteAthlete(@PathVariable("id") Long athleteId, Model model) {
		repository.delete(athleteId);
		return "redirect:../athletes";
	}
	
	// Edit athlete
	@RequestMapping("/editAthlete/{id}") 
	public String editAthlete(@PathVariable("id") Long athleteId, Model model) {
		model.addAttribute("athlete", repository.findOne(athleteId));
		return "editAthlete";
	}
	
	// RESTful service to get all athletes
	@RequestMapping(value="/athletesApi", method=RequestMethod.GET)
	public @ResponseBody List<Athlete> athleteListRest() {
		return (List<Athlete>) repository.findAll();
	}
	
	//RESTful service to get athlete by id
	@RequestMapping(value="/athletesApi/{id}", method=RequestMethod.GET)
	public @ResponseBody Athlete findAthleteRest(@PathVariable("id") Long athleteId) {
		return repository.findOne(athleteId);
	}
	
}
