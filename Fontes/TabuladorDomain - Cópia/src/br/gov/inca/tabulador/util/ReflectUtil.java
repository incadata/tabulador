package br.gov.inca.tabulador.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtil {
	/**
	 * @param clazz Classe para pegar o parmetro do Generics.
	 * @return Parâmetro encontrado.
	 */
	public static Class<?> getGenericType(Class<?> clazz) {
		return getGenericTypes(clazz).get(0);
	}

	/**
	 * @param clazz Classe para pegar o parmetro do Generics.
	 * @return Parâmetros encontrados.
	 */
	public static List<Class<?>> getGenericTypes(Class<?> clazz) {
		final Type[] actualTypeArguments = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
		final List<Class<?>> arrayList = new ArrayList<>(actualTypeArguments.length);
		for (Type type : actualTypeArguments) {
			if (type != null) {
				arrayList.add((Class<?>) type);
			}
		}
		return arrayList;
	}
}
