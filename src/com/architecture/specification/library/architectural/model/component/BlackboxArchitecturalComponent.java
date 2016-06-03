package com.architecture.specification.library.architectural.model.component;

import java.util.ArrayList;
import java.util.HashSet;

import com.architecture.specification.library.architectural.model.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.portinterface.RequiredPortInterface;

public class BlackboxArchitecturalComponent extends ArchitecturalComponent {

	public BlackboxArchitecturalComponent(String componentIdentifier, ArchitecturalComponent parentComponent, ArrayList<String> componentClasses,
			HashSet<ProvidedPortInterface> providedInterfaces, HashSet<RequiredPortInterface> requiredInterfaces) {
		super(componentIdentifier, parentComponent, componentClasses, providedInterfaces, requiredInterfaces);
	}

}
