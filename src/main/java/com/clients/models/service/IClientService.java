package com.clients.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.clients.models.entity.Client;
import com.clients.models.entity.District;

public interface IClientService {

	public List<Client> findAll();

	public Page<Client> findAll(Pageable pageable);

	public Client save(Client client);

	public void delete(Long id);

	public Client findById(Long id);

	public List<District> findAllDistricts();

}
