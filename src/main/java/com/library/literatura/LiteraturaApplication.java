package com.library.literatura;

import com.library.literatura.menu.Menu;
import com.library.literatura.repository.BookRepository;
import com.library.literatura.repository.PersonRepository;
import com.library.literatura.service.APIFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private PersonRepository personRepository;

	@Override
	public void run(String... args) throws Exception {
		Menu menu = new Menu(bookRepository, personRepository);
		menu.show();
	}
}
