package br.gov.inca.tabulador.domain.entity.regiao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "regional_saude")
@SequenceGenerator(name = "SEQUENCE", sequenceName = "regional_saude_seq", allocationSize = 1)
public class RegionalSaude implements Serializable {
	private static final long serialVersionUID = -6826160985492396491L;

	@Id
	@Column(name = "id_regional_saude")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "nm_regional_saude")
	private @NotEmpty(message = "Nome não pode estar vazio") String nome;

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
