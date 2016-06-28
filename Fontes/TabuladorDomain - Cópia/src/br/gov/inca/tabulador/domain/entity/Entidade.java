package br.gov.inca.tabulador.domain.entity;

import java.io.Serializable;

public interface Entidade<T> extends Serializable {
	T getId();
}
