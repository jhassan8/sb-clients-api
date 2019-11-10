package com.clients.models.service;

import java.util.List;

import com.clients.models.entity.Client;

public interface IClientService {

	public List<Client> findAll();

	public Client save(Client client);

	public void delete(Long id);

	public Client findById(Long id);

}
