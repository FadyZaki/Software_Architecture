package com.architecture.specification.architectural.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.architecture.specification.library.ArchitecturalComponent;
import com.architecture.specification.library.ArchitecturalModel;
import com.architecture.specification.library.CommunicationLink;
import com.architecture.specification.library.PortInterfaceCommunicationSynchronizationType;
import com.architecture.specification.library.PortInterfaceCommunicationType;
import com.architecture.specification.library.ProvidedPortInterface;
import com.architecture.specification.library.RequiredPortInterface;

public class CustomArchitecturalModel extends ArchitecturalModel {

	private static String GUI_COMPONENT = "GUI Component";
	private static String SHAPES_MODEL_COMPONENT = "Shapes Model Component";

	@Override
	protected void initializeArchitecturalComponents(HashSet<ArchitecturalComponent> modelComponents) {

		HashSet<ProvidedPortInterface> guiProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		guiProvidedInterfaces.add(new ProvidedPortInterface("updateGui", PortInterfaceCommunicationType.METHOD_CALL,
				new HashSet<PortInterfaceCommunicationSynchronizationType>(
						Arrays.asList(new PortInterfaceCommunicationSynchronizationType[] {
								PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK }))));
		modelComponents.add(new ArchitecturalComponent.Builder(GUI_COMPONENT)
				.componentClasses(new HashSet<String>(
						Arrays.asList(new String[] { "VectorGraphicsGuiDelegate", "DrawingPanel" })))
				.providedPortInterfaces(guiProvidedInterfaces).build());

		HashSet<RequiredPortInterface> shapesModelRequiredInterfaces = new HashSet<RequiredPortInterface>();
		shapesModelRequiredInterfaces
				.add(new RequiredPortInterface("updateGui", PortInterfaceCommunicationType.METHOD_CALL,
						PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		modelComponents.add(new ArchitecturalComponent.Builder(SHAPES_MODEL_COMPONENT)
				.componentClasses(new HashSet<String>(Arrays.asList(new String[] { "VectorGraphicsModel" })))
				.requiredPortInterfaces(shapesModelRequiredInterfaces).build());

	}

	@Override
	protected void initializeComponentsCommunicationLinks(HashSet<CommunicationLink> componentsCommunicationLinks,
			HashMap<String, ArchitecturalComponent> modelComponentsIdentifiersMap) {

		componentsCommunicationLinks.add(new CommunicationLink(
				modelComponentsIdentifiersMap.get(GUI_COMPONENT).getProvidedInterfacesMap().get("updateGui"),
				modelComponentsIdentifiersMap.get(SHAPES_MODEL_COMPONENT).getRequiredInterfacesMap().get("updateGui")));

	}

	@Override
	protected void initializeConcurrentComponentsMap(
			HashMap<ArchitecturalComponent, HashSet<ArchitecturalComponent>> concurrentComponentsMap,
			HashMap<String, ArchitecturalComponent> modelComponentsIdentifiersMap) {
		concurrentComponentsMap.put(modelComponentsIdentifiersMap.get(GUI_COMPONENT), new HashSet<>(Arrays
				.asList(new ArchitecturalComponent[] { modelComponentsIdentifiersMap.get(SHAPES_MODEL_COMPONENT) })));
		concurrentComponentsMap.put(modelComponentsIdentifiersMap.get(SHAPES_MODEL_COMPONENT), new HashSet<>(
				Arrays.asList(new ArchitecturalComponent[] { modelComponentsIdentifiersMap.get(GUI_COMPONENT) })));

	}
}
