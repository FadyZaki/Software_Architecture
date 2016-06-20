package com.architecture.specification.library.architectural.model.intended.constraint.model;

import com.architecture.specification.library.architectural.model.intended.IntendedArchitecturalModel;

/**
 * Defines a constraint on the model level
 * @author FadyShining
 *
 */
public interface IArchitecturalModelConstraint {

	public boolean verify(IntendedArchitecturalModel modelToBeVerified);
}
