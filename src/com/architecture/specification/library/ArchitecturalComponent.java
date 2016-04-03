package com.architecture.specification.library;
import java.util.HashMap;
import java.util.HashSet;

public class ArchitecturalComponent {

	private final String componentIdentifier;
	private final ArchitecturalComponent parentComponent;
	private HashSet<String> componentClasses;
	private HashSet<ProvidedPortInterface> providedInterfaces;
	private HashSet<RequiredPortInterface> requiredInterfaces;
	
	private ArchitecturalComponent(Builder builder){
		this.componentIdentifier = builder.componentIdentifier;
		this.componentClasses = builder.componentClasses;
		this.providedInterfaces = builder.providedInterfaces;
		this.requiredInterfaces = builder.requiredInterfaces;
		this.parentComponent = builder.parentComponent;
	}
	
	public String getComponentIdentifier() {
		return componentIdentifier;
	}

	public HashSet<String> getComponentClasses() {
		return componentClasses;
	}
	
	public void addComponentClasses(HashSet<String> additionalClasses){
		componentClasses.addAll(additionalClasses);
	}

	public HashSet<ProvidedPortInterface> getProvidedInterfaces() {
		return providedInterfaces;
	}
	
	public void addProvidedInterfaces(HashSet<ProvidedPortInterface> additionalPorts) {
		providedInterfaces.addAll(additionalPorts);
	}
	
	public HashMap<String, ProvidedPortInterface> getProvidedInterfacesMap(){
		HashMap<String, ProvidedPortInterface> providedInterfacesMap = new HashMap<String, ProvidedPortInterface>();
		for(ProvidedPortInterface ppi : providedInterfaces){
			providedInterfacesMap.put(ppi.getPortInterfaceIdentifier(), ppi);
		}
		return providedInterfacesMap;
	}

	public HashSet<RequiredPortInterface> getRequiredInterfaces() {
		return requiredInterfaces;
	}
	
	public void addRequiredInterfaces(HashSet<RequiredPortInterface> additionalPorts) {
		requiredInterfaces.addAll(additionalPorts);
	}
	
	public HashMap<String, RequiredPortInterface> getRequiredInterfacesMap(){
		HashMap<String, RequiredPortInterface> requiredInterfacesMap = new HashMap<String, RequiredPortInterface>();
		for(RequiredPortInterface rpi : requiredInterfaces){
			requiredInterfacesMap.put(rpi.getPortInterfaceIdentifier(), rpi);
		}
		return requiredInterfacesMap;
	}
	
	public ArchitecturalComponent getParentComponent() {
		return parentComponent;
	}
	
	
	public static class Builder {
		
		private final String componentIdentifier;
		private HashSet<String> componentClasses;
		private HashSet<ProvidedPortInterface> providedInterfaces;
		private HashSet<RequiredPortInterface> requiredInterfaces;
		private ArchitecturalComponent parentComponent;
		
		public Builder(String componentIdentifier){
			this.componentIdentifier = componentIdentifier;
			this.componentClasses = new HashSet<String>();
			this.providedInterfaces = new HashSet<ProvidedPortInterface>();
			this.requiredInterfaces = new HashSet<RequiredPortInterface>();
			this.parentComponent = null;
		}
		
		public Builder componentClasses(HashSet<String> componentClasses) {
			this.componentClasses = componentClasses;
			return this;
		}
		
		public Builder parentComponent(ArchitecturalComponent parentComponent) {
			this.parentComponent = parentComponent;
			return this;
		}
		
		public Builder requiredPortInterfaces(HashSet<RequiredPortInterface> requiredInterfaces) {
			this.requiredInterfaces = requiredInterfaces;
			return this;
		}
		
		public Builder providedPortInterfaces(HashSet<ProvidedPortInterface> providedInterfaces) {
			this.providedInterfaces = providedInterfaces;
			return this;
		}
		
		public ArchitecturalComponent build(){
			return new ArchitecturalComponent(this);
		}
		
	}
	
	@Override
    public int hashCode() {
        return this.componentIdentifier.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof ArchitecturalComponent))
            return false;
        if (obj == this)
            return true;

        ArchitecturalComponent rhs = (ArchitecturalComponent) obj;
        return this.getComponentIdentifier().equals(rhs.componentIdentifier);
    }

}
