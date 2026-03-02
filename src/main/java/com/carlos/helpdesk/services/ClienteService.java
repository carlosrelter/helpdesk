package com.carlos.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlos.helpdesk.domain.Pessoa;
import com.carlos.helpdesk.domain.Cliente;
import com.carlos.helpdesk.dtos.ClienteDTO;
import com.carlos.helpdesk.repository.PessoaRepository;
import com.carlos.helpdesk.repository.ClienteRepository;
import com.carlos.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.carlos.helpdesk.services.exceptions.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Cliente findById(Integer id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado! id: " + id));
	}
	
	public List<Cliente> findAll(){
		return clienteRepository.findAll();
	}
	
	public Cliente create(ClienteDTO clienteDTO) {
		clienteDTO.setId(null);
		validaPorCpfEEmail(clienteDTO);
		Cliente Cliente = new Cliente(clienteDTO);
		return clienteRepository.save(Cliente);
	}

	private void validaPorCpfEEmail(ClienteDTO clienteDTO) {
		Optional<Pessoa> pessoa = pessoaRepository.findByCpf(clienteDTO.getCpf());
		if(pessoa.isPresent() && pessoa.get().getId() != clienteDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}
		
		pessoa = pessoaRepository.findByEmail(clienteDTO.getEmail());
		if(pessoa.isPresent() && pessoa.get().getId() != clienteDTO.getId()) {
			throw new DataIntegrityViolationException("Email já cadastrado no sistema!");		}
	}

	public Cliente update(Integer id, @Valid ClienteDTO clienteDTO) {
		clienteDTO.setId(id);
		Cliente cliente = findById(id);
		validaPorCpfEEmail(clienteDTO);
		cliente = new Cliente(clienteDTO);
		return clienteRepository.save(cliente);
	}

	public void delete(Integer id) {
		Cliente cliente = findById(id);
		if(cliente.getChamados().size() > 0 ) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
		}
		clienteRepository.deleteById(id);
		
	}
}
