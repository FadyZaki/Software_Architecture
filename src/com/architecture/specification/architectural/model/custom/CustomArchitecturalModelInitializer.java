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
import com.architecture.specification.library.architectural.style.InMemoryArchitecturalStyles;
import com.architecture.specification.library.exceptions.ArchitecturalStyleException;
import com.architecture.specification.library.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.library.exceptions.ComponentNotFoundException;
import com.architecture.specification.library.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.library.exceptions.PortInterfaceNotFoundException;

public class CustomArchitecturalModelInitializer extends ArchitecturalModelInitializer {

	private static String SERVER_COMPONENT = "Server Component";
	private static String GEO_LOCATOR_COMPONENT = "Geo Locator Component";
	private static String IP2LOCATION_COMPONENT = "IP to Location Component";
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

	public CustomArchitecturalModelInitializer(ArchitecturalModelBuilder builder) {
		super(builder);
	}

	@Override
	public void initializeModelPortInterfaces() {
		builder.addPortInterface(IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT, PortInterfaceType.PROVIDED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT, PortInterfaceType.REQUIRED,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(REGISTER_USER_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(REGISTER_USER_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(AUTHORIZE_USER_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(AUTHORIZE_USER_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(POST_MESSAGE_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(POST_MESSAGE_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(REPLY_TO_MESSAGE_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(REPLY_TO_MESSAGE_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(DELETE_MESSAGE_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(DELETE_MESSAGE_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(SOCIAL_NETWORK_STATE_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(SOCIAL_NETWORK_STATE_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(MESSAGE_INFORMATION_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(MESSAGE_INFORMATION_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(MESSAGE_REPLIES_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(MESSAGE_REPLIES_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(USER_INFORMATION_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(USER_INFORMATION_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(USER_ACCOUNT_TYPE_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(USER_ACCOUNT_TYPE_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(USER_SUBSCRIPTION_FEE_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(USER_SUBSCRIPTION_FEE_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(SOCKET_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.MESSAGE_PASSING,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

		builder.addPortInterface(SOCKET_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.MESSAGE_PASSING,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

		builder.addPortInterface(HANDLE_CLIENT_COMMANDS_PORT, PortInterfaceType.PROVIDED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

		builder.addPortInterface(HANDLE_CLIENT_COMMANDS_PORT, PortInterfaceType.REQUIRED, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

	}

	@Override
	public void initializeModelArchitecturalComponents() throws ComponentNotFoundException, PortInterfaceNotFoundException {

		builder.addComponent(SERVER_COMPONENT, null, new ArrayList<String>(Arrays.asList(new String[] { "trial2" })), null, null, null, null, false);
		builder.addComponent(CLIENT_COMPONENT, null, null, null, null, null, null, false);

		HashSet<ProvidedPortInterface> geoLocatorComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		geoLocatorComponentProvidedInterfaces.add(builder.getProvidedPortInterface(IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		builder.addComponent(GEO_LOCATOR_COMPONENT, SERVER_COMPONENT, new ArrayList<String>(Arrays.asList(new String[] { "trial1" })),
				geoLocatorComponentProvidedInterfaces, null, null, null, false);

		builder.addComponent(IP2LOCATION_COMPONENT, null, new ArrayList<>(Arrays.asList(new String[] { "com.ip2location.IP2Location", "com.ip2location.IPResult" })), null, null, null, null, true);
		

		HashSet<ProvidedPortInterface> socialNetworkComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(REGISTER_USER_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(AUTHORIZE_USER_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(POST_MESSAGE_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(REPLY_TO_MESSAGE_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(DELETE_MESSAGE_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(SOCIAL_NETWORK_STATE_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));

		HashSet<RequiredPortInterface> socialNetworkComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(MESSAGE_INFORMATION_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(MESSAGE_REPLIES_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(USER_INFORMATION_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(USER_ACCOUNT_TYPE_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(USER_SUBSCRIPTION_FEE_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));

		builder.addComponent(SOCIAL_NETWORK_COMPONENT, SERVER_COMPONENT, null, socialNetworkComponentProvidedInterfaces,
				socialNetworkComponentRequiredInterfaces, null, null, false);

		HashSet<ProvidedPortInterface> messageComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		messageComponentProvidedInterfaces.add(builder.getProvidedPortInterface(MESSAGE_INFORMATION_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		messageComponentProvidedInterfaces.add(builder.getProvidedPortInterface(MESSAGE_REPLIES_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));

		builder.addComponent(MESSAGE_COMPONENT, SERVER_COMPONENT, null, messageComponentProvidedInterfaces, null, null, null, false);

		HashSet<ProvidedPortInterface> userComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		userComponentProvidedInterfaces.add(builder.getProvidedPortInterface(USER_INFORMATION_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		userComponentProvidedInterfaces.add(builder.getProvidedPortInterface(USER_ACCOUNT_TYPE_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		userComponentProvidedInterfaces.add(builder.getProvidedPortInterface(USER_SUBSCRIPTION_FEE_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));

		builder.addComponent(USER_COMPONENT, SERVER_COMPONENT, null, userComponentProvidedInterfaces, null, null, null, false);

		HashSet<RequiredPortInterface> clientHandlerControllerComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		clientHandlerControllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(REGISTER_USER_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		clientHandlerControllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(AUTHORIZE_USER_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		clientHandlerControllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(POST_MESSAGE_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		clientHandlerControllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(REPLY_TO_MESSAGE_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		clientHandlerControllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(DELETE_MESSAGE_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC));
		clientHandlerControllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(SOCIAL_NETWORK_STATE_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK));
		clientHandlerControllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(SOCKET_PORT,
				PortInterfaceCommunicationType.MESSAGE_PASSING, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK));

		builder.addComponent(CLIENT_HANDLER_COMPONENT, SERVER_COMPONENT, null, null, clientHandlerControllerComponentRequiredInterfaces, null, null, false);

		HashSet<ProvidedPortInterface> serverHandlerControllerComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		serverHandlerControllerComponentProvidedInterfaces.add(builder.getProvidedPortInterface(SOCKET_PORT,
				PortInterfaceCommunicationType.MESSAGE_PASSING, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK));
		serverHandlerControllerComponentProvidedInterfaces.add(builder.getProvidedPortInterface(HANDLE_CLIENT_COMMANDS_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK));

		builder.addComponent(SERVER_HANDLER_COMPONENT, CLIENT_COMPONENT, null, serverHandlerControllerComponentProvidedInterfaces, null, null, null, false);

		HashSet<RequiredPortInterface> uiComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		uiComponentRequiredInterfaces.add(builder.getRequiredPortInterface(HANDLE_CLIENT_COMMANDS_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK));

		builder.addComponent(UI_COMPONENT, CLIENT_COMPONENT, null, null, uiComponentRequiredInterfaces, null, null, false);

	}

	@Override
	public void initializeModelComponentsCommunicationLinks() throws ComponentNotFoundException, PortInterfaceNotDefinedInComponentException,
			PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException {
		builder.addCommunicationLink(CLIENT_COMPONENT, SERVER_COMPONENT, SERVER_HANDLER_COMPONENT, SOCKET_PORT,
				PortInterfaceCommunicationType.MESSAGE_PASSING, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

		builder.addCommunicationLink(GEO_LOCATOR_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, REGISTER_USER_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, AUTHORIZE_USER_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, POST_MESSAGE_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, REPLY_TO_MESSAGE_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, DELETE_MESSAGE_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, SOCIAL_NETWORK_STATE_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, USER_INFORMATION_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		builder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, USER_ACCOUNT_TYPE_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		builder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, USER_SUBSCRIPTION_FEE_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addCommunicationLink(MESSAGE_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, MESSAGE_INFORMATION_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		builder.addCommunicationLink(MESSAGE_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, MESSAGE_REPLIES_PORT,
				PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addCommunicationLink(SERVER_HANDLER_COMPONENT, UI_COMPONENT, null, HANDLE_CLIENT_COMMANDS_PORT,
				PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

	}

	@Override
	public void addConstraintsToModel() {
		return;
		
	}

	@Override
	public void addStylesToModel() throws ArchitecturalStyleException {
		builder.addModelCompliantStyle(InMemoryArchitecturalStyles.SERVER_CLIENT_STYLE);
	}

}
