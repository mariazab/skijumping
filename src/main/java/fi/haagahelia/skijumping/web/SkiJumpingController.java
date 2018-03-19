package fi.haagahelia.skijumping.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fi.haagahelia.skijumping.domain.Athlete;
import fi.haagahelia.skijumping.domain.AthleteRepository;

@Controller
public class SkiJumpingController {

	@Autowired
	AthleteRepository repository;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String skijumping(Model model) {
          return "index";
    }
	
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

}
