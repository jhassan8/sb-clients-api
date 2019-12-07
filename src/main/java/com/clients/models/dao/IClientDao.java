package com.clients.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.clients.models.entity.Client;
import com.clients.models.entity.District;

public interface IClientDao extends JpaRepository<Client, Long> {
	
	@Query("from District")
	public List<District> findAllDistricts();

}
