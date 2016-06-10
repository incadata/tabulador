package br.gov.inca.tabulador.domain.entity;

import java.io.Serializable;

public interface Entidade<K> extends Serializable {
	K getId();
}
