package com.architecture.specification.library.architectural.model.constraint.model;

import com.architecture.specification.library.architectural.model.ArchitecturalModel;

public interface IArchitecturalModelConstraint {

	public boolean verify(ArchitecturalModel modelToBeVerified);
}
