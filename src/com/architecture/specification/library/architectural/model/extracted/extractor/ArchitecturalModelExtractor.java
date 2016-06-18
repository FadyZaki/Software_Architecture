package com.architecture.specification.library.architectural.model.extracted.extractor;

import java.io.IOException;
import java.util.LinkedList;
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

	public ExtractedArchitecturalModel extractArchitecturalModelFromImplementation(List<String> verifiableClassFiles, List<String> blackboxClassFiles,
			List<String> uncheckedClassFiles, IntendedArchitecturalModel intendedArchitecturalModel) throws IOException {
		ImplementationParser implementationParser = new ImplementationParser();
		implementationParser.parseImplementationCode(verifiableClassFiles, blackboxClassFiles, uncheckedClassFiles, intendedArchitecturalModel);

		for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getModelComponentsIdentifiersMap().values()) {
			extractedArchitecturalModel.getActualImplementedComponents().put(architecturalComponent.getComponentIdentifier(),
					new ArchitecturalComponentImplementationMetaData(architecturalComponent.getComponentIdentifier()));
		}

		List<ClassMetaData> verifiableClassesMetadata = implementationParser.getVeriafiableClassesMetadata();
		List<ClassMetaData> blackboxClassesMetadata = implementationParser.getBlackboxClassesMetadata();

		for (ClassMetaData cmd : verifiableClassesMetadata) {
			System.out.println("aho ya 3am " + cmd.getFullyQualifiedName());
			if (intendedArchitecturalModel.getClassComponentsMap().containsKey(cmd.getFullyQualifiedName())) {
				for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getClassComponentsMap().get(cmd.getFullyQualifiedName())) {
					if (!(architecturalComponent instanceof BlackboxArchitecturalComponent)) {
						String componentIdentifier = architecturalComponent.getComponentIdentifier();
						ArchitecturalComponentImplementationMetaData currentComponentMetaData = extractedArchitecturalModel.getActualImplementedComponents()
								.get(componentIdentifier);
						currentComponentMetaData.getComponentImplementedClasses().put(cmd.getFullyQualifiedName(), cmd);
					}
				}
			} else {
				extractedArchitecturalModel.getImplementedClassesNotIntended().add(cmd.getFullyQualifiedName());
			}

		}

		for (ClassMetaData cmd : blackboxClassesMetadata) {
			if (intendedArchitecturalModel.getClassComponentsMap().containsKey(cmd.getFullyQualifiedName())) {
				for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getModelComponentsIdentifiersMap().values()) {
					if (architecturalComponent instanceof BlackboxArchitecturalComponent) {
						String componentIdentifier = architecturalComponent.getComponentIdentifier();
						ArchitecturalComponentImplementationMetaData currentComponentMetaData = extractedArchitecturalModel.getActualImplementedComponents()
								.get(componentIdentifier);
						currentComponentMetaData.getComponentImplementedClasses().put(cmd.getFullyQualifiedName(), cmd);
					}
				}
			}
		}

		for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getModelComponentsIdentifiersMap().values()) {
			String componentIdentifier = architecturalComponent.getComponentIdentifier();
			ArchitecturalComponentImplementationMetaData currentComponentMetaData = extractedArchitecturalModel.getActualImplementedComponents()
					.get(componentIdentifier);

			for (ArchitecturalComponent child : architecturalComponent.getChildrenComponents()) {
				currentComponentMetaData.getComponentChildren().put(child.getComponentIdentifier(),
						extractedArchitecturalModel.getActualImplementedComponents().get(child.getComponentIdentifier()));
			}
		}
		return extractedArchitecturalModel;

	}
}
