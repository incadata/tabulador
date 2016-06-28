package br.gov.inca.tabulador.domain.cdi.producer;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer implements Serializable {
	private static final long serialVersionUID = 4343079253417767769L;

	private EntityManagerFactory factory;

	public EntityManagerProducer() {
		this("TabuladorWeb");
	}

	public EntityManagerProducer(String persistenceUnitName) {
		this.factory = Persistence.createEntityManagerFactory(persistenceUnitName);
	}

	@Produces
	@RequestScoped
	public EntityManager createEntityManager() {
		return factory.createEntityManager();
	}

	public void closeEntityManager(@Disposes EntityManager manager) {
		manager.close();
	}
}
