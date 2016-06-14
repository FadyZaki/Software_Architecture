package com.architecture.specification.library.architectural.style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.architecture.specification.library.architectural.model.constraint.component.ArchitecturalComponentConstraint;
import com.architecture.specification.library.architectural.model.constraint.component.ComponentProvidedPortsNumberConstraint;
import com.architecture.specification.library.architectural.model.constraint.component.ComponentRequiredPortsNumberConstraint;
import com.architecture.specification.library.architectural.model.constraint.model.IArchitecturalModelConstraint;
import com.architecture.specification.library.architectural.model.constraint.model.ComponentTypeExistsInModelConstraint;
import com.architecture.specification.library.architectural.model.constraint.model.ConnectedComponentTypesConstraint;
import com.architecture.specification.library.architectural.style.component.type.ArchitecturalComponentType;
import com.architecture.specification.library.architectural.style.component.type.InMemoryArchitecturalComponentTypes;

public class InMemoryArchitecturalStyles {

	public static final String SERVER_CLIENT_STYLE = "Server Client Style";
	private static HashMap<String, ArchitecturalStyle> InMemoryModelStyles = new HashMap<String, ArchitecturalStyle>();

	public static HashMap<String, ArchitecturalStyle> initializeBasicArchitecturalStyles() {
		
		List<IArchitecturalModelConstraint> clientServerStyleConstraints = new ArrayList<IArchitecturalModelConstraint>();
		clientServerStyleConstraints.add(new ComponentTypeExistsInModelConstraint(InMemoryArchitecturalComponentTypes.CLIENT_COMPONENT_TYPE));
		clientServerStyleConstraints.add(new ComponentTypeExistsInModelConstraint(InMemoryArchitecturalComponentTypes.SERVER_COMPONENT_TYPE));
		clientServerStyleConstraints.add(new ConnectedComponentTypesConstraint(InMemoryArchitecturalComponentTypes.CLIENT_COMPONENT_TYPE, InMemoryArchitecturalComponentTypes.SERVER_COMPONENT_TYPE));
		ArchitecturalStyle clientServerStyle = new ArchitecturalStyle(SERVER_CLIENT_STYLE, clientServerStyleConstraints);
		InMemoryModelStyles.put(clientServerStyle.getStyleIdentifier(), clientServerStyle);
			
		return InMemoryModelStyles;
	}

	public static HashMap<String, ArchitecturalStyle> getInMemoryModelStyles() {
		return InMemoryModelStyles;
	}

}
