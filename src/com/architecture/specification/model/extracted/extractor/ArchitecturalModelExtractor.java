package com.architecture.specification.model.extracted.extractor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.architecture.specification.exceptions.VerificationException;
import com.architecture.specification.model.extracted.ExtractedArchitecturalModel;
import com.architecture.specification.model.extracted.metadata.ArchitecturalComponentImplementationMetaData;
import com.architecture.specification.model.extracted.metadata.ClassMetaData;
import com.architecture.specification.model.extracted.metadata.MethodCallMetaData;
import com.architecture.specification.model.extracted.metadata.MethodDeclarationMetaData;
import com.architecture.specification.model.extracted.parser.ImplementationParser;
import com.architecture.specification.model.intended.IntendedArchitecturalModel;
import com.architecture.specification.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.model.intended.component.BlackboxArchitecturalComponent;

import javassist.expr.MethodCall;

/**
 * This class is responsible for extracting the model from the metadata provided by the parser
 */
public class ArchitecturalModelExtractor {

	ExtractedArchitecturalModel extractedArchitecturalModel;
	ImplementationParser implementationParser;

	public ArchitecturalModelExtractor(ImplementationParser implementationParser) {
		this.extractedArchitecturalModel = new ExtractedArchitecturalModel();
		this.implementationParser = implementationParser;
	}

	/**
	 * This method extracts the model from the metadata provided by the parser
	 * @param verifiableClassFiles class files to be verified
	 * @param blackboxClassFiles external components class files
	 * @param uncheckedClassFiles class files to be ignored
	 * @param intendedArchitecturalModel
	 * @return
	 * @throws IOException
	 */
	public ExtractedArchitecturalModel extractArchitecturalModelFromImplementation(List<String> verifiableClassFiles, List<String> blackboxClassFiles,
			List<String> uncheckedClassFiles, IntendedArchitecturalModel intendedArchitecturalModel) throws IOException {
		implementationParser.parseImplementationCode(verifiableClassFiles, blackboxClassFiles, uncheckedClassFiles);

		for (ArchitecturalComponent architecturalComponent : intendedArchitecturalModel.getModelComponentsIdentifiersMap().values()) {
			extractedArchitecturalModel.getActualImplementedComponents().put(architecturalComponent.getComponentIdentifier(),
					new ArchitecturalComponentImplementationMetaData(architecturalComponent.getComponentIdentifier()));
		}

		List<ClassMetaData> verifiableClassesMetadata = implementationParser.getVeriafiableClassesMetadata();
		List<ClassMetaData> blackboxClassesMetadata = implementationParser.getBlackboxClassesMetadata();

		for (ClassMetaData cmd : verifiableClassesMetadata) {
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
