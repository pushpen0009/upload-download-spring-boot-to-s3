package com.mytoshika.spring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PersonController {

	@Autowired
	private Person person;
	
	@RequestMapping("/")
	public String healthCheck() {
		return "OK";
	}

	@Value("${json.data}")
	private String data;
	
	@RequestMapping(value="/person/get",method=RequestMethod.GET)
	public Person getPerson(@RequestParam(name="name", required=false, defaultValue="Unknown") String name) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode actualObj = mapper.readTree(data);
			System.out.println("@@@@@@@@@@@@@@"+actualObj.get("data").textValue());
			updatePerson(new Person());
		} catch (IOException e) {
			e.printStackTrace();
		}

		person.setName(name);
		return person;
	}

	
//	@RequestMapping(value="/person/update", method=RequestMethod.POST)
//	public Person updatePerson(@RequestParam(name="name", required=true) String name) {
//		person.setName(name);
//		return person;
//	}
	
	@RequestMapping(value="/person/update", method=RequestMethod.POST, consumes = "application/json")
	public Person updatePerson(@RequestBody Person p) {
		System.out.println("KJHKDKHKHHK$$$$$$$$$$$$$$$$$$$$$$");
		person.setName(p.getName());
		return person;
	}
}
