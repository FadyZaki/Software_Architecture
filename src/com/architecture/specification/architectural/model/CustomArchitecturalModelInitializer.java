package com.architecture.specification.architectural.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.architecture.specification.library.ArchitecturalComponent;
import com.architecture.specification.library.ArchitecturalModel;
import com.architecture.specification.library.ArchitecturalModelBuilder;
import com.architecture.specification.library.ArchitecturalModelInitializer;
import com.architecture.specification.library.CommunicationLink;
import com.architecture.specification.library.IArchitecturalModelInitializer;
import com.architecture.specification.library.PortInterfaceCommunicationSynchronizationType;
import com.architecture.specification.library.PortInterfaceCommunicationType;
import com.architecture.specification.library.ProvidedPortInterface;
import com.architecture.specification.library.RequiredPortInterface;

public class CustomArchitecturalModelInitializer extends ArchitecturalModelInitializer {

	public CustomArchitecturalModelInitializer(ArchitecturalModelBuilder architecturalModelBuilder) {
		super(architecturalModelBuilder);
	}

	private static String GUI_COMPONENT = "GUI Component";
	private static String SHAPES_MODEL_COMPONENT = "Shapes Model Component";

	@Override
	public void initializeArchitecturalComponents() {

//		HashSet<ProvidedPortInterface> guiProvidedInterfaces = new HashSet<ProvidedPortInterface>();
//		guiProvidedInterfaces.add(new ProvidedPortInterface("updateGui", PortInterfaceCommunicationType.METHOD_CALL,
//				new HashSet<PortInterfaceCommunicationSynchronizationType>(
//						Arrays.asList(new PortInterfaceCommunicationSynchronizationType[] {
//								PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK }))));
//		architecturalModelBuilder.addComponent(GUI_COMPONENT, null,
//				new HashSet<String>(Arrays.asList(new String[] { "VectorGraphicsGuiDelegate", "DrawingPanel" })),
//				guiProvidedInterfaces, null);
//
//		HashSet<RequiredPortInterface> shapesModelRequiredInterfaces = new HashSet<RequiredPortInterface>();
//		shapesModelRequiredInterfaces
//				.add(new RequiredPortInterface("updateGui", PortInterfaceCommunicationType.METHOD_CALL,
//						PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
//
//		architecturalModelBuilder.addComponent(SHAPES_MODEL_COMPONENT, null,
//				new HashSet<String>(Arrays.asList(new String[] { "VectorGraphicsModel" })), null,
//				shapesModelRequiredInterfaces);

	}

	@Override
	public void initializeComponentsCommunicationLinks() {
//		architecturalModelBuilder.addCommunicationLink(GUI_COMPONENT, SHAPES_MODEL_COMPONENT, "updateGui");

	}

	@Override
	public void initializeConcurrentComponentsMap() {
//		architecturalModelBuilder.addConcurrentComponentsEntry(GUI_COMPONENT, new ArrayList<String> (Arrays.asList(new String[] { SHAPES_MODEL_COMPONENT })));
//		architecturalModelBuilder.addConcurrentComponentsEntry(SHAPES_MODEL_COMPONENT, new ArrayList<String> (Arrays.asList(new String[] { GUI_COMPONENT })));
	}
	
}
