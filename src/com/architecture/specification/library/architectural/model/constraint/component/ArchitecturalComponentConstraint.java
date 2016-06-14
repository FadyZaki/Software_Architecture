package com.architecture.specification.library.architectural.model.constraint.component;

import com.architecture.specification.library.architectural.model.component.ArchitecturalComponent;

public interface ArchitecturalComponentConstraint {

	public boolean verify(ArchitecturalComponent componentToBeVerified);
}
