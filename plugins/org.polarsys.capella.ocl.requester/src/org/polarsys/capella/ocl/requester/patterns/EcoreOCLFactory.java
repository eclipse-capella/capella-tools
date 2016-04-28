/*******************************************************************************
 * Copyright (c) 2011 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.ocl.requester.patterns;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ocl.OCL;
import org.polarsys.capella.ocl.requester.console.DelegatingPackageRegistry;
import org.polarsys.capella.ocl.requester.console.IOCLFactory;
import org.polarsys.capella.ocl.requester.level.ModelingLevel;
import org.polarsys.capella.ocl.requester.patterns.templates.PatternEnvironmentFactory;

/**
 * Create a custom Ecore Factory to provide PatternEnvironment operations
 * 
 * @see PatternEnvironmentFactory
 */
public class EcoreOCLFactory implements IOCLFactory<Object> {

	/**
	 * The selected context.
	 */
	protected EObject context;

	/**
	 * Default constructor.
	 * 
	 * @param context
	 *            : the selected element from the model.
	 */
	public EcoreOCLFactory(EObject context) {
		super();
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public OCL<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> createOCL(ModelingLevel level) {
		List<EPackage.Registry> delegates = new ArrayList<EPackage.Registry>();
		delegates
			.add(context.eResource().getResourceSet().getPackageRegistry());
		delegates.add(EPackage.Registry.INSTANCE);

		return OCL.newInstance(new PatternEnvironmentFactory(
			new DelegatingPackageRegistry(delegates)));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public OCL<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> createOCL(
			ModelingLevel level, Resource res) {
		List<EPackage.Registry> delegates = new ArrayList<EPackage.Registry>();
		delegates
			.add(context.eResource().getResourceSet().getPackageRegistry());
		delegates.add(EPackage.Registry.INSTANCE);
		return OCL.newInstance(new PatternEnvironmentFactory(
			new DelegatingPackageRegistry(delegates)), res);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getContextClassifier(EObject object) {
		return context.eClass();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName(Object modelElement) {
		return ((ENamedElement) modelElement).getName();
	}
}