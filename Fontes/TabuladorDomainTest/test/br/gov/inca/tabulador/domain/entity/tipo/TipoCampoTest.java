package br.gov.inca.tabulador.domain.entity.tipo;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Direitos Autorais Reservados (c) 2016 Instituto Nacional de Câncer
 * 
 * Este programa é software livre; você pode redistribuí-lo
 * e/ou modificá-lo sob os termos da Licença Pública Geral
 * GNU conforme publicada pela Free Software Foundation;
 * 
 * Este programa é distribuédo na expectativa de que seja útil,
 * porém, SEM NENHUMA GARANTIA; nem mesmo a garantia implícita
 * de COMERCIABILIDADE OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA.
 * 
 * Consulte a Licença Pública Geral do GNU para mais detalhes.
 * Você deve ter recebido uma cópia da Licença Pública Geral do
 * GNU junto com este programa; se não, escreva para a
 * Free Software Foundation, Inc., no endereço:
 * 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */
public class TipoCampoTest extends TipoCampo {
	private static final long serialVersionUID = -3779772160023565140L;

	@Test
	public void isDataNull() {
		setId(null);
		Assert.assertFalse(isData());
	}

	@Test
	public void isDataFalse() {
		setId(0);
		Assert.assertFalse(isData());

		setId(TipoCampo.TIPO_DATA + 1);
		Assert.assertFalse(isData());

		setId(TipoCampo.TIPO_INTEIRO);
		Assert.assertFalse(isData());

		setId(TipoCampo.TIPO_TEXTO);
		Assert.assertFalse(isData());
	}

	@Test
	public void isDataTrue() {
		setId(TipoCampo.TIPO_DATA);
		Assert.assertTrue(isData());
	}

}
