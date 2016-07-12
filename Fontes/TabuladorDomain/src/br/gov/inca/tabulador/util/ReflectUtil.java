package br.gov.inca.tabulador.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

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
	
	private static Field getFieldId(Class<?> classe) {
        Field fld = null;
        for (Class<?> cls = classe; classe != null; classe = classe.getSuperclass()) {
            if (cls.isAnnotationPresent(Entity.class)) {
                for (int i = 0; i < cls.getDeclaredFields().length; i++) {
                    fld = cls.getDeclaredFields()[i];
                    boolean encontrou = false;
                    if (fld != null) {
                        for (int j = 0; j < fld.getDeclaredAnnotations().length; j++) {
                            Annotation ann = fld.getDeclaredAnnotations()[j];
                            if (ann.annotationType().equals(javax.persistence.Id.class) || ann.annotationType().equals(javax.persistence.EmbeddedId.class)) {
                                return fld;
                            } else {
                                fld = null;
                            }
                        }
                    }
                    if (encontrou) {
                        break;
                    }
                }
            } else {
            	for (Field f: cls.getDeclaredFields()) {
            		if (f.getName().equals("id")){
            			return f;
            		}
            			
            	}
            }
        }
        return fld;
    }
	
	private static Method getMethodId(Class<?> classe) {
        Method method = null;
        for (Class<?> cls = classe; classe != null; classe = classe.getSuperclass()) {
            if (cls.isAnnotationPresent(Entity.class)) {
                for (int i = 0; i < cls.getDeclaredMethods().length; i++) {
                    method = cls.getDeclaredMethods()[i];
                    boolean encontrou = false;
                    if (method != null) {
                        for (int j = 0; j < method.getDeclaredAnnotations().length; j++) {
                            Annotation ann = method.getDeclaredAnnotations()[j];
                            if (ann.annotationType().equals(javax.persistence.Id.class) || ann.annotationType().equals(javax.persistence.EmbeddedId.class)) {
                                encontrou = true;
                                break;
                            } else {
                                method = null;
                            }
                        }
                    }
                    if (encontrou) {
                        break;
                    }
                }
            } else {
            	for (Method m: cls.getDeclaredMethods()) {
            		if (m.getName().equalsIgnoreCase("getId"))
            			return m;
            	}
            }
        }
        return method;
    }
	
	public static Object getIdValue(Object obj) {
		Field field = getFieldId(obj.getClass());
		try {
			if (field != null){
				field.setAccessible(true);
					return field.get(obj);
			}
			
			Method method = getMethodId(obj.getClass());
			if (method != null)
				return method.invoke(obj);
			
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
