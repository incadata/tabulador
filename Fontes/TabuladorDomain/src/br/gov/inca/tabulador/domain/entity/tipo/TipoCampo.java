package br.gov.inca.tabulador.domain.entity.tipo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_campo")
@SequenceGenerator(name = "SEQUENCE", sequenceName = "tipo_campo_seq", allocationSize = 1)
public class TipoCampo implements Serializable {
	private static final long serialVersionUID = 9137246248390452907L;
	@Id
	@Column(name = "id_tipo_campo")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "nm_tipo_campo")
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
