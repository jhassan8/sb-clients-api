package com.clients.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.clients.models.entity.Client;

public interface IClientDao extends CrudRepository<Client, Long> {

}
