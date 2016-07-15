package br.gov.inca.tabulador.web.view;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.web.bean.ViewBean;

@Named
@ViewScoped
public class IndexView implements ViewBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1865360930890923944L;
	
	private MenuModel model;
	
	private @Inject TabelaConfigDao tabelaConfigDao;
	
	@PostConstruct
	private void init(){
		model = new DefaultMenuModel();
		
		model.addElement(new DefaultMenuItem("Configuração", "", "pages/tabelaconfig/"));
		DefaultSubMenu subMenuTabulador = new DefaultSubMenu("Tabulador");
		DefaultSubMenu subMenuConsulta = new DefaultSubMenu("Consulta Dinâmica");
		
		for (TabelaConfig tabelaConfig: tabelaConfigDao.findAll().getCollection()){
			subMenuTabulador.addElement(new DefaultMenuItem(tabelaConfig.getNome(), "", "pages/tabelaconfig/tabular.xhtml?entity.id=" + tabelaConfig.getId()));
			subMenuConsulta.addElement(new DefaultMenuItem(tabelaConfig.getNome(), "", "pages/tabelaconfig/consultaDinamica.xhtml?entity.id=" + tabelaConfig.getId()));
		}
		
		model.addElement(subMenuTabulador);
		model.addElement(subMenuConsulta);
	}

	public MenuModel getModel() {
		return model;
	}

}
