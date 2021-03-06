package com.architecture.specification.model.intended.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import com.architecture.specification.exceptions.ArchitecturalStyleException;
import com.architecture.specification.exceptions.BlackBoxCommunicationLinkException;
import com.architecture.specification.exceptions.BrokenConstraintException;
import com.architecture.specification.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.exceptions.ComponentNotFoundException;
import com.architecture.specification.exceptions.IncompatiblePortInterfacesException;
import com.architecture.specification.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.exceptions.PortInterfaceNotFoundException;
import com.architecture.specification.exceptions.UnusedComponentException;
import com.architecture.specification.exceptions.UnusedRequiredPortInterfaceException;
import com.architecture.specification.model.intended.IntendedArchitecturalModel;
import com.architecture.specification.model.intended.communication.link.CommunicationLink;
import com.architecture.specification.model.intended.component.ArchitecturalComponent;
import com.architecture.specification.model.intended.component.BlackboxArchitecturalComponent;
import com.architecture.specification.model.intended.constraint.component.IArchitecturalComponentConstraint;
import com.architecture.specification.model.intended.constraint.model.IArchitecturalModelConstraint;
import com.architecture.specification.model.intended.initializer.IArchitecturalModelInitializer;
import com.architecture.specification.model.intended.portinterface.PortInterface;
import com.architecture.specification.model.intended.portinterface.PortInterfaceCommunicationSynchronizationType;
import com.architecture.specification.model.intended.portinterface.PortInterfaceCommunicationType;
import com.architecture.specification.model.intended.portinterface.PortInterfaceType;
import com.architecture.specification.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.style.ArchitecturalStyle;
import com.architecture.specification.style.InMemoryArchitecturalStyles;
import com.architecture.specification.style.builder.ArchitecturalStylesBuilder;
import com.architecture.specification.style.component.type.ArchitecturalComponentType;
import com.architecture.specification.style.component.type.InMemoryArchitecturalComponentTypes;
import com.architecture.specification.style.initializer.ArchitecturalStylesInitializer;

/**
 * This class is responsible for building the intended architectural model 
 */
public class ArchitecturalModelBuilder implements IArchitecturalModelBuilder {

	public IntendedArchitecturalModel getArchitecturalModel() {
		return architecturalModel;
	}

	private IntendedArchitecturalModel architecturalModel;

	public ArchitecturalModelBuilder(String modelIdentifier) {
		this.architecturalModel = new IntendedArchitecturalModel(modelIdentifier);
	}

	/**
	 * This method builds the architectural model by calling the custom initializer methods
	 */
	@Override
	public IntendedArchitecturalModel buildArchitecturalModel(IArchitecturalModelInitializer initializer)
			throws IncompatiblePortInterfacesException, UnusedRequiredPortInterfaceException, UnusedComponentException, ComponentNotFoundException,
			PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException, BrokenConstraintException,
			ArchitecturalStyleException, BlackBoxCommunicationLinkException {

		if (initializer != null) {
			initializer.initializeModelPortInterfaces();
			initializer.initializeModelArchitecturalComponents();
			refineArchitecturalComponentsHierarchy();

			initializer.initializeModelComponentsCommunicationLinks();
			verifyCommunicationLinksCompatibility();
			verifyEachComponentIsUsed();
			verifyEachRequiredPortIsUsed();

			initializer.addConstraintsToModel();
			initializer.addStylesToModel();
			verifyConstraints();
		}

		return architecturalModel;

	}

	/**
	 * verifies all component and model constraints defined by the model
	 * @throws BrokenConstraintException
	 */
	private void verifyConstraints() throws BrokenConstraintException {
		for (IArchitecturalModelConstraint modelConstraint : architecturalModel.getModelConstraints()) {
			if (!modelConstraint.verify(architecturalModel))
				throw new BrokenConstraintException(architecturalModel.getModelIdentifier(), modelConstraint.getClass().getName());
		}

		for (ArchitecturalComponent architecturalComponent : architecturalModel.getModelComponentsIdentifiersMap().values()) {
			for (IArchitecturalComponentConstraint componentConstraint : architecturalComponent.getComponentConstraints())
				if (!componentConstraint.verify(architecturalComponent))
					throw new BrokenConstraintException(architecturalComponent.getComponentIdentifier(), componentConstraint.getClass().getName());
		}
		
		for (ArchitecturalStyle style : architecturalModel.getModelCompliantStyles()) {
			for (IArchitecturalModelConstraint modelConstraint : style.getStyleConstraints())
			if (!modelConstraint.verify(architecturalModel))
				throw new BrokenConstraintException(architecturalModel.getModelIdentifier(), modelConstraint.getClass().getSimpleName());
		}

	}

	private void refineArchitecturalComponentsHierarchy() {
		adjustChildrenComponentsForEachComponent();
		for (ArchitecturalComponent c : architecturalModel.getModelComponentsIdentifiersMap().values()) {
			if (c.getParentComponent() == null) {
				adjustComponentsTree(c);
			}
		}
	}

	private void adjustChildrenComponentsForEachComponent() {
		for (ArchitecturalComponent c : architecturalModel.getModelComponentsIdentifiersMap().values()) {
			if (c.getParentComponent() != null)
				c.getParentComponent().getChildrenComponents().add(c);
		}
	}

	// post-order traversal
	private void adjustComponentsTree(ArchitecturalComponent root) {
		for (ArchitecturalComponent c : root.getChildrenComponents()) {
			adjustComponentsTree(c);
		}
		for (ArchitecturalComponent c : root.getChildrenComponents()) {
			root.getProvidedInterfaces().addAll(c.getProvidedInterfaces());
			root.getRequiredInterfaces().addAll(c.getRequiredInterfaces());
		}
	}

	/**
	 * Two Interfaces are compatible if they have the same attributes (interface
	 * identifier, communication type and synchronization type) and are siblings
	 * 
	 * @throws IncompatiblePortInterfacesException
	 * @throws BlackBoxCommunicationLinkException
	 */
	private void verifyCommunicationLinksCompatibility() throws IncompatiblePortInterfacesException, BlackBoxCommunicationLinkException {
		for (CommunicationLink cl : architecturalModel.getCommunicationLinks()) {
			if (cl.getProvidingComponent() instanceof BlackboxArchitecturalComponent && cl.getRequiringComponent() instanceof BlackboxArchitecturalComponent)
				throw new BlackBoxCommunicationLinkException(cl.getProvidingComponent().getComponentIdentifier(),
						cl.getRequiringComponent().getComponentIdentifier());

			if (!cl.getProvidedPortInterface().canConnectTo(cl.getRequiredPortInterface())
					|| !Objects.equals(cl.getProvidingComponent().getParentComponent(), cl.getRequiringComponent().getParentComponent()))
				throw new IncompatiblePortInterfacesException(cl.getProvidedPortInterface().getPortInterfaceIdentifier(),
						cl.getRequiredPortInterface().getPortInterfaceIdentifier());
			else
				removeUnnecessaryPortInterfaces(cl);
		}

	}

	private void removeUnnecessaryPortInterfaces(CommunicationLink cl) {
		ArchitecturalComponent providingComponent = cl.getProvidingComponent();
		ArchitecturalComponent requiringComponent = cl.getRequiringComponent();

		ArchitecturalComponent currentComponent = providingComponent.getParentComponent();
		while (currentComponent != null) {
			currentComponent.getProvidedInterfaces().remove(cl.getProvidedPortInterface());
			currentComponent = currentComponent.getParentComponent();
		}

		currentComponent = requiringComponent.getParentComponent();
		while (currentComponent != null) {
			currentComponent.getRequiredInterfaces().remove(cl.getRequiredPortInterface());
			currentComponent = currentComponent.getParentComponent();
		}
	}

	/**
	 * verifies that each component engages in at least one commmunication link
	 * @throws UnusedComponentException
	 */
	private void verifyEachComponentIsUsed() throws UnusedComponentException {
		HashSet<CommunicationLink> commLinks = architecturalModel.getCommunicationLinks();
		HashSet<ArchitecturalComponent> usedComponents = new HashSet<ArchitecturalComponent>();
		for (CommunicationLink cl : commLinks) {
			usedComponents.add(cl.getProvidingComponent());
			addUsedComponents(usedComponents, cl.getProvidingComponent(), cl.getProvidedPortInterface());

			usedComponents.add(cl.getRequiringComponent());
			addUsedComponents(usedComponents, cl.getRequiringComponent(), cl.getRequiredPortInterface());
		}

		for (ArchitecturalComponent c : architecturalModel.getModelComponentsIdentifiersMap().values()) {
			if (!usedComponents.contains(c))
				throw new UnusedComponentException(c);
		}

	}

	private void addUsedComponents(HashSet<ArchitecturalComponent> usedComponents, ArchitecturalComponent component, PortInterface p) {
		if (!component.getChildrenComponents().isEmpty()) {
			for (ArchitecturalComponent c : component.getChildrenComponents()) {
				if (c.getProvidedInterfaces().contains(p)) {
					usedComponents.add(c);
					addUsedComponents(usedComponents, c, p);
				}
			}
		}

	}

	/**
	 * verifies that each required port engages in at least one communication link
	 * @throws UnusedRequiredPortInterfaceException
	 */
	private void verifyEachRequiredPortIsUsed() throws UnusedRequiredPortInterfaceException {

		HashMap<ArchitecturalComponent, HashSet<RequiredPortInterface>> componentsUsedRequiredPortInterfacesMap = new HashMap<ArchitecturalComponent, HashSet<RequiredPortInterface>>();
		HashSet<CommunicationLink> commLinks = architecturalModel.getCommunicationLinks();
		for (CommunicationLink cl : commLinks) {
			HashSet<RequiredPortInterface> requiredPortInterfaces = componentsUsedRequiredPortInterfacesMap.containsKey(cl.getRequiringComponent())
					? componentsUsedRequiredPortInterfacesMap.get(cl.getRequiringComponent()) : new HashSet<RequiredPortInterface>();
			requiredPortInterfaces.add(cl.getRequiredPortInterface());
			componentsUsedRequiredPortInterfacesMap.put(cl.getRequiringComponent(), requiredPortInterfaces);
		}

		for (ArchitecturalComponent c : architecturalModel.getModelComponentsIdentifiersMap().values()) {
			if (c.getParentComponent() == null) {
				if (!c.getRequiredInterfaces().isEmpty() && !Objects.equals(c.getRequiredInterfaces(), componentsUsedRequiredPortInterfacesMap.get(c)))
					throw new UnusedRequiredPortInterfaceException(c);

			}
		}
	}

	/**
	 * adds a component to the intended model 
	 *
	 * @param componentIdentifier
	 * @param parentComponentIdentifier
	 * @param componentClasses
	 * @param providedInterfaces
	 * @param requiredInterfaces
	 * @param componentTypes
	 * @param componentConstraints
	 * @param isBlackbox
	 * @throws ComponentNotFoundException
	 */
	public void addComponent(String componentIdentifier, String parentComponentIdentifier, ArrayList<String> componentClasses,
			HashSet<ProvidedPortInterface> providedInterfaces, HashSet<RequiredPortInterface> requiredInterfaces,
			HashSet<ArchitecturalComponentType> componentTypes, ArrayList<IArchitecturalComponentConstraint> componentConstraints, boolean isBlackbox)
					throws ComponentNotFoundException {
		HashMap<String, ArchitecturalComponent> componentsIdentifersMap = architecturalModel.getModelComponentsIdentifiersMap();
		ArchitecturalComponent parentComponent = componentsIdentifersMap.get(parentComponentIdentifier);
		if (parentComponentIdentifier != null && parentComponent == null)
			throw new ComponentNotFoundException(parentComponentIdentifier);

		ArchitecturalComponent architecturalComponent;
		if (isBlackbox)
			architecturalComponent = new BlackboxArchitecturalComponent(componentIdentifier, parentComponent, componentClasses, providedInterfaces,
					requiredInterfaces, componentTypes, componentConstraints);
		else
			architecturalComponent = new ArchitecturalComponent(componentIdentifier, parentComponent, componentClasses, providedInterfaces, requiredInterfaces,
					componentTypes, componentConstraints);
		architecturalModel.getModelComponentsIdentifiersMap().put(architecturalComponent.getComponentIdentifier(), architecturalComponent);
	}

	/**
	 * adds a required and provided port interfaces with the specified properties to the intended model 
	 * @param portInterfaceIdentifier
	 * @param portInterfaceCommunicationType
	 * @param portInterfaceCommunicationSynchronizationType
	 */
	public void addPortInterface(String portInterfaceIdentifier, PortInterfaceCommunicationType portInterfaceCommunicationType,
			PortInterfaceCommunicationSynchronizationType portInterfaceCommunicationSynchronizationType) {

		architecturalModel.getModelProvidedPortInterfacesMap().put(portInterfaceIdentifier,
				new ProvidedPortInterface(portInterfaceIdentifier, portInterfaceCommunicationType, portInterfaceCommunicationSynchronizationType));
		architecturalModel.getModelRequiredPortInterfacesMap().put(portInterfaceIdentifier,
				new RequiredPortInterface(portInterfaceIdentifier, portInterfaceCommunicationType, portInterfaceCommunicationSynchronizationType));

	}

	public ProvidedPortInterface getProvidedPortInterface(String portInterfaceIdentifier) throws PortInterfaceNotFoundException {

		ProvidedPortInterface p = architecturalModel.getModelProvidedPortInterfacesMap().get(portInterfaceIdentifier);

		if (p == null)
			throw new PortInterfaceNotFoundException(portInterfaceIdentifier);
		else
			return p;

	}

	public RequiredPortInterface getRequiredPortInterface(String portInterfaceIdentifier) throws PortInterfaceNotFoundException {
		RequiredPortInterface r = architecturalModel.getModelRequiredPortInterfacesMap().get(portInterfaceIdentifier);

		if (r == null)
			throw new PortInterfaceNotFoundException(portInterfaceIdentifier);
		else
			return r;

	}

	/**
	 * adds a communication link to the intended model 
	 * @param providingComponentIdentifier
	 * @param requiringComponentIdentifier
	 * @param innermostProvidingComponentIdentifier
	 * @param portInterfaceIdentifier
	 * @throws ComponentNotFoundException
	 * @throws PortInterfaceNotDefinedInComponentException
	 * @throws PortInterfaceNotFoundException
	 * @throws ComponentNotDescendantOfAnotherException
	 */
	public void addCommunicationLink(String providingComponentIdentifier, String requiringComponentIdentifier, String innermostProvidingComponentIdentifier,
			String portInterfaceIdentifier) throws ComponentNotFoundException, PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException,
					ComponentNotDescendantOfAnotherException {
		HashMap<String, ArchitecturalComponent> componentsIdentifersMap = architecturalModel.getModelComponentsIdentifiersMap();
		ArchitecturalComponent providingComponent = componentsIdentifersMap.get(providingComponentIdentifier);
		ArchitecturalComponent requiringComponent = componentsIdentifersMap.get(requiringComponentIdentifier);

		if (providingComponent == null)
			throw new ComponentNotFoundException(providingComponentIdentifier);

		if (requiringComponent == null)
			throw new ComponentNotFoundException(requiringComponentIdentifier);

		ProvidedPortInterface p = getProvidedPortInterface(portInterfaceIdentifier);
		RequiredPortInterface r = getRequiredPortInterface(portInterfaceIdentifier);

		HashMap<String, ProvidedPortInterface> providingComponentPortInterfacesMap = providingComponent.getProvidedInterfacesMap();
		HashMap<String, RequiredPortInterface> requiringComponentPortInterfacesMap = requiringComponent.getRequiredInterfacesMap();

		if (!providingComponentPortInterfacesMap.containsKey(p.getPortInterfaceIdentifier()))
			throw new PortInterfaceNotDefinedInComponentException(p.getPortInterfaceIdentifier(), providingComponent);

		if (!requiringComponentPortInterfacesMap.containsKey(r.getPortInterfaceIdentifier()))
			throw new PortInterfaceNotDefinedInComponentException(r.getPortInterfaceIdentifier(), requiringComponent);

		ArchitecturalComponent innermostProvidingComponent = null;
		if (innermostProvidingComponentIdentifier != null) {
			innermostProvidingComponent = componentsIdentifersMap.get(innermostProvidingComponentIdentifier);
			if (innermostProvidingComponent == null)
				throw new ComponentNotFoundException(innermostProvidingComponentIdentifier);

			if (!componentIsDescendantOfAnother(innermostProvidingComponent, providingComponent))
				throw new ComponentNotDescendantOfAnotherException(innermostProvidingComponent.getComponentIdentifier(),
						providingComponent.getComponentIdentifier());

			if (!innermostProvidingComponent.getProvidedInterfacesMap().containsKey(p.getPortInterfaceIdentifier()))
				throw new PortInterfaceNotDefinedInComponentException(p.getPortInterfaceIdentifier(), innermostProvidingComponent);

		}

		CommunicationLink communicationLink = new CommunicationLink(providingComponent, requiringComponent, innermostProvidingComponent, p, r);
		architecturalModel.getCommunicationLinks().add(communicationLink);
	}

	private boolean componentIsDescendantOfAnother(ArchitecturalComponent descendantComponent, ArchitecturalComponent parentComponent) {
		while ((descendantComponent = descendantComponent.getParentComponent()) != null) {
			if (descendantComponent.equals(parentComponent))
				return true;
		}
		return false;
	}

	/**
	 * adds a constraint to be followed by the model
	 * @param modelConstraint
	 */
	public void addModelConstraint(IArchitecturalModelConstraint modelConstraint) {
		architecturalModel.getModelConstraints().add(modelConstraint);
	}

	/**
	 * adds an architectural style to be followed by the model
	 * @param styleIdentifier
	 * @throws ArchitecturalStyleException
	 */
	public void addModelCompliantStyle(String styleIdentifier) throws ArchitecturalStyleException {
		ArchitecturalStyle modelCompliantStyle = InMemoryArchitecturalStyles.getInMemoryModelStyles().get(styleIdentifier);
		if (modelCompliantStyle != null) {
			architecturalModel.getModelCompliantStyles().add(modelCompliantStyle);
			}
		else
			throw new ArchitecturalStyleException("This Style was never defined ", styleIdentifier);
	}

}
