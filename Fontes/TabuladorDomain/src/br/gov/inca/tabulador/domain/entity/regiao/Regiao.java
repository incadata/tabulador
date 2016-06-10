package br.gov.inca.tabulador.domain.entity.regiao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = Regiao.TABLE_NAME)
@SequenceGenerator(name = "SEQUENCE", sequenceName = "regiao_seq", allocationSize = 1)
public class Regiao implements Serializable {
	public static final String TABLE_NAME = "regiao";

	private static final long serialVersionUID = 1226307521064997275L;

	@Id
	@Column(name = "id_regiao")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "nm_regiao")
	private String nome;
	@Column(name = "sg_regiao")
	private String sigla;
	@ManyToOne
	@JoinColumn(name = "id_estado")
	private Estado estado;

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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
