package com.carlos.helpdesk.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.carlos.helpdesk.domain.Pessoa;
import com.carlos.helpdesk.repository.PessoaRepository;
import com.carlos.helpdesk.security.UserSS;

public class UserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Pessoa> user  = pessoaRepository.findByEmail(email);
		if(user.isPresent()) {
			return new UserSS(user.get().getId(), user.get().getEmail(), user.get().getSenha(), user.get().getPerfis());
		}
		throw new UsernameNotFoundException(email);
	}

}
