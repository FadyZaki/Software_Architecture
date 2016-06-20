package com.architecture.specification.library.architectural.model.intended.constraint.component;

import com.architecture.specification.library.architectural.model.intended.component.ArchitecturalComponent;
/**
 * Defines a constraint on the component level
 * @author FadyShining
 *
 */
public interface IArchitecturalComponentConstraint {

	public boolean verify(ArchitecturalComponent componentToBeVerified);
}
