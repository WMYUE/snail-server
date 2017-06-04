package com.snail.fitment.common.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.springframework.stereotype.Component;

import com.snail.fitment.common.lang.StringUtils;

public class ClassUtils {
	public static String className2ComponentName(String className) {
		char[] cs = className.toCharArray();
		cs[0] += 32;
		return String.valueOf(cs);
	}

	public static List<String> getComponentsName(Class c, String packageName) {
		List<String> returnComponentsNameList = new ArrayList<>();
		List<Class> commands = ClassUtils.getAllClassByInterface(c, packageName);
		for (Class command : commands) {
			Component anotation = (Component) command.getAnnotation(Component.class);
			if (anotation != null) {
				if (StringUtils.isEmpty(anotation.value())) {
					returnComponentsNameList.add(className2ComponentName(command.getSimpleName()));
				} else {
					returnComponentsNameList.add(anotation.value());
				}
			}
		}
		return returnComponentsNameList;
	}

	public static List<Class> getAllClassByInterface(Class c, String packageName) {
		List<Class> returnClassList = new ArrayList<Class>();

		if (c.isInterface()) {
			if (packageName == null) {
				packageName = c.getPackage().getName();
			}
			try {
				List<Class> allClass = getClasses(packageName);
				for (int i = 0; i < allClass.size(); i++) {
					if (c.isAssignableFrom(allClass.get(i))) {
						if (!c.equals(allClass.get(i))) {
							returnClassList.add(allClass.get(i));
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnClassList;
	}

	public static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace(".", "/");

		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes;
	}

	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {

		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}

		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

}