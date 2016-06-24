package br.gov.inca.tabulador.web;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.inca.tabulador.domain.entity.Entidade;

public class EntityConverter implements Converter {
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value == null || value.isEmpty() || value.equalsIgnoreCase("null")) {
			return null;
		}
		return component.getAttributes().get(value);
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object obj) {
		if (obj != null && !"".equals(obj)) {
			String id = this.getId(obj);
			if (id == null) {
				id = "";
			}
			id = id.trim();

			component.getAttributes().put(id, getClazz(ctx, component, obj).cast(obj));
			return id;
		}
		return null;
	}

	/**
	 * Obtém, via expression language, a classe do objeto
	 * @param FacesContext facesContext
	 * @param UICompoment compoment
	 * @return Class<?>
	 */
	private Class<?> getClazz(FacesContext facesContext, UIComponent component, Object obj) {
		Class<?> clazz = component.getValueExpression("value").getType(
				facesContext.getELContext());
		if (Collection.class.isAssignableFrom(clazz)) {
			Type superclassType = clazz.getGenericSuperclass();
			if (superclassType != null && ParameterizedType.class.isAssignableFrom(superclassType.getClass()))
				return (Class<?>) ((ParameterizedType) superclassType).getActualTypeArguments()[0];
			else {
				return obj.getClass();
			}
		} else {
			return clazz;
		}
	}

	/**
	 * Retorna a representação em String do retorno do método anotado com @Id ou @EmbeddedId
	 * do objeto.
	 * @param Object obj
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getId(Object obj) {
		Object idValue = null;
		if (obj instanceof Entidade) {
			idValue = ((Entidade<Object>) obj).getId();
		}
		return String.valueOf(idValue);
	}

}
