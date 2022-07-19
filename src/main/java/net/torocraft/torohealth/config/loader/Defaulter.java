package net.torocraft.torohealth.config.loader;

import java.lang.reflect.Field;

public class Defaulter {
	private static boolean hasDefaultConstructor(Class<?> clazz) {
		try {
			clazz.getDeclaredConstructor();

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void setDefaults(Object o, Class<?> clazz) {
		try {
			Object defaults = clazz.getDeclaredConstructor().newInstance();
			Field[] fields = clazz.getFields();

			for (Field field : fields) {
				if (field.get(o) == null) {
					field.set(o, field.get(defaults));
				}

				if (hasDefaultConstructor(field.getType())) {
					setDefaults(field.get(o), field.getType());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
