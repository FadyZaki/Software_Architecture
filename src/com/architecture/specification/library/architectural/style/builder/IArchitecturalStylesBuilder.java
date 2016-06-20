package com.architecture.specification.library.architectural.style.builder;

import com.architecture.specification.library.architectural.style.initializer.IArchitecturalStylesInitializer;

/**
 * Responsible for building styles using a styles initializer object
 * @author FadyShining
 *
 */
public interface IArchitecturalStylesBuilder {

	public void buildArchitecturalStyles(IArchitecturalStylesInitializer initializer);
}
