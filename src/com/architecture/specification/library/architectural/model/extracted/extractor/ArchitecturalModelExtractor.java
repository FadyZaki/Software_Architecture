package com.architecture.specification.library.architectural.model.extracted.extractor;

import java.io.IOException;
import java.util.List;

import com.architecture.specification.library.architectural.model.extracted.ExtractedArchitecturalModel;
import com.architecture.specification.library.architectural.model.extracted.metadata.ArchitecturalComponentImplementationMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.ClassMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.MethodCallMetaData;
import com.architecture.specification.library.architectural.model.extracted.metadata.MethodDeclarationMetaData;
import com.architecture.specification.library.architectural.model.extracted.parser.ImplementationParser;
import com.architecture.specification.library.architectural.model.intended.IntendedArchitecturalModel;
import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.library.architectural.model.intended.component.BlackboxArchitecturalComponent;
import com.architecture.specification.library.exceptions.VerificationException;

import javassist.expr.MethodCall;

public class ArchitecturalModelExtractor {

	ExtractedArchitecturalModel extractedArchitecturalModel;

	public ArchitecturalModelExtractor() {
		this.extractedArchitecturalModel = new ExtractedArchitecturalModel();
	}

	public ExtractedArchitecturalModel extractArchitecturalModelFromImplementation(List<String> verifiableClassFiles, List<String> blackboxClassFiles, IntendedArchitecturalModel intendedArchitecturalModel) throws IOException {
		ImplementationParser implementationParser = new ImplementationParser();
		List<ClassMetaData> cmdList = implementationParser.parseImplementationCode(verifiableClassFiles, blackboxClassFiles,
				intendedArchitecturalModel);

		for (ClassMetaData cmd : cmdList) {
			if (intendedArchitecturalModel.getClassComponentsMap().containsKey(cmd.getFullyQualifiedName())) {
//				System.out.println("class name : " + cmd.getFullyQualifiedName());
//				System.out.println("declared methods : ");
//				for (MethodDeclarationMetaData mmd : cmd.getDeclaredMethodsMap().values())
//					System.out.println(mmd.getMethodIdentifier() + " " + mmd.getMethodDeclaringClass() + " " + mmd.getMethodReturnType());
				for (MethodCallMetaData methodCall : cmd.getMethodCallsMap().values()) {
					if(!intendedArchitecturalModel.getClassComponentsMap().containsKey(methodCall.getMethodDeclaringClass())) {
						for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getClassComponentsMap().get(cmd.getFullyQualifiedName())) {
							if (!(architecturalComponent instanceof BlackboxArchitecturalComponent)) {
								
								extractedArchitecturalModel.getMethodCallsToBlackBoxClassesNotPartOfAnyComponent().add(methodCall.getMethodIdentifier());
								System.out.println("ahom :" + methodCall.getMethodIdentifier() + "  " + methodCall.getMethodDeclaringClass() + "  "  + cmd.getFullyQualifiedName());
							}
						}
					}
				}
				for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getClassComponentsMap().get(cmd.getFullyQualifiedName())) {
					String componentIdentifier = architecturalComponent.getComponentIdentifier();
					ArchitecturalComponentImplementationMetaData currentComponentMetaData = extractedArchitecturalModel.getActualImplementedComponents()
							.containsKey(componentIdentifier) ? extractedArchitecturalModel.getActualImplementedComponents().get(componentIdentifier)
									: new ArchitecturalComponentImplementationMetaData(componentIdentifier);
					currentComponentMetaData.getComponentImplementedClasses().put(cmd.getFullyQualifiedName(), cmd);
				}
			} else {
				extractedArchitecturalModel.getImplementedClassesNotIntended().add(cmd.getFullyQualifiedName());
			}
		}
		return extractedArchitecturalModel;
		
	}
}
