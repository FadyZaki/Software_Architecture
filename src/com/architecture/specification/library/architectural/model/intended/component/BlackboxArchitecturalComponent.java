package com.architecture.specification.library.architectural.model.intended.component;

import java.util.ArrayList;
import java.util.HashSet;

import com.architecture.specification.library.architectural.model.intended.constraint.component.IArchitecturalComponentConstraint;
import com.architecture.specification.library.architectural.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.library.architectural.style.component.type.ArchitecturalComponentType;

public class BlackboxArchitecturalComponent extends ArchitecturalComponent {

	public BlackboxArchitecturalComponent(String componentIdentifier, ArchitecturalComponent parentComponent, ArrayList<String> componentClasses,
			HashSet<ProvidedPortInterface> providedInterfaces, HashSet<RequiredPortInterface> requiredInterfaces, HashSet<ArchitecturalComponentType> componentTypes, ArrayList<IArchitecturalComponentConstraint> componentConstraints) {
		super(componentIdentifier, parentComponent, componentClasses, providedInterfaces, requiredInterfaces, componentTypes, componentConstraints);
	}

}
