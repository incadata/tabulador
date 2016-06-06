package br.gov.inca.tabulador.domain.entity.regiao;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "estado")
@SequenceGenerator(name = "SEQUENCE", sequenceName = "estado_seq", allocationSize = 1)
public class Estado implements Serializable {
	private static final long serialVersionUID = 777614608188750669L;

	@Id
	@Column(name = "id_estado")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "nm_estado")
	private String nome;
	@Column(name = "sg_estado")
	private String sigla;
	@ManyToOne
	@JoinColumn(name = "id_regional_saude")
	private RegionalSaude regionalSaude;
	@OneToMany(mappedBy = "estado")
	private Set<Regiao> regioes;

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

	public RegionalSaude getRegionalSaude() {
		return regionalSaude;
	}

	public void setRegionalSaude(RegionalSaude regionalSaude) {
		this.regionalSaude = regionalSaude;
	}

	public Set<Regiao> getRegioes() {
		return regioes;
	}

	public void setRegioes(Set<Regiao> regioes) {
		this.regioes = regioes;
	}
}
