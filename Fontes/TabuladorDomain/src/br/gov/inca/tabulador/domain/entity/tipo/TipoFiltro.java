package br.gov.inca.tabulador.domain.entity.tipo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.inca.tabulador.domain.entity.Entidade;

@Entity
@Table(name = TipoFiltro.TABLE_NAME)
@SequenceGenerator(name = "SEQUENCE", sequenceName = "tipo_filtro_seq", allocationSize = 1)
public class TipoFiltro implements Serializable, Entidade<Integer> {
	public static final String TABLE_NAME = "tipo_filtro";

	private static final long serialVersionUID = 643158529234596693L;

	@Id
	@Column(name = "id_tipo_filtro")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "nm_tipo_filtro")
	private String nome;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
