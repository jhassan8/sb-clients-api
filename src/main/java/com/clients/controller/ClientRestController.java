package com.clients.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public Client getClient(@PathVariable Long id) {
		return this.iClientService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Client saveClient(@RequestBody Client client) {
		return this.iClientService.save(client);
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Client updateClient(@RequestBody Client client, @PathVariable Long id) {

		Client currentClient = this.iClientService.findById(id);

		currentClient.setEmail(client.getEmail());
		currentClient.setName(client.getName());
		currentClient.setSurname(client.getSurname());

		return this.iClientService.save(currentClient);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteClient(@PathVariable Long id) {
		this.iClientService.delete(id);
	}
}
