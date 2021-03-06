package br.gov.inca.tabulador.web.factory;

import java.io.Serializable;
import java.sql.Connection;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import br.gov.inca.tabulador.domain.db.ConnectionFactory;

public class ConnectionCreator implements Serializable {
	private static final long serialVersionUID = -5979481488736391253L;

	private @Inject ConnectionFactory connectionFactory;

	@Produces
	public Connection createConnection() throws Exception {
		// TODO Melhorar a obteção da conexão
		return getConnectionFactory().createConnection("org.postgresql.Driver",
				"jdbc:postgresql://it-des14:5433/tabulador",
				"postgres",
				"postgres");
	}

	private ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}
}
