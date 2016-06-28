package br.gov.inca.tabulador.domain.entity.regiao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.inca.tabulador.domain.entity.Entidade;

@Entity
@Table(name = Cidade.TABLE_NAME)
@SequenceGenerator(name = "SEQUENCE", sequenceName = "cidade_seq", allocationSize = 1)
public class Cidade implements Entidade<Integer> {
	public static final String TABLE_NAME = "cidade";

	private static final long serialVersionUID = -926457384777724122L;

	@Id
	@Column(name = "id_cidade")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "nm_cidade")
	private String nome;
	@Column(name = "fg_capital_estado")
	private Boolean capitalEstado;
	@Column(name = "fg_capital_rs")
	private Boolean capitalRegionalSaude;
	@ManyToOne
	@JoinColumn(name = "id_estado")
	private Estado estado;
	@ManyToOne
	@JoinColumn(name = "id_regional_saude")
	private RegionalSaude regionalSaude;

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

	public Boolean getCapitalEstado() {
		return capitalEstado;
	}

	public void setCapitalEstado(Boolean capitalEstado) {
		this.capitalEstado = capitalEstado;
	}

	public Boolean getCapitalRegionalSaude() {
		return capitalRegionalSaude;
	}

	public void setCapitalRegionalSaude(Boolean capitalRegionalSaude) {
		this.capitalRegionalSaude = capitalRegionalSaude;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public RegionalSaude getRegionalSaude() {
		return regionalSaude;
	}

	public void setRegionalSaude(RegionalSaude regionalSaude) {
		this.regionalSaude = regionalSaude;
	}
}
