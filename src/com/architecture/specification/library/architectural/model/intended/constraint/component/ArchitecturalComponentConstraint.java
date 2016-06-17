package com.architecture.specification.library.architectural.model.intended.constraint.component;

import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;

public interface ArchitecturalComponentConstraint {

	public boolean verify(ArchitecturalComponent componentToBeVerified);
}
