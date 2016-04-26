package com.architecture.specification.library;

import java.io.FileInputStream;
import java.io.IOException;

import japa.parser.ASTHelper;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class ArchitecturalModelParser {

	public static void verifyAgainstImplementation(String sourceFilesDirectory, ArchitecturalModel model)
			throws IOException, ParseException {

		// creates an input stream for the file to be parsed
		FileInputStream in = new FileInputStream(
				"D:/Work/TrialWorkspace/DissertationProject/src/com/architecture/specification/library/ArchitecturalModel.java");

		CompilationUnit cu;
		try {
			// parse the file
			cu = JavaParser.parse(in);
		} finally {
			in.close();
		}

		// visit and print the methods names
		new MethodChangerVisitor().visit(cu, null);
		// prints the changed compilation unit
		//System.out.println(cu.toString());
	}

	/**
	 * Simple visitor implementation for visiting MethodDeclaration nodes.
	 */
	private static class MethodChangerVisitor extends VoidVisitorAdapter {

		@Override
		public void visit(MethodDeclaration n, Object arg) {
			// change the name of the method to upper case
			n.setName(n.getName().toUpperCase());

			 // create the new parameter
			 Parameter newArg = ASTHelper.createParameter(ASTHelper.INT_TYPE,
			 "value");
			
			 // add the parameter to the method
			 ASTHelper.addParameter(n, newArg);
		}
	}
}
