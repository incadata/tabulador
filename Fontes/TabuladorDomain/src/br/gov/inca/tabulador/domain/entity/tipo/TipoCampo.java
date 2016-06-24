package br.gov.inca.tabulador.domain.entity.tipo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.inca.tabulador.domain.entity.EntidadeAbstract;

@Entity
@Table(name = TipoCampo.TABLE_NAME)
@SequenceGenerator(name = "SEQUENCE", sequenceName = "tipo_campo_seq", allocationSize = 1)
public class TipoCampo extends EntidadeAbstract<Integer> {
	public static final String TABLE_NAME = "tipo_campo";

	private static final long serialVersionUID = 9137246248390452907L;

	public static final int TIPO_INTEIRO = 1;
	public static final int TIPO_TEXTO = 2;
	public static final int TIPO_DATA = 3;

	@Id
	@Column(name = "id_tipo_campo")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	private Integer id;
	@Column(name = "ds_tipo_campo")
	private String nome;

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
	
	public boolean isData() {
		return getId() != null && TIPO_DATA == getId();
	}
	
}
