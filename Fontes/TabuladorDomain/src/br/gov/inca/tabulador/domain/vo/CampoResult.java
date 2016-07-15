package br.gov.inca.tabulador.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Transient;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;

public class CampoResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4743440458811058572L;
	
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
		setId(campo.getId());
		setNome(campo.getNome());
		setLabel(campo.getLabel());
		setAbreviado(campo.getAbreviado());
		setTipoCampo(campo.getTipoCampo());
		setValores(new ArrayList<>(campo.getValores()));
	}
	
	public CampoResult(CampoConfig campo, Object valor) {
		setId(campo.getId());
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
		return getClass().hashCode() + getId().hashCode();
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

	@Transient
	public String getDescricaoValor(Object valor) {
		if (valores != null && !valores.isEmpty()){
			Optional<ValorCampoConfig> opValor = valores.stream().filter(x -> x.getCodigo().equals(valor)).findFirst();
			if (opValor.isPresent())
				return opValor.get().getDescricao();
		} 
			return valor.toString();
	}
}
