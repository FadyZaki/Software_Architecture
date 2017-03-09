package com.architecture.specification.style.component.type;

import java.util.Arrays;
import java.util.HashMap;

import com.architecture.specification.model.intended.constraint.component.ComponentMinimumProvidedPortsNumberConstraint;
import com.architecture.specification.model.intended.constraint.component.ComponentMinimumRequiredPortsNumberConstraint;
import com.architecture.specification.model.intended.constraint.component.IArchitecturalComponentConstraint;

public class InMemoryArchitecturalComponentTypes {

	public static final String CLIENT_COMPONENT_TYPE = "Client Component Type";
	public static final String SERVER_COMPONENT_TYPE = "Server Component Type";
	private static HashMap<String, ArchitecturalComponentType> InMemoryComponentTypes = new HashMap<String, ArchitecturalComponentType>();

	public static void initializeBasicComponentTypes() {

		ArchitecturalComponentType clientComponentType = new ArchitecturalComponentType(CLIENT_COMPONENT_TYPE,
				Arrays.asList(new IArchitecturalComponentConstraint[] { new ComponentMinimumProvidedPortsNumberConstraint(1) }));
		InMemoryComponentTypes.put(clientComponentType.getComponentTypeIdentifier(), clientComponentType);
		
		ArchitecturalComponentType serverComponentType = new ArchitecturalComponentType(SERVER_COMPONENT_TYPE,
				Arrays.asList(new IArchitecturalComponentConstraint[] { new ComponentMinimumRequiredPortsNumberConstraint(1) }));
		InMemoryComponentTypes.put(serverComponentType.getComponentTypeIdentifier(), serverComponentType);
		
	}
	
	public static HashMap<String, ArchitecturalComponentType> getInMemoryComponentTypes() {
		return InMemoryComponentTypes;
	}
}
