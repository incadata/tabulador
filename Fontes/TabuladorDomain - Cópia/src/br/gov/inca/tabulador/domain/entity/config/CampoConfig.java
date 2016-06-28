package br.gov.inca.tabulador.domain.entity.config;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import br.gov.inca.tabulador.domain.entity.EntidadeAbstract;
import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;
import br.gov.inca.tabulador.domain.entity.tipo.TipoFiltro;

@Entity
@Table(name = CampoConfig.TABLE_NAME)
@SequenceGenerator(name = "SEQUENCE", sequenceName = "campo_config_seq", allocationSize = 1)
public class CampoConfig extends EntidadeAbstract<Integer> {
	public static final String TABLE_NAME = "campo_config";

	private static final long serialVersionUID = -9047437387992306550L;

	@Id
	@Column(name = "id_campo_config")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "nm_campo_config")
	@NotEmpty @Pattern(regexp = "[\\w_]+", message = "Nome de campo não é válido")
	private String nome;
	@Column(name = "nm_label")
	private String label;
	@Column(name = "nm_abreviado")
	private String abreviado;
	@ManyToOne
	@JoinColumn(name = "id_tipo_filtro")
	private TipoFiltro tipoFiltro;
	@ManyToOne
	@JoinColumn(name = "id_tipo_campo")
	@NotNull
	private TipoCampo tipoCampo;
	@ManyToOne
	@JoinColumn(name = "id_tabela_config")
	private TabelaConfig tabelaConfig;
	@OneToMany(mappedBy = "campoConfig", cascade = CascadeType.ALL)
	private List<ValorCampoConfig> valores;

	public CampoConfig() {
		setTipoFiltro(new TipoFiltro());
		setTipoCampo(new TipoCampo());
		setTabelaConfig(new TabelaConfig());
		setValores(new ArrayList<>());
	}

	public CampoConfig(CampoConfig campo) {
		setId(campo.getId());
		setNome(campo.getNome());
		setLabel(campo.getLabel());
		setAbreviado(campo.getAbreviado());
		setTipoFiltro(campo.getTipoFiltro());
		setTipoCampo(campo.getTipoCampo());
		setTabelaConfig(campo.getTabelaConfig());
		setValores(new ArrayList<>(campo.getValores()));
	}

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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isFiltro() {
		return getTipoFiltro() != null && getTipoFiltro().getId() != null;
	}

	public String getAbreviado() {
		return abreviado;
	}

	public void setAbreviado(String abreviado) {
		this.abreviado = abreviado;
	}

	public TipoFiltro getTipoFiltro() {
		return tipoFiltro;
	}

	public void setTipoFiltro(TipoFiltro tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}

	public TipoCampo getTipoCampo() {
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampo tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	public TabelaConfig getTabelaConfig() {
		return tabelaConfig;
	}

	public void setTabelaConfig(TabelaConfig tabelaConfig) {
		this.tabelaConfig = tabelaConfig;
	}

	public List<ValorCampoConfig> getValores() {
		return valores;
	}

	public void setValores(List<ValorCampoConfig> valores) {
		this.valores = valores;
	}

	public String getLabelOrNome() {
		return getLabel() != null && !getLabel().isEmpty() ? getLabel() : getNome();
	}

}
