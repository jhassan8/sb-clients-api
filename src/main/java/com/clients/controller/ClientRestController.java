package com.clients.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
