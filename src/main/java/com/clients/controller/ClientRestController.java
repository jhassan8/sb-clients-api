package com.clients.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clients.models.entity.Client;
import com.clients.models.service.IClientService;

@RestController
@RequestMapping("api/clients")
public class ClientRestController {

	private IClientService iClientService;

	@Autowired
	public ClientRestController(IClientService iClientService) {
		this.iClientService = iClientService;
	}

	@GetMapping
	public List<Client> getClients() {
		return this.iClientService.findAll();
	}

	@GetMapping("id/{id}")
	public ResponseEntity<?> getClient(@PathVariable Long id) {
		
		Client client = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			client = this.iClientService.findById(id);
		} catch (DataAccessException e) {
			response.put("message", "a error ocurred in db..");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(client == null) {
			response.put("message", "the client doent exist.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Client>(client, HttpStatus.OK) ;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveClient(@RequestBody Client client) {
		
		Client clientNew = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			clientNew = this.iClientService.save(client);
		} catch (DataAccessException e) {
			response.put("message", "a error ocurred on insert db..");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "the client has been created correcly.");
		response.put("client", clientNew);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED) ;
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updateClient(@RequestBody Client client, @PathVariable Long id) {

		Client currentClient = null;
		Map<String, Object> response = new HashMap<>();
	
		currentClient =	this.iClientService.findById(id);

		if(currentClient == null) {
			response.put("message", "the client doent exist.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			currentClient.setEmail(client.getEmail());
			currentClient.setName(client.getName());
			currentClient.setSurname(client.getSurname());
			
			currentClient = this.iClientService.save(currentClient);
		} catch (DataAccessException e) {
			response.put("message", "a error ocurred on insert db..");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "the client has been updated correcly.");
		response.put("client", currentClient);
		
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED) ;
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> deleteClient(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();

		try {
			this.iClientService.delete(id);
		} catch (DataAccessException e) {
			response.put("message", "a error ocurred on insert db..");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("message", "the client has been deleted correcly.");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK) ;
	}
}
