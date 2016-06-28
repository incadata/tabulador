package br.gov.inca.tabulador.domain.dao.regiao;

import javax.enterprise.context.Dependent;

import br.gov.inca.tabulador.domain.db.DaoAbstract;
import br.gov.inca.tabulador.domain.entity.regiao.RegionalSaude;

@Dependent
public class RegionalSaudeDao extends DaoAbstract<RegionalSaude, Integer> {
	private static final long serialVersionUID = -8326821370283407075L;
}
