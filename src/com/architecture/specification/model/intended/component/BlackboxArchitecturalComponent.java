package com.architecture.specification.model.intended.component;

import java.util.ArrayList;
import java.util.HashSet;

import com.architecture.specification.model.intended.constraint.component.IArchitecturalComponentConstraint;
import com.architecture.specification.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.style.component.type.ArchitecturalComponentType;

/**
 * A blackbox architectural component represents an external component to the system under structure
 * @author FadyShining
 *
 */
public class BlackboxArchitecturalComponent extends ArchitecturalComponent {

	public BlackboxArchitecturalComponent(String componentIdentifier, ArchitecturalComponent parentComponent, ArrayList<String> componentClasses,
			HashSet<ProvidedPortInterface> providedInterfaces, HashSet<RequiredPortInterface> requiredInterfaces, HashSet<ArchitecturalComponentType> componentTypes, ArrayList<IArchitecturalComponentConstraint> componentConstraints) {
		super(componentIdentifier, parentComponent, componentClasses, providedInterfaces, requiredInterfaces, componentTypes, componentConstraints);
	}

}
