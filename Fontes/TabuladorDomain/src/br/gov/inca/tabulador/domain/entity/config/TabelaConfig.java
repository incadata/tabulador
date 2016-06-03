package br.gov.inca.tabulador.domain.entity.config;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tabela_config")
@SequenceGenerator(name = "SEQUENCE", sequenceName = "tabela_config_seq", allocationSize = 1)
public class TabelaConfig {
	@Id
	@Column(name = "id_tabela_config")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "nm_tabela_config")
	private String nome;
	@Column(name = "ds_titulo")
	private String titulo;
	@Column(name = "fg_localidade")
	private Boolean localidade;
	@OneToMany(mappedBy="tabelaConfig")
	private List<CampoConfig> campos;

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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Boolean getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Boolean localidade) {
		this.localidade = localidade;
	}
}
