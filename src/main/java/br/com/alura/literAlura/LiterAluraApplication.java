package br.com.alura.literAlura;

import br.com.alura.literAlura.controller.Principal;
import br.com.alura.literAlura.repository.iAutorRepository;
import br.com.alura.literAlura.repository.iLivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	@Autowired
	private iLivroRepository livrosRepositorio;

	@Autowired
	private iAutorRepository autorRepositorio;



	public static void main(String[] args) {

		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(livrosRepositorio, autorRepositorio);
		principal.exibeMenu();

	}
}
