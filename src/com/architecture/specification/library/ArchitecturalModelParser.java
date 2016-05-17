package com.architecture.specification.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import japa.parser.ASTHelper;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class ArchitecturalModelParser {

	ArrayList<File> sourceFiles;
	ArchitecturalModel architecturalModel;

	public ArchitecturalModelParser(String sourceFilesDirectory, ArchitecturalModel architecturalModel)
			throws IOException {
		sourceFiles = new ArrayList<File>();
		listSourceFiles(new File[] { new File(sourceFilesDirectory) });

		this.architecturalModel = architecturalModel;

	}

	private void listSourceFiles(File[] files) throws IOException {
		for (File file : files) {
			if (file.isDirectory()) {
				listSourceFiles(file.listFiles());
			} else {
				if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("java"))
					sourceFiles.add(file);
			}
		}
	}

	public void verifyAgainstImplementation() throws IOException, ParseException {

		// creates an input stream for the file to be parsed
		for (File sourceFile : sourceFiles) {
			FileInputStream in = new FileInputStream(sourceFile);

			CompilationUnit cu;
			try {
				// parse the file
				cu = JavaParser.parse(in);

			} finally {
				in.close();
			}
			cu.accept(new SourceCodeVisitor(), null);
		}
	}

	/**
	 * Simple visitor implementation for visiting MethodDeclaration nodes.
	 */
	private static class SourceCodeVisitor extends VoidVisitorAdapter<Object> {

		@Override
		public void visit(final ClassOrInterfaceDeclaration n, Object arg) {
			System.out.println(" * " + n.getName());
            super.visit(n, arg);
        }
		
        @Override
        public void visit(final MethodDeclaration n, Object arg) {
            System.out.println(n.getName());
            super.visit(n, arg);
        }
		
		@Override
		public void visit(final MethodCallExpr n, Object arg) {
			
			System.out.println("method call :" + n);			
		}
	}
}
