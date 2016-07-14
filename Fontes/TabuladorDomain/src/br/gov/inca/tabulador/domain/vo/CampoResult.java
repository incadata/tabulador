package br.gov.inca.tabulador.domain.vo;

import java.util.ArrayList;
import java.util.List;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;

public class CampoResult {

	private Integer id;
	private String nome;
	private String label;
	private String abreviado;
	private TipoCampo tipoCampo;
	private ValorCampoConfig valorConfig;
	private Object value; 
	private List<ValorCampoConfig> valores;

	public CampoResult() {
		setTipoCampo(new TipoCampo());
		setValores(new ArrayList<>());
	}

	public CampoResult(CampoConfig campo) {
		setNome(campo.getNome());
		setLabel(campo.getLabel());
		setAbreviado(campo.getAbreviado());
		setTipoCampo(campo.getTipoCampo());
		setValores(new ArrayList<>(campo.getValores()));
	}
	
	public CampoResult(CampoConfig campo, Object valor) {
		setNome(campo.getNome());
		setLabel(campo.getLabel());
		setAbreviado(campo.getAbreviado());
		setTipoCampo(campo.getTipoCampo());
		setValores(new ArrayList<>(campo.getValores()));
		if (getValores().isEmpty())
		setValorConfig(getValores().stream().filter(x -> x.getCodigo().equals(valor)).findFirst().get());
		
		this.value = valor;
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


	public String getAbreviado() {
		return abreviado;
	}

	public void setAbreviado(String abreviado) {
		this.abreviado = abreviado;
	}

	public TipoCampo getTipoCampo() {
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampo tipoCampo) {
		this.tipoCampo = tipoCampo;
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

	public ValorCampoConfig getValorConfig() {
		return valorConfig;
	}

	public void setValorConfig(ValorCampoConfig valor) {
		this.valorConfig = valor;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampoResult other = (CampoResult) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
