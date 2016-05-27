package com.architecture.specification.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.architecture.specification.library.parser.classutil.AbstractClassFilter;
import com.architecture.specification.library.parser.classutil.AndClassFilter;
import com.architecture.specification.library.parser.classutil.ClassFilter;
import com.architecture.specification.library.parser.classutil.ClassFinder;
import com.architecture.specification.library.parser.classutil.ClassInfo;
import com.architecture.specification.library.parser.classutil.EnumClassFilter;
import com.architecture.specification.library.parser.classutil.InnerClassFilter;
import com.architecture.specification.library.parser.classutil.InterfaceClassFilter;
import com.architecture.specification.library.parser.classutil.NotClassFilter;
import com.architecture.specification.library.parser.classutil.PublicClassFilter;

public class ArchitecturalModelParser {

	ArrayList<File> classFilesRelevantToArchitecture;
	ArchitecturalModel intendedArchitecturalModel;

	public ArchitecturalModelParser(String classFilesDirectory, ArchitecturalModel intendedArchitecturalModel)
			throws IOException {
		classFilesRelevantToArchitecture = new ArrayList<File>();
		this.intendedArchitecturalModel = intendedArchitecturalModel;

		prepareModelParser(classFilesDirectory);

	}

	private void prepareModelParser(String classFilesDirectory) throws IOException {
		ClassFinder finder = new ClassFinder();
		finder.add(new File(classFilesDirectory));

		ClassFilter filter = new AndClassFilter(
				// Must not be an interface
				new NotClassFilter(new InterfaceClassFilter()),

		// Must not be an enum
				new NotClassFilter(new EnumClassFilter()),

		// Must not be abstract
				new NotClassFilter(new AbstractClassFilter()),

		// Must not be an inner class
				new NotClassFilter(new InnerClassFilter()),

		// Must be public
				new PublicClassFilter());

		Collection<ClassInfo> filteredClasses = new ArrayList<ClassInfo>();
		finder.findClasses(filteredClasses, filter);

		for (ClassInfo classInfo : filteredClasses) {
			System.out.println("Found " + classInfo.getClassName());

			Map<String, ClassInfo> sClasses = new HashMap<String, ClassInfo>();
			finder.findAllSuperClasses(classInfo, sClasses);
			System.out.println("Here are your superclasses");
			for (String s : sClasses.keySet()) {
				System.out.println("super " + s);
			}
		}

	}

	public void verifyAgainstImplementation() {

		HashMap<String, HashSet<ArchitecturalComponent>> classComponentsMap = intendedArchitecturalModel
				.getClassComponentsMap();

	}

}
