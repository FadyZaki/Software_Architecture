package com.architecture.specification.architectural.model.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.architecture.specification.library.architectural.model.intended.builder.ArchitecturalModelBuilder;
import com.architecture.specification.library.architectural.model.intended.initializer.ArchitecturalModelInitializer;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterfaceCommunicationSynchronizationType;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterfaceCommunicationType;
import com.architecture.specification.library.architectural.model.intended.portinterface.PortInterfaceType;
import com.architecture.specification.library.architectural.model.intended.portinterface.ProvidedPortInterface;
import com.architecture.specification.library.architectural.model.intended.portinterface.RequiredPortInterface;
import com.architecture.specification.library.architectural.style.InMemoryArchitecturalStyles;
import com.architecture.specification.library.exceptions.ArchitecturalStyleException;
import com.architecture.specification.library.exceptions.ComponentNotDescendantOfAnotherException;
import com.architecture.specification.library.exceptions.ComponentNotFoundException;
import com.architecture.specification.library.exceptions.PortInterfaceNotDefinedInComponentException;
import com.architecture.specification.library.exceptions.PortInterfaceNotFoundException;

public class CustomArchitecturalModelInitializer extends ArchitecturalModelInitializer {

	private static final String SERVER_COMPONENT = "Server Component";
	private static final String GEO_LOCATOR_COMPONENT = "Geo Locator Component";
	private static final String IP2LOCATION_COMPONENT = "IP to Location Library Component";
	private static final String SOCIAL_NETWORK_COMPONENT = "Social Network Component";
	private static final String CLIENT_HANDLER_COMPONENT = "Client Handler Component";
	private static final String MESSAGE_COMPONENT = "Message Component";
	private static final String USER_COMPONENT = "User Component";

	private static final String CLIENT_COMPONENT = "Client Component";
	private static final String SERVER_HANDLER_COMPONENT = "Server Handler Component";
	private static final String CONTROLLER_COMPONENT = "Controller Component";
	private static final String UI_COMPONENT = "UI Component";
	private static final String MODEL_COMPONENT = "Model Component";

	private static final String IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT = "identifyGeoLocationUsingIpAddress";

	private static final String IP_QUERY_PORT = "IPQuery";
	private static final String COUNTRY_LONG_PORT = "countryLong";
	private static final String CITY_PORT = "city";

	private static final String REGISTER_USER_PORT = "registerUser";
	private static final String AUTHORIZE_USER_PORT = "authorizeUser";
	private static final String POST_MESSAGE_PORT = "postMessage";
	private static final String REPLY_TO_MESSAGE_PORT = "replyToMessage";
	private static final String RETRIEVE_LATEST_VERSION_PORT = "retrieveLatestVersion";
	private static final String ADJUST_USER_GEO_LOCATION_PORT = "adjustUserGeoLocation";
	private static final String RETRIEVE_USER_ACCOUNT_TYPE_PORT = "retrieveUserAccountType";
	private static final String RETRIEVE_USER_SUBSCRIPTION_FEE_PORT = "retrieveUserSubscriptionFee";

	private static final String RETRIEVE_ALL_MESSAGE_INFORMATION_PORT = "retrieveAllMessageInformation";
	private static final String ADD_REPLY_PORT = "addReply";
	private static final String MESSAGE_ID_PORT = "messageId";

	private static final String CURRENT_GEO_LOCATION_PORT = "currentGeoLocation";
	private static final String ACCOUNT_TYPE_PORT = "accountType";
	private static final String SUBSCRIPTION_FEE_PORT = "subscriptionFee";
	private static final String USERNAME_PORT = "username";
	private static final String PASSWORD_PORT = "password";

	private static final String SOCKET_PORT = "socket";

	private static final String BUILD_CLIENT_COMMANDS_PORT = "buildClientCommands";
	private static final String CONFIRM_REGISTRATION_PORT = "confirmRegistration";
	private static final String CONFIRM_AUTHORIZATION_PORT = "confirmAuthorization";
	private static final String DECLARE_UNAUTHORIZATION_PORT = "declareUnauthorization";
	private static final String UPDATE_SOCIAL_NETWORK_STATE_PORT = "updateSocialNetworkState";

	private static final String USER_ACCOUNT_TYPE_PORT = "userAccountType";
	private static final String USER_SUBSCRIPTION_FEE_PORT = "userSubscriptionFee";
	private static final String USER_LOGIN_STATE_PORT = "userLoginState";
	private static final String CURRENT_SOCIAL_NETWORK_STATE_PORT = "currentSocialNetworkState";

	private static final String ADJUST_GUI_COMPONENT_PORT = "adjustGuiComponent";
	private static final String DISPLAY_SUCCESSFUL_LOGIN_PORT = "displaySuccessfulLogin";
	private static final String DISPLAY_UNSUCCESSFUL_LOGIN_PORT = "displayUnsuccessfulLogin";
	private static final String DISPLAY_SOCIAL_NETWORK_MESSAGES_PORT = "displaySocialNetworkMessages";

	private static final String HANDLE_CLIENT_COMMANDS_PORT = "handleClientCommands";

	public CustomArchitecturalModelInitializer(ArchitecturalModelBuilder builder) {
		super(builder);
	}

	@Override
	public void initializeModelPortInterfaces() {
		builder.addPortInterface(IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(IP_QUERY_PORT, PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(COUNTRY_LONG_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(CITY_PORT, PortInterfaceCommunicationType.SHARED_DATA, PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(REGISTER_USER_PORT, PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(AUTHORIZE_USER_PORT, PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(POST_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(REPLY_TO_MESSAGE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(RETRIEVE_LATEST_VERSION_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(RETRIEVE_USER_ACCOUNT_TYPE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(RETRIEVE_USER_SUBSCRIPTION_FEE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(ADJUST_USER_GEO_LOCATION_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(RETRIEVE_ALL_MESSAGE_INFORMATION_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(ADD_REPLY_PORT, PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(MESSAGE_ID_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(USERNAME_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(PASSWORD_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(CURRENT_GEO_LOCATION_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(ACCOUNT_TYPE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(SUBSCRIPTION_FEE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(SOCKET_PORT, PortInterfaceCommunicationType.MESSAGE_PASSING,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_CALLBACK);

		builder.addPortInterface(HANDLE_CLIENT_COMMANDS_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(ADJUST_GUI_COMPONENT_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(DISPLAY_SUCCESSFUL_LOGIN_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(DISPLAY_UNSUCCESSFUL_LOGIN_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(DISPLAY_SOCIAL_NETWORK_MESSAGES_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

		builder.addPortInterface(BUILD_CLIENT_COMMANDS_PORT, PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(CONFIRM_REGISTRATION_PORT, PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(CONFIRM_AUTHORIZATION_PORT, PortInterfaceCommunicationType.TASK_EXECUTION, PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(DECLARE_UNAUTHORIZATION_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(UPDATE_SOCIAL_NETWORK_STATE_PORT, PortInterfaceCommunicationType.TASK_EXECUTION,
				PortInterfaceCommunicationSynchronizationType.SYNC);

		builder.addPortInterface(USER_ACCOUNT_TYPE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		builder.addPortInterface(USER_SUBSCRIPTION_FEE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		builder.addPortInterface(USER_LOGIN_STATE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);
		builder.addPortInterface(CURRENT_SOCIAL_NETWORK_STATE_PORT, PortInterfaceCommunicationType.SHARED_DATA,
				PortInterfaceCommunicationSynchronizationType.ASYNC_WITH_NO_CALLBACK);

	}

	@Override
	public void initializeModelArchitecturalComponents() throws ComponentNotFoundException, PortInterfaceNotFoundException {

		HashSet<RequiredPortInterface> serverComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		serverComponentRequiredInterfaces.add(builder.getRequiredPortInterface(SOCKET_PORT));
		serverComponentRequiredInterfaces.add(builder.getRequiredPortInterface(IP_QUERY_PORT));
		serverComponentRequiredInterfaces.add(builder.getRequiredPortInterface(CITY_PORT));
		serverComponentRequiredInterfaces.add(builder.getRequiredPortInterface(COUNTRY_LONG_PORT));
		
		builder.addComponent(SERVER_COMPONENT, null,
				new ArrayList<String>(Arrays.asList(new String[] { "com.practicalfour.socialnetwork.server.SocialNetworkServerMain" })), null,
				serverComponentRequiredInterfaces, null, null, false);

		
		HashSet<ProvidedPortInterface> clientComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		clientComponentProvidedInterfaces.add(builder.getProvidedPortInterface(SOCKET_PORT));
		builder.addComponent(CLIENT_COMPONENT, null,
				new ArrayList<String>(Arrays.asList(new String[] { "com.practicalfour.socialnetwork.client.SocialNetworkClientMain" })),
				clientComponentProvidedInterfaces, null, null, null, false);

		HashSet<ProvidedPortInterface> geoLocatorComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		geoLocatorComponentProvidedInterfaces.add(builder.getProvidedPortInterface(IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT));

		builder.addComponent(GEO_LOCATOR_COMPONENT, SERVER_COMPONENT,
				new ArrayList<String>(
						Arrays.asList(new String[] { "com.practicalfour.geolocation.GeoLocation", "com.practicalfour.geolocation.GeoLocatorUtility" })),
				geoLocatorComponentProvidedInterfaces, null, null, null, false);

		HashSet<ProvidedPortInterface> ip2LocationComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		ip2LocationComponentProvidedInterfaces.add(builder.getProvidedPortInterface(IP_QUERY_PORT));
		ip2LocationComponentProvidedInterfaces.add(builder.getProvidedPortInterface(COUNTRY_LONG_PORT));
		ip2LocationComponentProvidedInterfaces.add(builder.getProvidedPortInterface(CITY_PORT));
		builder.addComponent(IP2LOCATION_COMPONENT, null,
				new ArrayList<>(Arrays.asList(new String[] { "com.ip2location.IP2Location", "com.ip2location.IPResult" })),
				ip2LocationComponentProvidedInterfaces, null, null, null, true);

		HashSet<ProvidedPortInterface> socialNetworkComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(REGISTER_USER_PORT));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(AUTHORIZE_USER_PORT));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(POST_MESSAGE_PORT));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(REPLY_TO_MESSAGE_PORT));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(RETRIEVE_LATEST_VERSION_PORT));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(RETRIEVE_USER_ACCOUNT_TYPE_PORT));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(RETRIEVE_USER_SUBSCRIPTION_FEE_PORT));
		socialNetworkComponentProvidedInterfaces.add(builder.getProvidedPortInterface(ADJUST_USER_GEO_LOCATION_PORT));

		HashSet<RequiredPortInterface> socialNetworkComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(RETRIEVE_ALL_MESSAGE_INFORMATION_PORT));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(ADD_REPLY_PORT));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(MESSAGE_ID_PORT));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(ACCOUNT_TYPE_PORT));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(SUBSCRIPTION_FEE_PORT));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(CURRENT_GEO_LOCATION_PORT));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(USERNAME_PORT));
		socialNetworkComponentRequiredInterfaces.add(builder.getRequiredPortInterface(PASSWORD_PORT));

		builder.addComponent(SOCIAL_NETWORK_COMPONENT, SERVER_COMPONENT,
				new ArrayList<>(Arrays.asList(new String[] { "com.practicalfour.socialnetwork.SocialNetwork" })), socialNetworkComponentProvidedInterfaces,
				socialNetworkComponentRequiredInterfaces, null, null, false);

		HashSet<ProvidedPortInterface> messageComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		messageComponentProvidedInterfaces.add(builder.getProvidedPortInterface(RETRIEVE_ALL_MESSAGE_INFORMATION_PORT));
		messageComponentProvidedInterfaces.add(builder.getProvidedPortInterface(ADD_REPLY_PORT));
		messageComponentProvidedInterfaces.add(builder.getProvidedPortInterface(MESSAGE_ID_PORT));

		HashSet<RequiredPortInterface> messageComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		messageComponentRequiredInterfaces.add(builder.getRequiredPortInterface(USERNAME_PORT));
		messageComponentRequiredInterfaces.add(builder.getRequiredPortInterface(CURRENT_GEO_LOCATION_PORT));

		builder.addComponent(MESSAGE_COMPONENT, SERVER_COMPONENT,
				new ArrayList<>(Arrays.asList(new String[] { "com.practicalfour.socialnetwork.message.Message" })), messageComponentProvidedInterfaces,
				messageComponentRequiredInterfaces, null, null, false);

		HashSet<ProvidedPortInterface> userComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		userComponentProvidedInterfaces.add(builder.getProvidedPortInterface(ACCOUNT_TYPE_PORT));
		userComponentProvidedInterfaces.add(builder.getProvidedPortInterface(SUBSCRIPTION_FEE_PORT));
		userComponentProvidedInterfaces.add(builder.getProvidedPortInterface(CURRENT_GEO_LOCATION_PORT));
		userComponentProvidedInterfaces.add(builder.getProvidedPortInterface(USERNAME_PORT));
		userComponentProvidedInterfaces.add(builder.getProvidedPortInterface(PASSWORD_PORT));

		builder.addComponent(USER_COMPONENT, SERVER_COMPONENT,
				new ArrayList<>(Arrays.asList(new String[] { "com.practicalfour.socialnetwork.user.User", "com.practicalfour.socialnetwork.user.GuestUser",
						"com.practicalfour.socialnetwork.user.PremiumUser", "com.practicalfour.socialnetwork.user.PrivilegedUser",
						"com.practicalfour.socialnetwork.user.AdministratorUser" })),
				userComponentProvidedInterfaces, null, null, null, false);

		HashSet<RequiredPortInterface> clientHandlerComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		clientHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(REGISTER_USER_PORT));
		clientHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(AUTHORIZE_USER_PORT));
		clientHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(POST_MESSAGE_PORT));
		clientHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(REPLY_TO_MESSAGE_PORT));
		clientHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(RETRIEVE_USER_ACCOUNT_TYPE_PORT));
		clientHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(RETRIEVE_USER_SUBSCRIPTION_FEE_PORT));
		clientHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(RETRIEVE_LATEST_VERSION_PORT));
		clientHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(ADJUST_USER_GEO_LOCATION_PORT));

		builder.addComponent(CLIENT_HANDLER_COMPONENT, SERVER_COMPONENT, new ArrayList<>(Arrays.asList(new String[] {
				"com.practicalfour.socialnetwork.server.SocialNetworkServerConnectionHandler", "com.practicalfour.socialnetwork.server.SocialNetworkServer" })),
				null, clientHandlerComponentRequiredInterfaces, null, null, false);

		HashSet<ProvidedPortInterface> serverHandlerComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		serverHandlerComponentProvidedInterfaces.add(builder.getProvidedPortInterface(SOCKET_PORT));
		serverHandlerComponentProvidedInterfaces.add(builder.getProvidedPortInterface(BUILD_CLIENT_COMMANDS_PORT));

		HashSet<RequiredPortInterface> serverHandlerComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		serverHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(CONFIRM_AUTHORIZATION_PORT));
		serverHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(CONFIRM_REGISTRATION_PORT));
		serverHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(DECLARE_UNAUTHORIZATION_PORT));
		serverHandlerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(UPDATE_SOCIAL_NETWORK_STATE_PORT));

		builder.addComponent(SERVER_HANDLER_COMPONENT, CLIENT_COMPONENT,
				new ArrayList<>(Arrays.asList(new String[] { "com.practicalfour.socialnetwork.client.SocialNetworkClientConnectionHandler" })),
				serverHandlerComponentProvidedInterfaces, serverHandlerComponentRequiredInterfaces, null, null, false);

		HashSet<ProvidedPortInterface> controllerComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		controllerComponentProvidedInterfaces.add(builder.getProvidedPortInterface(CONFIRM_REGISTRATION_PORT));
		controllerComponentProvidedInterfaces.add(builder.getProvidedPortInterface(CONFIRM_AUTHORIZATION_PORT));
		controllerComponentProvidedInterfaces.add(builder.getProvidedPortInterface(DECLARE_UNAUTHORIZATION_PORT));
		controllerComponentProvidedInterfaces.add(builder.getProvidedPortInterface(UPDATE_SOCIAL_NETWORK_STATE_PORT));
		controllerComponentProvidedInterfaces.add(builder.getProvidedPortInterface(HANDLE_CLIENT_COMMANDS_PORT));
		controllerComponentProvidedInterfaces.add(builder.getProvidedPortInterface(ADJUST_GUI_COMPONENT_PORT));

		HashSet<RequiredPortInterface> controllerComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		controllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(BUILD_CLIENT_COMMANDS_PORT));
		controllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(DISPLAY_SUCCESSFUL_LOGIN_PORT));
		controllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(DISPLAY_UNSUCCESSFUL_LOGIN_PORT));
		controllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(DISPLAY_SOCIAL_NETWORK_MESSAGES_PORT));
		controllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(USER_ACCOUNT_TYPE_PORT));
		controllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(USER_SUBSCRIPTION_FEE_PORT));
		controllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(USER_LOGIN_STATE_PORT));
		controllerComponentRequiredInterfaces.add(builder.getRequiredPortInterface(CURRENT_SOCIAL_NETWORK_STATE_PORT));

		builder.addComponent(CONTROLLER_COMPONENT, CLIENT_COMPONENT,
				new ArrayList<>(Arrays.asList(new String[] { "com.practicalfour.socialnetwork.client.SocialNetworkClientController" })),
				controllerComponentProvidedInterfaces, controllerComponentRequiredInterfaces, null, null, false);

		HashSet<ProvidedPortInterface> UIComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		UIComponentProvidedInterfaces.add(builder.getProvidedPortInterface(DISPLAY_SUCCESSFUL_LOGIN_PORT));
		UIComponentProvidedInterfaces.add(builder.getProvidedPortInterface(DISPLAY_UNSUCCESSFUL_LOGIN_PORT));
		UIComponentProvidedInterfaces.add(builder.getProvidedPortInterface(DISPLAY_SOCIAL_NETWORK_MESSAGES_PORT));

		HashSet<RequiredPortInterface> UIComponentRequiredInterfaces = new HashSet<RequiredPortInterface>();
		UIComponentRequiredInterfaces.add(builder.getRequiredPortInterface(HANDLE_CLIENT_COMMANDS_PORT));
		UIComponentRequiredInterfaces.add(builder.getRequiredPortInterface(ADJUST_GUI_COMPONENT_PORT));

		builder.addComponent(UI_COMPONENT, CLIENT_COMPONENT,
				new ArrayList<>(Arrays.asList(new String[] { "com.practicalfour.socialnetwork.client.SocialNetworkClientGUI" })), UIComponentProvidedInterfaces,
				UIComponentRequiredInterfaces, null, null, false);

		HashSet<ProvidedPortInterface> modelComponentProvidedInterfaces = new HashSet<ProvidedPortInterface>();
		modelComponentProvidedInterfaces.add(builder.getProvidedPortInterface(USER_ACCOUNT_TYPE_PORT));
		modelComponentProvidedInterfaces.add(builder.getProvidedPortInterface(USER_SUBSCRIPTION_FEE_PORT));
		modelComponentProvidedInterfaces.add(builder.getProvidedPortInterface(USER_LOGIN_STATE_PORT));
		modelComponentProvidedInterfaces.add(builder.getProvidedPortInterface(CURRENT_SOCIAL_NETWORK_STATE_PORT));

		builder.addComponent(MODEL_COMPONENT, CLIENT_COMPONENT,
				new ArrayList<>(Arrays.asList(new String[] { "com.practicalfour.socialnetwork.client.SocialNetworkClientModel" })),
				modelComponentProvidedInterfaces, null, null, null, false);

	}

	@Override
	public void initializeModelComponentsCommunicationLinks() throws ComponentNotFoundException, PortInterfaceNotDefinedInComponentException,
			PortInterfaceNotFoundException, ComponentNotDescendantOfAnotherException {
		builder.addCommunicationLink(CLIENT_COMPONENT, SERVER_COMPONENT, SERVER_HANDLER_COMPONENT, SOCKET_PORT);

		builder.addCommunicationLink(IP2LOCATION_COMPONENT, SERVER_COMPONENT, null, IP_QUERY_PORT);
		builder.addCommunicationLink(IP2LOCATION_COMPONENT, SERVER_COMPONENT, null, COUNTRY_LONG_PORT);
		builder.addCommunicationLink(IP2LOCATION_COMPONENT, SERVER_COMPONENT, null, CITY_PORT);

		builder.addCommunicationLink(GEO_LOCATOR_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, IDENTIFY_GEO_LOCATION_USING_IP_ADDRESS_PORT);

		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, REGISTER_USER_PORT);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, AUTHORIZE_USER_PORT);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, POST_MESSAGE_PORT);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, REPLY_TO_MESSAGE_PORT);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, RETRIEVE_USER_ACCOUNT_TYPE_PORT);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, RETRIEVE_USER_SUBSCRIPTION_FEE_PORT);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, RETRIEVE_LATEST_VERSION_PORT);
		builder.addCommunicationLink(SOCIAL_NETWORK_COMPONENT, CLIENT_HANDLER_COMPONENT, null, ADJUST_USER_GEO_LOCATION_PORT);

		builder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, ACCOUNT_TYPE_PORT);
		builder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, SUBSCRIPTION_FEE_PORT);
		builder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, CURRENT_GEO_LOCATION_PORT);
		builder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, USERNAME_PORT);
		builder.addCommunicationLink(USER_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, PASSWORD_PORT);
		builder.addCommunicationLink(USER_COMPONENT, MESSAGE_COMPONENT, null, CURRENT_GEO_LOCATION_PORT);
		builder.addCommunicationLink(USER_COMPONENT, MESSAGE_COMPONENT, null, USERNAME_PORT);

		builder.addCommunicationLink(MESSAGE_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, RETRIEVE_ALL_MESSAGE_INFORMATION_PORT);
		builder.addCommunicationLink(MESSAGE_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, ADD_REPLY_PORT);
		builder.addCommunicationLink(MESSAGE_COMPONENT, SOCIAL_NETWORK_COMPONENT, null, MESSAGE_ID_PORT);

		builder.addCommunicationLink(CONTROLLER_COMPONENT, UI_COMPONENT, null, HANDLE_CLIENT_COMMANDS_PORT);
		builder.addCommunicationLink(CONTROLLER_COMPONENT, UI_COMPONENT, null, ADJUST_GUI_COMPONENT_PORT);
		builder.addCommunicationLink(CONTROLLER_COMPONENT, SERVER_HANDLER_COMPONENT, null, CONFIRM_REGISTRATION_PORT);
		builder.addCommunicationLink(CONTROLLER_COMPONENT, SERVER_HANDLER_COMPONENT, null, CONFIRM_AUTHORIZATION_PORT);
		builder.addCommunicationLink(CONTROLLER_COMPONENT, SERVER_HANDLER_COMPONENT, null, DECLARE_UNAUTHORIZATION_PORT);
		builder.addCommunicationLink(CONTROLLER_COMPONENT, SERVER_HANDLER_COMPONENT, null, UPDATE_SOCIAL_NETWORK_STATE_PORT);

		builder.addCommunicationLink(SERVER_HANDLER_COMPONENT, CONTROLLER_COMPONENT, null, BUILD_CLIENT_COMMANDS_PORT);

		builder.addCommunicationLink(MODEL_COMPONENT, CONTROLLER_COMPONENT, null, USER_ACCOUNT_TYPE_PORT);
		builder.addCommunicationLink(MODEL_COMPONENT, CONTROLLER_COMPONENT, null, USER_SUBSCRIPTION_FEE_PORT);
		builder.addCommunicationLink(MODEL_COMPONENT, CONTROLLER_COMPONENT, null, USER_LOGIN_STATE_PORT);
		builder.addCommunicationLink(MODEL_COMPONENT, CONTROLLER_COMPONENT, null, CURRENT_SOCIAL_NETWORK_STATE_PORT);

		builder.addCommunicationLink(UI_COMPONENT, CONTROLLER_COMPONENT, null, DISPLAY_SUCCESSFUL_LOGIN_PORT);
		builder.addCommunicationLink(UI_COMPONENT, CONTROLLER_COMPONENT, null, DISPLAY_UNSUCCESSFUL_LOGIN_PORT);
		builder.addCommunicationLink(UI_COMPONENT, CONTROLLER_COMPONENT, null, DISPLAY_SOCIAL_NETWORK_MESSAGES_PORT);

	}

	@Override
	public void addConstraintsToModel() {
		return;
	}

	@Override
	public void addStylesToModel() throws ArchitecturalStyleException {
		// builder.addModelCompliantStyle(InMemoryArchitecturalStyles.SERVER_CLIENT_STYLE);
	}

}
