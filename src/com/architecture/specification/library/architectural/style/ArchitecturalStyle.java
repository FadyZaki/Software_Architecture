package com.architecture.specification.library.architectural.style;

import java.util.List;

import com.architecture.specification.library.architectural.model.constraint.model.IArchitecturalModelConstraint;

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
