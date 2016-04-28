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

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.EcoreEnvironment;
import org.eclipse.ocl.expressions.Variable;

/**
 * The signature of a custom OCL operation. A convenient equals() method is
 * provided which only considers the name of the operation. This is because we
 * would need an Ecore / OCL type converter otherwise (e.g, EObject/OclAny).
 */
public class OperationSignature {

	/** The non-null classifier on which the operation is applicable */
	private final EClassifier _owner;

	/** The non-null name of the operation */
	private final String _name;

	/** The non-null return type of the operation */
	private final EClassifier _type;

	/** Whether the operation may return more than one element */
	private final boolean _isMany;

	/** The non-null, potentially empty list of parameters */
	private final List<Variable<EClassifier, EParameter>> _parameters;

	/**
	 * Full constructor
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
	public OperationSignature(EClassifier owner, String name,
			EClassifier type, boolean isMany,
			List<Variable<EClassifier, EParameter>> parameters) {
		_owner = owner;
		_name = name;
		_type = type;
		_isMany = isMany;
		_parameters = parameters;
	}

	/**
	 * Shorter constructor
	 * 
	 * @see OperationSignature#OperationSignature(EClassifier, String,
	 *      EClassifier, List)
	 */
	public OperationSignature(EClassifier owner, String name,
			EClassifier type, boolean isMany) {
		this(owner, name, type, isMany, Collections
			.<Variable<EClassifier, EParameter>> emptyList());
	}

	/**
	 * Operation-based constructor
	 * 
	 * @param operation
	 *            a non-null operation
	 */
	public OperationSignature(EOperation operation) {
		// Ignore parameters
		this(operation.getEContainingClass(), operation.getName(),
			operation.getEType(), operation.isMany());
	}

	/**
	 * Return a new constraint for creating a new operation
	 * 
	 * @return a non-null constraint
	 */
	private Constraint createConstraint() {
		return org.eclipse.ocl.ecore.EcoreFactory.eINSTANCE.createConstraint();
	}

	/**
	 * Define the signature as a new operation in the given environment
	 * 
	 * @param env
	 *            a non-null environment
	 */
	public void defineIn(EcoreEnvironment env) {
		EOperation op = env.defineOperation(getOwner(), getName(), getType(),
			getParameters(), createConstraint());
		if (_isMany)
			op.setUpperBound(-1);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof OperationSignature) {
			OperationSignature peer = (OperationSignature) obj;
			result = getName().equals(peer.getName());
		}
		return result;
	}

	/**
	 * Return the name of the operation
	 * 
	 * @return a non-null string
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Return the classifier on which the operation is applicable
	 * 
	 * @return a non-null classifier
	 */
	public EClassifier getOwner() {
		return _owner;
	}

	/**
	 * Return the parameters of this operation
	 * 
	 * @return a non-null, potentially empty, unmodifiable list
	 */
	public List<Variable<EClassifier, EParameter>> getParameters() {
		return Collections.unmodifiableList(_parameters);
	}

	/**
	 * Return the return type of the operation
	 * 
	 * @return a non-null classifier
	 */
	public EClassifier getType() {
		return _type;
	}

	/**
	 * Return whether the operation may return more than one element
	 */
	public boolean isMany() {
		return _isMany;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

}
