/*******************************************************************************
 * Copyright (c) 2010 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.ocl.requester.patterns.templates;

import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.ocl.ecore.EcoreEnvironment;
import org.eclipse.ocl.expressions.Variable;

/**
 * A specification of an OCL operation (signature and semantics).
 */
public abstract class CustomOperation {

	/** The non-null signature of the operation */
	private final OperationSignature _signature;

	/**
	 * Constructor
	 * 
	 * @param owner
	 *            a non-null classifier on which the operation is applicable
	 * @param name
	 *            a non-null string
	 * @param type
	 *            a non-null classifier for the return type of the operation
	 * @param isMany
	 *            whether the operation may return more than one element
	 */
	public CustomOperation(EClassifier owner, String name,
			EClassifier type, boolean isMany) {
		_signature = new OperationSignature(owner, name, type, isMany);
	}

	/**
	 * Constructor
	 * 
	 * @param owner
	 *            a non-null classifier on which the operation is applicable
	 * @param name
	 *            a non-null string
	 * @param type
	 *            a non-null classifier for the return type of the operation
	 * @param isMany
	 *            whether the operation may return more than one element
	 * @param parameters
	 *            a non-null, potentially empty list
	 */
	public CustomOperation(EClassifier owner, String name,
			EClassifier type, boolean isMany,
			List<Variable<EClassifier, EParameter>> parameters) {
		_signature = new OperationSignature(owner, name, type, isMany,
			parameters);
	}

	/**
	 * Define the operation in the given environment
	 * 
	 * @param env
	 *            a non-null environment
	 */
	public void defineIn(EcoreEnvironment env) {
		_signature.defineIn(env);
	}

	/**
	 * Execute the operation of the given receiver
	 * 
	 * @param source
	 *            a non-null object
	 * @param args
	 *            a non-null, potentially empty list
	 * @return a potentially null object
	 */
	public abstract Object executeOn(Object source, List<Object> args);

	/**
	 * Return the signature of the custom operation
	 * 
	 * @return a non-null operation signature
	 */
	public OperationSignature getSignature() {
		return _signature;
	}

}
