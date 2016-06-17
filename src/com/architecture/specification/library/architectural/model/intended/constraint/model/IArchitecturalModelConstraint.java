package com.architecture.specification.library.architectural.model.intended.constraint.model;

import com.architecture.specification.library.architectural.model.intended.IntendedArchitecturalModel;

public interface IArchitecturalModelConstraint {

	public boolean verify(IntendedArchitecturalModel modelToBeVerified);
}
