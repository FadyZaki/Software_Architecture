package com.architecture.specification.library.architectural.model.component;

import java.util.ArrayList;
import java.util.HashSet;

import com.architecture.specification.library.architectural.model.constraint.component.ArchitecturalComponentConstraint;
import com.architecture.specification.library.architectural.model.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.portinterface.RequiredPortInterface;
import com.architecture.specification.library.architectural.style.component.type.ArchitecturalComponentType;

public class BlackboxArchitecturalComponent extends ArchitecturalComponent {

	public BlackboxArchitecturalComponent(String componentIdentifier, ArchitecturalComponent parentComponent, ArrayList<String> componentClasses,
			HashSet<ProvidedPortInterface> providedInterfaces, HashSet<RequiredPortInterface> requiredInterfaces, HashSet<ArchitecturalComponentType> componentTypes, ArrayList<ArchitecturalComponentConstraint> componentConstraints) {
		super(componentIdentifier, parentComponent, componentClasses, providedInterfaces, requiredInterfaces, componentTypes, componentConstraints);
	}

}
