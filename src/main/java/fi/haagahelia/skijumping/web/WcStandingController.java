package fi.haagahelia.skijumping.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.haagahelia.skijumping.domain.AthleteRepository;
import fi.haagahelia.skijumping.domain.WcStanding2018;
import fi.haagahelia.skijumping.domain.WcStanding2018Repository;

@Controller
public class WcStandingController {

	@Autowired
	WcStanding2018Repository repository;
	
	@Autowired
	AthleteRepository athleteRepository;
	
	//Showing all standings
	@RequestMapping("/standings")
	public String showStandings(Model model) {
		model.addAttribute("standings", repository.findAllByOrderByPointsDesc());
		return "standings";
	}
	
	//Saving standing 
	@RequestMapping("/saveStanding")
	public String saveStanding(WcStanding2018 standing) {
		repository.save(standing);
		return "redirect:standings";
	}
	
	// Editing standing
	@RequestMapping("/editStanding/{id}")
		public String editCompetition(@PathVariable("id") Long standingId, Model model) {
			model.addAttribute("standing", repository.findOne(standingId));
			model.addAttribute("athletes", athleteRepository.findAll());
			return "editStanding";
		}
	
	//Deleting standing
	@RequestMapping("/deleteStanding/{id}")
	public String deleteStanding(@PathVariable("id") Long standingId, Model model) {
		WcStanding2018 standing = repository.findOne(standingId);
		repository.delete(standing);
		System.out.println(standingId);
		return "redirect:../standings";
	}
	
	// RESTful service to get standings
		@RequestMapping(value="/standingsApi", method=RequestMethod.GET)
		public @ResponseBody List<WcStanding2018> standingsListRest() {
			return (List<WcStanding2018>) repository.findAll();
		}
}
