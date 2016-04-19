package com.architecture.specification.library;

import java.io.FileInputStream;
import java.io.IOException;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

public class ArchitecturalModelParser {

	
	public static void verifyAgainstImplementation(String sourceFilesDirectory, ArchitecturalModel model) throws IOException, ParseException {
		
		// creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("test.java");

        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }

        // prints the resulting compilation unit to default system output
        System.out.println(cu.toString());
	}
}
