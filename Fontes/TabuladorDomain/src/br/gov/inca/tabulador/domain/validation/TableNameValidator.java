package br.gov.inca.tabulador.domain.validation;

import java.util.Collection;
import java.util.HashSet;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.domain.entity.regiao.Cidade;
import br.gov.inca.tabulador.domain.entity.regiao.Estado;
import br.gov.inca.tabulador.domain.entity.regiao.Regiao;
import br.gov.inca.tabulador.domain.entity.regiao.RegionalSaude;
import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;
import br.gov.inca.tabulador.domain.entity.tipo.TipoFiltro;

public class TableNameValidator implements
		ConstraintValidator<TableName, String> {
	private static final Collection<String> INVALID_TABLE_NAMES = new HashSet<>();
	
	static {
		INVALID_TABLE_NAMES.add(CampoConfig.TABLE_NAME);
		INVALID_TABLE_NAMES.add(TabelaConfig.TABLE_NAME);
		INVALID_TABLE_NAMES.add(ValorCampoConfig.TABLE_NAME);
		INVALID_TABLE_NAMES.add(TipoFiltro.TABLE_NAME);
		INVALID_TABLE_NAMES.add(TipoCampo.TABLE_NAME);
		INVALID_TABLE_NAMES.add(RegionalSaude.TABLE_NAME);
		INVALID_TABLE_NAMES.add(Regiao.TABLE_NAME);
		INVALID_TABLE_NAMES.add(Estado.TABLE_NAME);
		INVALID_TABLE_NAMES.add(Cidade.TABLE_NAME);
	}
	
	@Override
	public void initialize(final TableName annotation) {
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext ctx) {
		return !INVALID_TABLE_NAMES.contains(value);
	}

}
