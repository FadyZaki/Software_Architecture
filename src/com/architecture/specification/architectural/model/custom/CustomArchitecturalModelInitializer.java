package com.architecture.specification.architectural.model.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.architecture.specification.library.architectural.model.ArchitecturalModelBuilder;
import com.architecture.specification.library.architectural.model.ArchitecturalModelInitializer;
import com.architecture.specification.library.architectural.model.portinterface.PortInterfaceCommunicationSynchronizationType;
import com.architecture.specification.library.architectural.model.portinterface.PortInterfaceCommunicationType;
import com.architecture.specification.library.architectural.model.portinterface.PortInterfaceType;
import com.architecture.specification.library.architectural.model.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.portinterface.RequiredPortInterface;
import com.architecture.specification.library.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.library.exceptions.ComponentNotFoundException;
import com.architecture.specification.library.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.library.exceptions.PortInterfaceNotFoundException;

public class CustomArchitecturalModelInitializer extends ArchitecturalModelInitializer {

	private static String SERVER_COMPONENT = "Server Component";
	private static String GEO_LOCATOR_COMPONENT = "Geo Locator Component";
	private static String SOCIAL_NETWORK_COMPONENT = "Social Network Component";
	private static String CLIENT_HANDLER_COMPONENT = "Client Handler Component";
	private static String MESSAGE_COMPONENT = "Message Component";
	private static String USER_COMPONENT = "User Component";

	private static String CLIENT_COMPONENT = "Client Component";
	private static String SERVER_HANDLER_COMPONENT = "Server Handler Component";
	private static String UI_COMPONENT = "UI Component";

	private static String IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT = "identifyGeoLocationUsingIpAddress";
	private static String REGISTER_USER_PORT = "registerUser";
	private static String AUTHORIZE_USER_PORT = "authorizeUser";
	private static String POST_MESSAGE_PORT = "postMessage";
	private static String REPLY_TO_MESSAGE_PORT = "replyToMessage";
	private static String DELETE_MESSAGE_PORT = "deleteMessage";
	private static String SOCIAL_NETWORK_STATE_PORT = "socialNetworkState";

	private static String MESSAGE_INFORMATION_PORT = "messageInformation";
	private static String MESSAGE_REPLIES_PORT = "messageReplies";

	private static String USER_INFORMATION_PORT = "userInformation";
	private static String USER_ACCOUNT_TYPE_PORT = "userAccountType";
	private static String USER_SUBSCRIPTION_FEE_PORT = "userSubscriptionFee";

	private static String SOCKET_PORT = "socket";

	private static String HANDLE_CLIENT_COMMANDS_PORT = "handleClientCommands";

	public CustomArchitecturalModelInitializer(ArchitecturalModelBuilder architecturalModelBuilder) {
		super(architecturalModelBuilder);
	}

	@Override
	public void initializeModelPortInterfaces() {
		architecturalModelBuilder.addPortInterface(IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT,
				PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT,
				PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(REGISTER_USER_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(REGISTER_USER_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(AUTHORIZE_USER_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(AUTHORIZE_USER_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(POST_MESSAGE_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(POST_MESSAGE_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(REPLY_TO_MESSAGE_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(REPLY_TO_MESSAGE_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(DELETE_MESSAGE_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(DELETE_MESSAGE_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		architecturalModelBuilder.addPortInterface(SOCIAL_NETWORK_STATE_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(SOCIAL_NETWORK_STATE_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(MESSAGE_INFORMATION_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(MESSAGE_INFORMATION_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(MESSAGE_REPLIES_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(MESSAGE_REPLIES_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(USER_INFORMATION_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(USER_INFORMATION_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(USER_ACCOUNT_TYPE_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(USER_ACCOUNT_TYPE_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(USER_SUBSCRIPTION_FEE_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(USER_SUBSCRIPTION_FEE_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		architecturalModelBuilder.addPortInterface(SOCKET_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.MESSAGE_PASSING,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

		architecturalModelBuilder.addPortInterface(SOCKET_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.MESSAGE_PASSING,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

		architecturalModelBuilder.addPortInterface(HANDLE_CLIENT_COMMANDS_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

		architecturalModelBuilder.addPortInterface(HANDLE_CLIENT_COMMANDS_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

	}

	@Override
	public void initializeModelArchitecturalComponents() throws ComponentNotFoundException, PortInterfaceNotFoundException {

		architecturalModelBuilder.addComponent(SERVER_COMPONENT, null, new ArrayList<String> (Arrays.asList(new String[] {"trial2"})), null, null);
		architecturalModelBuilder.addComponent(CLIENT_COMPONENT, null, null, null, null);

		HashSet<ProvidedPortInterface> geoLocatorComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		geoLocatorComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		architecturalModelBuilder.addComponent(GEO_LOCATOR_COMPONENT, SERVER_COMPONENT, new ArrayList<String> (Arrays.asList(new String[] {"trial1"})),
				geoLocatorComponentProvidedInterfaces, null);

		HashSet<ProvidedPortInterface> socialNetworkComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		socialNetworkComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				REGISTER_USER_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				AUTHORIZE_USER_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				POST_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				REPLY_TO_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				DELETE_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				SOCIAL_NETWORK_STATE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));

		HashSet<RequiredPortInterface> socialNetworkComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		socialNetworkComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				MESSAGE_INFORMATION_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		socialNetworkComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				MESSAGE_REPLIES_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		socialNetworkComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				USER_INFORMATION_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		socialNetworkComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				USER_ACCOUNT_TYPE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		socialNetworkComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				USER_SUBSCRIPTION_FEE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));

		architecturalModelBuilder.addComponent(SOCIAL_NETWORK_COMPONENT, SERVER_COMPONENT, null,
				socialNetworkComponentProvidedInterfaces, socialNetworkComponentRequiredInterfaces);

		HashSet<ProvidedPortInterface> messageComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		messageComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				MESSAGE_INFORMATION_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		messageComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(MESSAGE_REPLIES_PORT,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));

		architecturalModelBuilder.addComponent(MESSAGE_COMPONENT, SERVER_COMPONENT, null,
				messageComponentProvidedInterfaces, null);

		HashSet<ProvidedPortInterface> userComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		userComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(USER_INFORMATION_PORT,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		userComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(USER_ACCOUNT_TYPE_PORT,
				PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		userComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				USER_SUBSCRIPTION_FEE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));

		architecturalModelBuilder.addComponent(USER_COMPONENT, SERVER_COMPONENT, null, userComponentProvidedInterfaces,
				null);

		HashSet<RequiredPortInterface> clientHandlerControllerComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		clientHandlerControllerComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				REGISTER_USER_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		clientHandlerControllerComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				AUTHORIZE_USER_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		clientHandlerControllerComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				POST_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		clientHandlerControllerComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				REPLY_TO_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		clientHandlerControllerComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				DELETE_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC));
		clientHandlerControllerComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				SOCIAL_NETWORK_STATE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		clientHandlerControllerComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				SOCKET_PORT, PortInterfaceCommunicationType.MESSAGE_PASSING,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK));

		architecturalModelBuilder.addComponent(CLIENT_HANDLER_COMPONENT, SERVER_COMPONENT, null, null,
				clientHandlerControllerComponentRequiredInterfaces);

		HashSet<ProvidedPortInterface> serverHandlerControllerComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		serverHandlerControllerComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				SOCKET_PORT, PortInterfaceCommunicationType.MESSAGE_PASSING,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK));
		serverHandlerControllerComponentProvidedInterfaces.add(architecturalModelBuilder.getProvidedPortInterface(
				HANDLE_CLIENT_COMMANDS_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK));

		architecturalModelBuilder.addComponent(SERVER_HANDLER_COMPONENT, CLIENT_COMPONENT, null,
				serverHandlerControllerComponentProvidedInterfaces, null);

		HashSet<RequiredPortInterface> uiComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		uiComponentRequiredInterfaces.add(architecturalModelBuilder.getRequiredPortInterface(
				HANDLE_CLIENT_COMMANDS_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK));

		architecturalModelBuilder.addComponent(UI_COMPONENT, CLIENT_COMPONENT, null, null,
				uiComponentRequiredInterfaces);

	}

	@Override
	public void initializeModelComponentsCommunicationLinks()
			throws ComponentNotFoundException, PortInterfaceNotDefinedInComponentException, PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException {
		architecturalModelBuilder.addCommunicationLink(CLIENT_COMPONENT, SERVER_COMPONENT, SERVER_HANDLER_COMPONENT, SOCKET_PORT,
				 PortInterfaceCommunicationType.MESSAGE_PASSING,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

		architecturalModelBuilder.addCommunicationLink(GEO_LOCATOR_COMPONENT, SOCIAL_NETWORK_COMPONENT, null,
				IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);
		
		architecturalModelBuilder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null,
				REGISTER_USER_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);
		architecturalModelBuilder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null,
				AUTHORIZE_USER_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);
		architecturalModelBuilder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null,
				POST_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);
		architecturalModelBuilder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null,
				REPLY_TO_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);
		architecturalModelBuilder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null,
				DELETE_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);
		architecturalModelBuilder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null,
				SOCIAL_NETWORK_STATE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		
		architecturalModelBuilder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null,
				USER_INFORMATION_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		architecturalModelBuilder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null,
				USER_ACCOUNT_TYPE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		architecturalModelBuilder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null,
				USER_SUBSCRIPTION_FEE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		
		architecturalModelBuilder.addCommunicationLink(MESSAGE_COMPONENT, SOCIAL_NETWORK_COMPONENT, null,
				MESSAGE_INFORMATION_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		architecturalModelBuilder.addCommunicationLink(MESSAGE_COMPONENT, SOCIAL_NETWORK_COMPONENT, null,
				MESSAGE_REPLIES_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		
		architecturalModelBuilder.addCommunicationLink(SERVER_HANDLER_COMPONENT, UI_COMPONENT, null,
				HANDLE_CLIENT_COMMANDS_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);
		
	}

	@Override
	public void initializeModelConcurrentComponentsMap() {
		// architecturalModelBuilder.addConcurrentComponentsEntry(GUI_COMPONENT,
		// new ArrayList<String> (Arrays.asList(new String[] {
		// SHAPES_MODEL_COMPONENT })));
		// architecturalModelBuilder.addConcurrentComponentsEntry(SHAPES_MODEL_COMPONENT,
		// new ArrayList<String> (Arrays.asList(new String[] { GUI_COMPONENT
		// })));
	}

}
