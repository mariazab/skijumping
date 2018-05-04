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

import fi.haagahelia.skijumping.domain.Athlete;
import fi.haagahelia.skijumping.domain.AthleteRepository;
import fi.haagahelia.skijumping.domain.Competition;
import fi.haagahelia.skijumping.domain.CompetitionRepository;
import fi.haagahelia.skijumping.domain.FavAthlete;
import fi.haagahelia.skijumping.domain.FavAthleteRepository;
import fi.haagahelia.skijumping.domain.Hill;
import fi.haagahelia.skijumping.domain.HillRecord;
import fi.haagahelia.skijumping.domain.Result2018;
import fi.haagahelia.skijumping.domain.Result2018Repository;
import fi.haagahelia.skijumping.domain.User;
import fi.haagahelia.skijumping.domain.UserRepository;
import fi.haagahelia.skijumping.domain.WcStanding2018;
import fi.haagahelia.skijumping.domain.WcStanding2018Repository;
import fi.haagahelia.skijumping.mail.EmailService;
import fi.haagahelia.skijumping.mail.Mail;

@Controller
public class SkiJumpingController  {
	
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
	
	@Autowired
	FavAthleteRepository favAthleteRepository;
	
	@Autowired
    private EmailService emailService;
	
	@RequestMapping("/")
	public String redirect(Model model) {
		return "redirect:dashboard";
	}
	
	@RequestMapping(value="/dashboard", method=RequestMethod.GET)
	public String skijumping(Model model) {
		//Get the name of the current user and pass it as a model
	    User user = getCurrentUser();
	    String name = user.getName();
	    Long userId = user.getId();
	    
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
		
		//Results of user's favourite athletes
		List<FavAthlete> favAthletes = favAthleteRepository.findByUserId(userId);
		List<Result2018> favResults = new ArrayList<Result2018>();
		for(int i = 0; i < favAthletes.size(); i++) {
			for(int j = 0; j < results.size(); j++) {
				if(results.get(j).getAthlete().getId() == favAthletes.get(i).getAthlete().getId()) {
					favResults.add(results.get(j));
				}
			}
			
		}
		
		//Get top 3 from standings
		List<WcStanding2018> standings = standingsRepository.findAllByOrderByPointsDesc();
		List<WcStanding2018> wcTopThree = new ArrayList<WcStanding2018>();
		for(int i = 0; i < 3; i++) {
			wcTopThree.add(standings.get(i));
		}
		
		//Standings of user's favorite athletes
		List<WcStanding2018> favStandings = new ArrayList<WcStanding2018>();
		List<Integer> favWcPosition = new ArrayList<Integer>();
		for(int i = 0; i < favAthletes.size(); i++) {
			
			for(int j = 0; j < standings.size(); j++) {
				if(standings.get(j).getAthlete().getId() == favAthletes.get(i).getAthlete().getId()) {
					favStandings.add(standings.get(j));
					favWcPosition.add(j);
				}
			}
			
		}

		model.addAttribute("favWcPosition", favWcPosition);
		model.addAttribute("favStandings", favStandings);		
		model.addAttribute("favResults", favResults);
		model.addAttribute("wcTopThree", wcTopThree);
		model.addAttribute("lastCompetition", lastCompetition);
	    model.addAttribute("topThree", topThree);
	    model.addAttribute("competition", competition);
	    model.addAttribute("name", name);
		return "dashboard";
    }
	
	@RequestMapping("/sendEmail")
	public String sendEmail() {
		//Get next competition
		Competition competition = getNextCompetition();
		
		//Get currently logged user and user's name and email
		User user = getCurrentUser();
	    String name = user.getName();
	    String email = user.getEmail();
	    
	    //Save information mentioned in email
	    Hill hill = competition.getHill();
	    HillRecord record = hill.getHillRecord();
	    Athlete athlete = record.getAthlete();
	    
	    //Set the content of the email
	    String content = "Hi " + name + "!\n\nHow's everything going?\n\nWe are sending you information about the next competition!\n\nType: " + competition.getType() + "\nDate: " + competition.getDate() + "\nStarting time: " + competition.getTime().substring(0, 5) + "\nHill: " + hill.getName() + "\nCity: " + hill.getCity() + "\nYear of construction: " + hill.getBuildYear() + "\nK-Point: " + hill.getkPoint() + "m\nHS: " + hill.getHsPoint() + "m\nRecord: " + record.getLength() + "m, " + athlete.getFirstName() + " " + athlete.getLastName() + " in " + record.getYear() + "\n\nMay the wind be in your favour! :)\n\n\n\nThis email has been sent automatically by worldofskijumping@gmail.com";
				
	    Mail mail = new Mail();
        mail.setFrom("worldofskijumping@gmail.com");
        mail.setTo(email);
        mail.setSubject("Information about " + competition.getType() + " competition in " + competition.getHill().getCity() + " HS" + competition.getHill().getHsPoint());
        mail.setContent(content);
        
        emailService.sendSimpleMessage(mail);
   
		return "redirect:dashboard";
	}
	
	
	//Get and return currently logged user
	public User getCurrentUser() {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	    User user = userRepository.findByUsername(loggedInUser.getName());
	    return user;
	}
	
	//Get and return next competition
	public Competition getNextCompetition() {
		Date today = new Date();
		Competition competition = new Competition();
		List<Competition> competitions = (List<Competition>) competitionRepository.findAllByOrderById();
		for(int i = 0; i < competitions.size(); i++) {
			Date competitionDate = competitions.get(i).getDate();
			if(today.getDay() == competitionDate.getDay() && today.getMonth() == competitionDate.getMonth() && today.getYear() == competitionDate.getYear()) {
				competition = competitions.get(i);
				i =  competitions.size();
				
				}
			else if(today.before(competitionDate)) {
				competition = competitions.get(i);
				i =  competitions.size();
			
			}
		}
		return competition;
		
	}
	
	//Get and return last competition
	public Competition getLastCompetition() {
		Date today = new Date();
		Competition competition = new Competition();
		List<Competition> competitions = (List<Competition>) competitionRepository.findAllByOrderById();
		for(int i = (competitions.size() - 1);i > 0; i--) {
			Date competitionDate = competitions.get(i).getDate();
			if(today.after(competitionDate)) {
				if(today.getDay() != competitionDate.getDay()) {
					competition = competitions.get(i);
					i = 0;
				}
				
				}
		}
		return competition;
	}

}
