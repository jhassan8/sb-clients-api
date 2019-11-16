package com.clients.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clients.models.entity.Client;

public interface IClientDao extends JpaRepository<Client, Long> {

}
