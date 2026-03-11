package com.carlos.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.carlos.helpdesk.domain.Pessoa;
import com.carlos.helpdesk.domain.Tecnico;
import com.carlos.helpdesk.dtos.TecnicoDTO;
import com.carlos.helpdesk.repository.PessoaRepository;
import com.carlos.helpdesk.repository.TecnicoRepository;
import com.carlos.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.carlos.helpdesk.services.exceptions.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public Tecnico findById(Integer id) {
		Optional<Tecnico> tecnico = tecnicoRepository.findById(id);
		return tecnico.orElseThrow(() -> new ObjectNotFoundException("Tecnico não encontrado! id: " + id));
	}
	
	public List<Tecnico> findAll(){
		return tecnicoRepository.findAll();
	}
	
	public Tecnico create(TecnicoDTO tecnicoDTO) {
		tecnicoDTO.setId(null);
		tecnicoDTO.setSenha(encoder.encode(tecnicoDTO.getSenha()));
		validaPorCpfEEmail(tecnicoDTO);
		Tecnico tecnico = new Tecnico(tecnicoDTO);
		return tecnicoRepository.save(tecnico);
	}

	private void validaPorCpfEEmail(TecnicoDTO tecnicoDTO) {
		Optional<Pessoa> pessoa = pessoaRepository.findByCpf(tecnicoDTO.getCpf());
		if(pessoa.isPresent() && pessoa.get().getId() != tecnicoDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}
		
		pessoa = pessoaRepository.findByEmail(tecnicoDTO.getEmail());
		if(pessoa.isPresent() && pessoa.get().getId() != tecnicoDTO.getId()) {
			throw new DataIntegrityViolationException("Email já cadastrado no sistema!");		}
	}

	public Tecnico update(Integer id, @Valid TecnicoDTO tecnicoDTO) {
		tecnicoDTO.setId(id);
		Tecnico tecnico = findById(id);
		validaPorCpfEEmail(tecnicoDTO);
		tecnico = new Tecnico(tecnicoDTO);
		return tecnicoRepository.save(tecnico);
	}

	public void delete(Integer id) {
		Tecnico tecnico = findById(id);
		if(tecnico.getChamados().size() > 0 ) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
		}
		tecnicoRepository.deleteById(id);
		
	}
}
