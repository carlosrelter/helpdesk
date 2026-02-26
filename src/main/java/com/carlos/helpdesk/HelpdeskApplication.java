package com.carlos.helpdesk;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.carlos.helpdesk.domain.Chamado;
import com.carlos.helpdesk.domain.Cliente;
import com.carlos.helpdesk.domain.Tecnico;
import com.carlos.helpdesk.domian.enums.Perfil;
import com.carlos.helpdesk.domian.enums.Prioridade;
import com.carlos.helpdesk.domian.enums.Status;
import com.carlos.helpdesk.repository.ChamadoRepository;
import com.carlos.helpdesk.repository.ClienteRepository;
import com.carlos.helpdesk.repository.TecnicoRepository;

@SpringBootApplication
public class HelpdeskApplication implements CommandLineRunner{
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ChamadoRepository chamadoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(HelpdeskApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Tecnico tec1 = new Tecnico(null,"João Paulo","63653230268", "jaump@gmail.com", "123");
		tec1.addPerfil(Perfil.ADMIN);
		
		Cliente cli1 = new Cliente(null, "João Pedro","89854646662","jopedro@gmail.com", "123");
		
		Chamado c1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01","Primeiro chamado",tec1,cli1 );
		
		tecnicoRepository.saveAll(Arrays.asList(tec1));
		clienteRepository.saveAll(Arrays.asList(cli1));
		chamadoRepository.saveAll(Arrays.asList(c1));
	}

}
