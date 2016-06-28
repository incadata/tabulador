package br.gov.inca.tabulador.domain.entity.config;

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
@Table(name = ValorCampoConfig.TABLE_NAME)
@SequenceGenerator(name = "SEQUENCE", sequenceName = "valor_campo_config_seq", allocationSize = 1)
public class ValorCampoConfig implements Entidade<Integer> {
	public static final String TABLE_NAME = "valor_campo_config";

	private static final long serialVersionUID = 240439219702372392L;

	@Id
	@Column(name = "id_valor_campo_config")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "ds_valor_campo_config")
	private String descricao;
	@Column(name = "cd_valor_campo_config")
	private Integer codigo;
	@ManyToOne
	@JoinColumn(name = "id_campo_config")
	private CampoConfig campoConfig;

	public ValorCampoConfig() {
		setCampoConfig(new CampoConfig());
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public CampoConfig getCampoConfig() {
		return campoConfig;
	}
	
	public void setCampoConfig(CampoConfig campoConfig) {
		this.campoConfig = campoConfig;
	}
	
	public String getCodigoDescricao() {
		return String.format("%d - %s", getCodigo(), getDescricao());
	}
}
