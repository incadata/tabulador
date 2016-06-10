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

import org.hibernate.validator.constraints.NotEmpty;

import br.gov.inca.tabulador.domain.entity.Entidade;

@Entity
@Table(name = RegionalSaude.TABLE_NAME)
@SequenceGenerator(name = "SEQUENCE", sequenceName = "regional_saude_seq", allocationSize = 1)
public class RegionalSaude implements Serializable, Entidade<Integer> {
	public static final String TABLE_NAME = "regional_saude";

	private static final long serialVersionUID = -6826160985492396491L;

	@Id
	@Column(name = "id_regional_saude")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "nm_regional_saude")
	private @NotEmpty(message = "Nome não pode estar vazio") String nome;
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

	public Estado getEstado() {
		return estado;
	}
	
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
