package fi.haagahelia.skijumping.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fi.haagahelia.skijumping.domain.AthleteRepository;
import fi.haagahelia.skijumping.domain.Competition;
import fi.haagahelia.skijumping.domain.CompetitionRepository;
import fi.haagahelia.skijumping.domain.Result2018;
import fi.haagahelia.skijumping.domain.Result2018Repository;
import fi.haagahelia.skijumping.domain.User;
import fi.haagahelia.skijumping.domain.UserRepository;
import fi.haagahelia.skijumping.domain.WcStanding2018;
import fi.haagahelia.skijumping.domain.WcStanding2018Repository;


@Controller
public class SkiJumpingController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	CompetitionRepository competitionRepository;
	
	@Autowired
	AthleteRepository athleteRepository;
	
	@Autowired
	Result2018Repository resultRepository;
	
	@Autowired
	WcStanding2018Repository standingsRepository;
	
	@RequestMapping(value="/dashboard", method=RequestMethod.GET)
	public String skijumping(Model model) {
		//Get the name of the current user and pass it as a model
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	    User user = userRepository.findByUsername(loggedInUser.getName());
	    String name = user.getName();
	    
	    //Get next competition and pass it
	    Competition competition = getNextCompetition();
	    
	    
	    //Get the top 3 from last competition
	    Competition lastCompetition = getLastCompetition();
	    long competitionId = lastCompetition.getId();
	    List<Result2018> results = resultRepository.findByCompetitionIdOrderByWcPoint(competitionId);
	    List<Result2018> topThree = new ArrayList<Result2018>();
		for(int i = 0; i < results.size(); i++) {
			if(results.get(i).getWcPoint().getPosition() < 4) {
				topThree.add(results.get(i));
			}
		}
		
		//Get top 3 from standings
		List<WcStanding2018> standings = standingsRepository.findAllByOrderByPointsDesc();
		List<WcStanding2018> wcTopThree = new ArrayList<WcStanding2018>();
		for(int i = 0; i < 3; i++) {
			wcTopThree.add(standings.get(i));
		}
		model.addAttribute("wcTopThree", wcTopThree);
		model.addAttribute("lastCompetition", lastCompetition);
	    model.addAttribute("topThree", topThree);
	    model.addAttribute("competition", competition);
	    model.addAttribute("name", name);
		return "dashboard";
    }
	
	public Competition getNextCompetition() {
		Date today = new Date();
		Competition competition = new Competition();
		List<Competition> competitions = (List<Competition>) competitionRepository.findAllByOrderById();
		for(int i = 0; i < competitions.size(); i++) {
			Date competitionDate = competitions.get(i).getDate();
			if(today.before(competitionDate)) {
				competition = competitions.get(i);
				i =  competitions.size();
				}
		}
		return competition;
		
	}
	
	public Competition getLastCompetition() {
		Date today = new Date();
		Competition competition = new Competition();
		List<Competition> competitions = (List<Competition>) competitionRepository.findAllByOrderById();
		for(int i = (competitions.size() - 1);i > 0; i--) {
			Date competitionDate = competitions.get(i).getDate();
			if(today.after(competitionDate)) {
				competition = competitions.get(i);
				i = 0;
				}
		}
		return competition;
	}

}
