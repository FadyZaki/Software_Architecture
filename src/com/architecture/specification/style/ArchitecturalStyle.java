package com.architecture.specification.style;

import java.util.List;

import com.architecture.specification.model.intended.constraint.model.IArchitecturalModelConstraint;

/**
 * Defines an architectural style in terms of model level constraints
 * @author FadyShining
 *
 */
public class ArchitecturalStyle {

	private String styleIdentifier;
	private List<IArchitecturalModelConstraint> styleConstraints;

	public ArchitecturalStyle(String styleIdentifier, List<IArchitecturalModelConstraint> styleConstraints) {
		this.styleIdentifier = styleIdentifier;
		this.styleConstraints = styleConstraints;
	}

	public String getStyleIdentifier() {
		return styleIdentifier;
	}

	public List<IArchitecturalModelConstraint> getStyleConstraints() {
		return styleConstraints;
	}

}
