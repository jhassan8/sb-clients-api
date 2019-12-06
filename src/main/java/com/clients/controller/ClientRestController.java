package com.clients.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clients.models.entity.Client;
import com.clients.models.entity.District;
import com.clients.models.service.IClientService;
import com.clients.models.service.IUploadFileService;

@RestController
@RequestMapping("api/clients")
public class ClientRestController {

	private IClientService iClientService;
	private IUploadFileService iUploadFileService;

	@Autowired
	public ClientRestController(IClientService iClientService, IUploadFileService iUploadFileService) {
		this.iClientService = iClientService;
		this.iUploadFileService = iUploadFileService;
	}

	@GetMapping
	public List<Client> getClients() {
		return this.iClientService.findAll();
	}

	@GetMapping("page/{page}")
	public Page<Client> getClients(@PathVariable Integer page) {
		return this.iClientService.findAll(PageRequest.of(page, 5));
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

		if (client == null) {
			response.put("message", "the client doent exist.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveClient(@Valid @RequestBody Client client, BindingResult result) {

		Client clientNew = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			response.put("errors", result.getFieldErrors().stream()
					.map(e -> "Field '" + e.getField() + "' " + e.getDefaultMessage()).collect(Collectors.toList()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			clientNew = this.iClientService.save(client);
		} catch (DataAccessException e) {
			response.put("message", "a error ocurred on insert db..");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("message", "the client has been created correcly.");
		response.put("client", clientNew);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> updateClient(@Valid @RequestBody Client client, @PathVariable Long id,
			BindingResult result) {

		Client currentClient = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			response.put("errors", result.getFieldErrors().stream()
					.map(e -> "Field '" + e.getField() + "' " + e.getDefaultMessage()).collect(Collectors.toList()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		currentClient = this.iClientService.findById(id);

		if (currentClient == null) {
			response.put("message", "the client doent exist.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			currentClient.setEmail(client.getEmail());
			currentClient.setName(client.getName());
			currentClient.setSurname(client.getSurname());
			currentClient.setDistrict(client.getDistrict());

			currentClient = this.iClientService.save(currentClient);
		} catch (DataAccessException e) {
			response.put("message", "a error ocurred on insert db..");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("message", "the client has been updated correcly.");
		response.put("client", currentClient);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> deleteClient(@PathVariable Long id) {

		Client client = iClientService.findById(id);
		Map<String, Object> response = new HashMap<>();

		// remove file if exist
		this.iUploadFileService.delete(client.getAvatar());

		try {
			this.iClientService.delete(id);
		} catch (DataAccessException e) {
			response.put("message", "a error ocurred on insert db..");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("message", "the client has been deleted correcly.");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping("upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();

		Client client = iClientService.findById(id);
		String name = null;
		if (!file.isEmpty()) {
			try {
				name = this.iUploadFileService.save(file);
			} catch (IOException e) {
				response.put("message", "a error ocurred on insert db..");
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// remove old file if exist
			this.iUploadFileService.delete(client.getAvatar());

			client.setAvatar(name);

			iClientService.save(client);

			response.put("message", "the client has been updated correcly.");
			response.put("client", client);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@GetMapping("/img/{avatar:.+}")
	public ResponseEntity<Resource> viewAvatar(@PathVariable String avatar) {

		Resource resouce = null;

		try {
			resouce = this.iUploadFileService.load(avatar);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + avatar + "\"");

		return new ResponseEntity<Resource>(resouce, headers, HttpStatus.OK);
	}
	
	@GetMapping("/districts")
	public List<District> getDistricts() {
		return this.iClientService.findAllDistricts();
	}


}
