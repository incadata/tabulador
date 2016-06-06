package br.gov.inca.tabulador.web;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "helloWorld", eager = true)
public class HelloWorld {
	public HelloWorld() {
		System.out.println("HelloWorld started!");
	}

	public String getMessage() {
		return "Hello World!";
	}
}
