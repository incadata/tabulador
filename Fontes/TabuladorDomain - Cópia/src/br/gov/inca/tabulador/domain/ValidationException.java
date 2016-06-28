package br.gov.inca.tabulador.domain;

public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 4745613120902400696L;

	public ValidationException(String msg) {
		super(msg);
	}
}
