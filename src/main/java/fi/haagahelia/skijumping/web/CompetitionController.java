package fi.haagahelia.skijumping.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.haagahelia.skijumping.domain.Competition;
import fi.haagahelia.skijumping.domain.CompetitionRepository;
import fi.haagahelia.skijumping.domain.HillRepository;

@Controller
public class CompetitionController {

	@Autowired
	CompetitionRepository repository;
	
	@Autowired
	HillRepository hillRepository;
	
	// Showing all competitions
	@RequestMapping("/competitions")
	public String showCompetitions(Model model) {
		model.addAttribute("competitions", repository.findAll());
		return "competitions";
	}
	
	// Adding new competition
	@RequestMapping("/addCompetition")
	public String addCompetition(Model model) {
		model.addAttribute("competition", new Competition());
		model.addAttribute("hills", hillRepository.findAll());
		return "addCompetition";
	}
	
	// Saving new competition
	@RequestMapping("/saveCompetition")
	public String saveCompetition(Competition competition) {
		repository.save(competition);
		return "redirect:competitions";
	}
	
	// Edit competition
	@RequestMapping("/editCompetition/{id}")
	public String editCompetition(@PathVariable("id") Long competitionId, Model model) {
		model.addAttribute("competition", repository.findOne(competitionId));
		model.addAttribute("hills", hillRepository.findAll());
		return "editCompetition";
	}
	
	// Delete competition
	@RequestMapping("/deleteCompetition/{id}")
	public String deleteCompetition(@PathVariable("id") Long competitionId, Model model) {
		repository.delete(competitionId);
		return "redirect:../competitions";
	}
	
	// RESTful service to get all competitions
	@RequestMapping(value="/competitionsApi", method=RequestMethod.GET)
	public @ResponseBody List<Competition> competitionListRest() {
		return (List<Competition>) repository.findAll();
	}
}
