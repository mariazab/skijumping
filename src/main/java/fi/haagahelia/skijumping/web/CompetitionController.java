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
import fi.haagahelia.skijumping.domain.Competition;
import fi.haagahelia.skijumping.domain.CompetitionRepository;
import fi.haagahelia.skijumping.domain.HillRepository;
import fi.haagahelia.skijumping.domain.Result2018;
import fi.haagahelia.skijumping.domain.Result2018Repository;
import fi.haagahelia.skijumping.domain.WcPoint;
import fi.haagahelia.skijumping.domain.WcPointRepository;
import fi.haagahelia.skijumping.domain.WcStanding2018;
import fi.haagahelia.skijumping.domain.WcStanding2018Repository;

@Controller
public class CompetitionController {

	@Autowired
	CompetitionRepository repository;
	
	@Autowired
	HillRepository hillRepository;
	
	@Autowired
	Result2018Repository resultRepository;
	
	@Autowired
	WcPointRepository wcPointRepository;
	
	@Autowired
	AthleteRepository athleteRepository;
	
	@Autowired
	WcStanding2018Repository wcStandingRepository;
	
	// Showing all competitions
	@RequestMapping("/competitions")
	public String showCompetitions(Model model) {
		model.addAttribute("competitions", repository.findAll());
		return "competitions";
	}
	
	// Showing results for one competition
	@RequestMapping("/results/{competitionId}")
	public String showResults(@PathVariable("competitionId") Long competitionId, Model model) {
		model.addAttribute("results", resultRepository.findByCompetitionIdOrderByWcPoint(competitionId));
		model.addAttribute("competition", repository.findOne(competitionId));
		return "results";
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
	
	// Adding new results for a competition
	@RequestMapping("/addResult/{competitionId}")
	public String addResult(@PathVariable("competitionId") Long competitionId, Model model) {
		model.addAttribute("result", new Result2018());
		model.addAttribute("wcpoints", wcPointRepository.findAll());
		model.addAttribute("athletes", athleteRepository.findAll());
		model.addAttribute("competition", repository.findOne(competitionId));
		return "addResults";
	}
	
	// Saving new results for a competition
	@RequestMapping("/saveResult")
	public String saveResult(Result2018 result2018) {
		resultRepository.save(result2018);
		updateStandings(result2018);
		return "redirect:results/" + result2018.getCompetition().getId();
	}
	
	// Saving edited result for a competition
	@RequestMapping("/saveEditResult")
	public String saveEditResult(Result2018 result2018) {
		resultRepository.save(result2018);
		return "redirect:competitions";
	}
	
	// Edit competition
	@RequestMapping("/editCompetition/{id}")
	public String editCompetition(@PathVariable("id") Long competitionId, Model model) {
		model.addAttribute("competition", repository.findOne(competitionId));
		model.addAttribute("hills", hillRepository.findAll());
		return "editCompetition";
	}
	
	// Editing results of a competition
	@RequestMapping("/editResult/{id}")
	public String editResult(@PathVariable("id") Long resultId, Model model) {
		model.addAttribute("result", resultRepository.findOne(resultId));
		model.addAttribute("athletes", athleteRepository.findAll());
		return "editResult";
	}
	
	// Delete competition
	@RequestMapping("/deleteCompetition/{id}")
	public String deleteCompetition(@PathVariable("id") Long competitionId, Model model) {
		repository.delete(competitionId);
		return "redirect:../competitions";
	}
	
	// Delete result
	@RequestMapping("/deleteResult/{id}")
	public String deleteResult(@PathVariable("id") Long resultId, Model model) {
		resultRepository.delete(resultId);
		return "redirect:../competitions";
	}
	
	// RESTful service to get all competitions
	@RequestMapping(value="/competitionsApi", method=RequestMethod.GET)
	public @ResponseBody List<Competition> competitionListRest() {
		return (List<Competition>) repository.findAll();
	}
	
	// RESTful service to get all results
	@RequestMapping(value="/resultsApi", method=RequestMethod.GET)
	public @ResponseBody List<Result2018> resultListRest() {
		return (List<Result2018>) resultRepository.findAll();
	}
	
	//Updating World Cup Standings
	public void updateStandings(Result2018 result2018) {
		//If the competition type is individual, update the standings
		if(result2018.getCompetition().getType().equals("Individual")) {
			// Finding the corresponding athlete
			Athlete athlete = athleteRepository.findOne(result2018.getAthlete().getId());
			System.out.println(athlete);
			//Getting the points to add to standings
			WcPoint wcPoint = wcPointRepository.findByPosition(result2018.getWcPoint().getPosition());
			int points = wcPoint.getPoints();
			System.out.println(points);
			//Finding the WcStanding with the athlete
			
			try {
				//Update the points of the athlete
				WcStanding2018 wcStanding = wcStandingRepository.findByAthleteId(athlete.getId());
				wcStanding.setPoints(wcStanding.getPoints() + points);
				System.out.println(wcStanding.getPoints());
				wcStandingRepository.save(wcStanding);
			} catch (NullPointerException e) {
				//If the wcStanding does not exits, then create the standing for the athlete
				WcStanding2018 standing = new WcStanding2018(athlete, points);
				wcStandingRepository.save(standing);
				System.out.println(standing);
			}
		}
		
	}
}
