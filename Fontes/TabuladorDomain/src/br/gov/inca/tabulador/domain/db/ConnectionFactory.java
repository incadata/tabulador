package br.gov.inca.tabulador.domain.db;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionFactory implements Serializable {
	private static final long serialVersionUID = 1846599951727624729L;

	public Connection createConnctionJDNI(String resource) throws SQLException, NamingException {
		return ((DataSource) new InitialContext().lookup(resource)).getConnection();
	}

	public Connection createConnection(String arqProp) throws Exception {
		final Properties properties = new Properties();
		final String path = ConnectionFactory.class.getResource("/").getPath();
		/*
		 * conexao feita através do arquivo de properties para facilitar
		 * modificação de conexao
		 */
		properties.load(new FileInputStream(path.replaceAll("%20", " ") + arqProp));
		
		// Registrado o driver, vamos estabelecer uma conexão
		return createConnection(properties.getProperty("driver"), properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
	}
	
	public Connection createConnection(String driver, String url, String username, String password) throws Exception {
		// registra um driver
		Class.forName(driver);
		
		// Registrado o driver, vamos estabelecer uma conexão
		return DriverManager.getConnection(url, username, password);
	}
}
