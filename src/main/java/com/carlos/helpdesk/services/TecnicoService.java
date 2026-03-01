package com.carlos.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlos.helpdesk.domain.Tecnico;
import com.carlos.helpdesk.repository.TecnicoRepository;
import com.carlos.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	public Tecnico findById(Integer id) {
		Optional<Tecnico> tecnico = tecnicoRepository.findById(id);
		return tecnico.orElseThrow(() -> new ObjectNotFoundException("Tecnico não encontrado! id: " + id));
	}
	
	public List<Tecnico> findAll(){
		return tecnicoRepository.findAll();
	}
}
