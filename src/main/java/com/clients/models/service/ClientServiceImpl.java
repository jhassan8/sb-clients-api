package com.clients.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clients.models.dao.IClientDao;
import com.clients.models.entity.Client;

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

}
