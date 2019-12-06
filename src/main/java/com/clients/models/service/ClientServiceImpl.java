package com.clients.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clients.models.dao.IClientDao;
import com.clients.models.entity.Client;
import com.clients.models.entity.District;

@Service
public class ClientServiceImpl implements IClientService {

	private IClientDao iClientDao;

	@Autowired
	public ClientServiceImpl(IClientDao iClientDao) {
		this.iClientDao = iClientDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return (List<Client>) this.iClientDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Client> findAll(Pageable pageable) {
		return this.iClientDao.findAll(pageable);
	}

	@Override
	@Transactional
	public Client save(Client client) {
		return this.iClientDao.save(client);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.iClientDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Client findById(Long id) {
		return this.iClientDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<District> findAllDistricts() {
		return this.iClientDao.findAllDistricts();
	}

}
