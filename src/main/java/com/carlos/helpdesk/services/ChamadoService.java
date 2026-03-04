package com.carlos.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlos.helpdesk.domain.Chamado;
import com.carlos.helpdesk.domain.Cliente;
import com.carlos.helpdesk.domain.Tecnico;
import com.carlos.helpdesk.domian.enums.Prioridade;
import com.carlos.helpdesk.domian.enums.Status;
import com.carlos.helpdesk.dtos.ChamadoDTO;
import com.carlos.helpdesk.repository.ChamadoRepository;
import com.carlos.helpdesk.services.exceptions.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class ChamadoService {
	
	@Autowired
	private ChamadoRepository chamadoRepository;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteService clienteService;
	
	public Chamado findById(Integer id) {
		Optional<Chamado> chamado = chamadoRepository.findById(id);
		return chamado.orElseThrow(() -> new ObjectNotFoundException("Chamado não encontrado! ID: " + id));
	}
	
	public List<Chamado> findAll(){
		return chamadoRepository.findAll();
	}

	public Chamado create(@Valid ChamadoDTO chamadoDTO) {
		return chamadoRepository.save(newChamado(chamadoDTO));
	}
	
	private Chamado newChamado(ChamadoDTO chamadoDTO) {
		
		Tecnico tecnico = tecnicoService.findById(chamadoDTO.getTecnico());
		Cliente cliente = clienteService.findById(chamadoDTO.getCliente());
		
		Chamado chamado = new Chamado();
		if(chamadoDTO.getId() != null) {
			chamado.setId(chamadoDTO.getId());
		}
		
		chamado.setTecnico(tecnico);
		chamado.setCliente(cliente);
		chamado.setPrioridade(Prioridade.toEnum(chamadoDTO.getPrioridade()));
		chamado.setStatus(Status.toEnum(chamadoDTO.getStatus()));
		chamado.setTitulo(chamadoDTO.getTitulo());
		chamado.setObservacoes(chamadoDTO.getObservacoes());		
		return chamado;
	}
}
