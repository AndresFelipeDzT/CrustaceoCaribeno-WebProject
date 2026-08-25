package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class DemoApplication {

	private final Environment environment;

	public DemoApplication(Environment environment) {
		this.environment = environment;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		String port = environment.getProperty("local.server.port", environment.getProperty("server.port", "8080"));
		String contextPath = environment.getProperty("server.servlet.context-path", "");
		String baseUrl = "http://localhost:" + port + contextPath;

		System.out.println();
		System.out.println("================================================================");
		System.out.println("  >> CRUSTACEO CARIBENO - SERVIDOR INICIADO CON EXITO <<");
		System.out.println("================================================================");
		System.out.println("  [+] Estado: Listo para recibir peticiones");
		System.out.println("  [+] URL Principal: " + baseUrl + "/home");
		System.out.println("================================================================");
		System.out.println();
	}

}

