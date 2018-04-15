package fi.haagahelia.skijumping.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.haagahelia.skijumping.domain.AthleteRepository;
import fi.haagahelia.skijumping.domain.Hill;
import fi.haagahelia.skijumping.domain.HillRecord;
import fi.haagahelia.skijumping.domain.HillRecordRepository;
import fi.haagahelia.skijumping.domain.HillRepository;
import fi.haagahelia.skijumping.domain.User;
import fi.haagahelia.skijumping.domain.UserRepository;

@Controller
public class HillController {

	@Autowired
	HillRepository repository;
	
	@Autowired HillRecordRepository recordRepository;
	
	@Autowired AthleteRepository athleteRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	// Showing all hills with records
	@RequestMapping("/hills")
	public String showHills(Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	    User user = userRepository.findByUsername(loggedInUser.getName());
	    String name = user.getName();
	    model.addAttribute("name", name);
		
		model.addAttribute("hills", repository.findAll());
		/*model.addAttribute("records", recordRepository.findAll());*/
		return "hills";
	}
	
	// Adding new hill
	@RequestMapping("/addHill")
	public String addHill(Model model) {
		model.addAttribute("hill", new Hill());
		return "addHill";
	}
	
	// Adding new record
	@RequestMapping("/addRecord")
	public String addRecord(Model model) {
		model.addAttribute("record", new HillRecord());
		model.addAttribute("hills", repository.findAll());
		model.addAttribute("athletes", athleteRepository.findAll());
		return "addRecord";
	}
	
	// Saving new hill
	@RequestMapping("/saveHill")
	public String saveHill(Hill hill) {
		repository.save(hill);
		return "redirect:hills";
	}
	
	//Saving new record
	@RequestMapping("/saveRecord") 
	public String saveRecord(HillRecord hillRecord) {
		recordRepository.save(hillRecord);
		return "redirect:hills";
	}
	
	// Editing hill
	@RequestMapping("/editHill/{id}")
	public String editHill(@PathVariable("id") Long hillId, Model model) {
		model.addAttribute("hill", repository.findOne(hillId));
		return "editHill";
	}
	
	// Editing record
	@RequestMapping("/editRecord/{id}")
	public String editRecord(@PathVariable("id") Long recordId, Model model) {
		model.addAttribute("record", recordRepository.findOne(recordId));
		model.addAttribute("hills", repository.findAll());
		model.addAttribute("athletes", athleteRepository.findAll());
		return "editRecord";
	}

	// Deleting hill
	@RequestMapping("/deleteHill/{id}")
	public String deleteHill(@PathVariable("id") Long hillId, Model model) {
		repository.delete(hillId);
		return "redirect:../hills";
	}
	
	// Deleting record
	@RequestMapping("/deleteRecord/{id}")
	public String deleteRecord(@PathVariable("id") Long recordId, Model model) {
		//Finding the hill with the record no. recordId
		Hill hill = (recordRepository.findOne(recordId)).getHill();
		
		//Setting the record to be null
		hill.setHillRecord(null);
		
		//Deleting record
		recordRepository.delete(recordId);
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
	
	//RESTful service to get all hill records
	@RequestMapping(value="/hillRecordsApi", method=RequestMethod.GET)
	public @ResponseBody List<HillRecord> hillRecordListRest() {
		return (List<HillRecord>) recordRepository.findAll();
	}
	
	//RESTful service to get hill records by Id
	@RequestMapping(value="/hillRecordsApi/{id}", method=RequestMethod.GET)
	public @ResponseBody HillRecord findRecordRest(@PathVariable("id") Long recordId) {
		return recordRepository.findOne(recordId);		
	}
}
