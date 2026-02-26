package com.carlos.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carlos.helpdesk.domain.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer>{

}
