package com.architecture.specification.library.architectural.style.component.type;

import java.util.List;

import com.architecture.specification.library.architectural.model.intended.constraint.component.IArchitecturalComponentConstraint;

public class ArchitecturalComponentType {

	private String componentTypeIdentifier;
	private List<IArchitecturalComponentConstraint> componentTypeConstraints;

	public ArchitecturalComponentType(String componentTypeIdentifier, List<IArchitecturalComponentConstraint> componentTypeConstraints) {
		this.componentTypeIdentifier = componentTypeIdentifier;
		this.componentTypeConstraints = componentTypeConstraints;
	}

	public List<IArchitecturalComponentConstraint> getComponentTypeConstraints() {
		return componentTypeConstraints;
	}
	
	public String getComponentTypeIdentifier() {
		return componentTypeIdentifier;
	}
	
	@Override
	public int hashCode() {
		return this.componentTypeIdentifier.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ArchitecturalComponentType))
			return false;
		if (obj == this)
			return true;

		ArchitecturalComponentType rhs = (ArchitecturalComponentType) obj;
		return this.getComponentTypeIdentifier().equals(rhs.getComponentTypeIdentifier());
	}
	
	
	
	
	
	
}
