/**
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM - Initial API and implementation
 * Thales - Capella adaptation
 */
package org.polarsys.capella.ocl.requester.level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.helper.ConstraintKind;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.ocl.utilities.UMLReflection;
import org.polarsys.capella.ocl.requester.console.IOCLFactory;
import org.polarsys.capella.ocl.requester.internal.l10n.OCLInterpreterMessages;

/**
 * Enumeration of modeling levels on which we can define OCL expressions, in
 * accordance with the OMG modeling stack.
 * 
 * @author Christian W. Damus (cdamus)
 */
public enum ModelingLevel {
	M2, M2Simplified, M1;

	/**
	 * Set the context of the specified OCL helper to the given element (in the
	 * {@link #M1} case) or its metaclass ({@link #M2} case).
	 * 
	 * @param helper
	 *            an OCL helper
	 * @param factory
	 *            a factory to help in obtaining a metaclass, where needed
	 * @param element
	 *            the selected context element
	 * 
	 * @return the kind of constraint to parse or to complete
	 * 
	 * @throws ParserException
	 *             if the specified context is not valid for this modeling level
	 */
	@SuppressWarnings("unchecked")
	public <C, O, P> ConstraintKind setContext(OCLHelper<C, O, P, ?> helper,
			EObject element, IOCLFactory<C> factory)
			throws ParserException {
		switch (this) {
			case M2 :
			case M2Simplified :
				C contextClassifier = factory.getContextClassifier(element);
				if (contextClassifier == null) {
					throw new ParserException(
						OCLInterpreterMessages.console_badContextForQuery);
				}
				helper.setContext(contextClassifier);
				return ConstraintKind.INVARIANT;
			case M1 :
				UMLReflection<?, C, O, P, ?, ?, ?, ?, ?, ?> uml = helper
					.getOCL().getEnvironment().getUMLReflection();

				if (uml.isClassifier(element)) {
					C classifier = (C) element;
					helper.setContext(classifier);
					return ConstraintKind.INVARIANT;
				} else if (uml.isOperation(element)) {
					O operation = (O) element;
					C classifier = uml.getOwningClassifier(operation);
					helper.setOperationContext(classifier, operation);
					return ConstraintKind.POSTCONDITION;
				} else if (uml.isProperty(element)) {
					P property = (P) element;
					C classifier = uml.getOwningClassifier(property);
					helper.setAttributeContext(classifier, property);
					return ConstraintKind.DERIVATION;
				} else {
					throw new ParserException(
						OCLInterpreterMessages.console_badContextForConstraint);
				}
		}

		return null;
	}
}
