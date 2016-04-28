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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.ocl.EvaluationEnvironment;
import org.eclipse.ocl.ecore.EcoreEnvironment;
import org.eclipse.ocl.ecore.EcoreEnvironmentFactory;
import org.eclipse.ocl.ecore.EcoreEvaluationEnvironment;
import org.eclipse.ocl.ecore.OCL;
import org.eclipse.ocl.expressions.CollectionKind;
import org.eclipse.ocl.types.OCLStandardLibrary;
import org.eclipse.ocl.util.CollectionUtil;

/**
 * An extension of the Ecore environment factory for additional navigation
 * facilities
 */
public class PatternEnvironmentFactory extends EcoreEnvironmentFactory {

	/** The custom operations, identified by signature */
	protected final Map<OperationSignature, CustomOperation> _additionalOperations;

	protected OCLStandardLibrary<EClassifier> library;

	/**
	 * Default constructor
	 */
	public PatternEnvironmentFactory() {
		super();
		_additionalOperations = new HashMap<OperationSignature, CustomOperation>();
		OCL result = OCL.newInstance();
		library = result.getEnvironment().getOCLStandardLibrary();
	}

	public PatternEnvironmentFactory(Registry reg) {
		super(reg);
		_additionalOperations = new HashMap<OperationSignature, CustomOperation>();
		OCL result = OCL.newInstance();
		library = result.getEnvironment().getOCLStandardLibrary();
	}

	/**
	 * Add a "container" operation in the given environment
	 * 
	 * @param env
	 *            a non-null environment
	 */
	private void addCustomOperations(EcoreEnvironment env) {
		EClassifier everyElement = env.getOCLStandardLibrary().getOclAny();
		OCLStandardLibrary<EClassifier> lib = env.getOCLStandardLibrary();
		// Container
		registerCustomOperation(env, new CustomOperation(everyElement,
			"oclOwner", everyElement, false) { //$NON-NLS-1$

				/**
				 * @see org.polarsys.capella.ocl.requester.patterns.templates.CustomOperation#executeOn(java.lang.Object,
				 *      java.util.List)
				 */
				@Override
				public Object executeOn(Object source, List<Object> args) {
					return ((EObject) source).eContainer();
				}
			});
		// All containers
		registerCustomOperation(env, new CustomOperation(everyElement,
			"oclOwners", everyElement, true) { //$NON-NLS-1$

				@Override
				public Object executeOn(Object source, List<Object> args) {
					List<EObject> result = new ArrayList<EObject>();
					EObject current = ((EObject) source).eContainer();
					while (current != null) {
						result.add(current);
						current = current.eContainer();
					}
					return result;
				}
			});
		// Contents
		registerCustomOperation(env, new CustomOperation(everyElement,
			"oclChildren", everyElement, true) { //$NON-NLS-1$

				@Override
				public Object executeOn(Object source, List<Object> args) {
					return ((EObject) source).eContents();
				}
			});
		// All contents
		registerCustomOperation(env, new CustomOperation(everyElement,
			"oclAllChildren", everyElement, true) { //$NON-NLS-1$

				@Override
				public Object executeOn(Object source, List<Object> args) {
					List<EObject> result = new ArrayList<EObject>();
					Iterator<EObject> it = ((EObject) source).eAllContents();
					while (it.hasNext()) {
						result.add(it.next());
					}
					return result;
				}
			});
		// Is leaf
		registerCustomOperation(env, new CustomOperation(everyElement,
			"oclIsLeaf", lib.getBoolean(), false) { //$NON-NLS-1$

				@Override
				public Object executeOn(Object source, List<Object> args) {
					boolean result = ((EObject) source).eContents().isEmpty();
					return Boolean.valueOf(result);
				}
			});
		// Is root
		registerCustomOperation(env, new CustomOperation(everyElement,
			"oclIsRoot", lib.getBoolean(), false) { //$NON-NLS-1$

				@Override
				public Object executeOn(Object source, List<Object> args) {
					boolean result = ((EObject) source).eContainer() == null;
					return Boolean.valueOf(result);
				}
			});
		// Type name
		registerCustomOperation(env, new CustomOperation(everyElement,
			"oclTypeName", lib.getString(), false) { //$NON-NLS-1$

				@Override
				public Object executeOn(Object source, List<Object> args) {
					EClass type = ((EObject) source).eClass();
					EPackage epackage = type.getEPackage();
					return epackage.getName() + "::" + type.getName(); //$NON-NLS-1$
				}
			});
	}

	/**
	 * @see org.eclipse.ocl.ecore.EcoreEnvironmentFactory#createEnvironment()
	 */
	@Override
	public EcoreEnvironment createEnvironment() {
		EcoreEnvironment result = (EcoreEnvironment) super.createEnvironment();
		addCustomOperations(result);
		return result;
	}

	/**
	 * @see org.eclipse.ocl.ecore.EcoreEnvironmentFactory#createEvaluationEnvironment()
	 */
	@Override
	public ExtendedEcoreEvaluationEnvironment createEvaluationEnvironment() {
		return new ExtendedEcoreEvaluationEnvironment();
	}

	/**
	 * @see org.eclipse.ocl.ecore.EcoreEnvironmentFactory#createEvaluationEnvironment(org.eclipse.ocl.EvaluationEnvironment)
	 */
	@Override
	public ExtendedEcoreEvaluationEnvironment createEvaluationEnvironment(
			EvaluationEnvironment<EClassifier, EOperation, EStructuralFeature, EClass, EObject> parent) {
		return new ExtendedEcoreEvaluationEnvironment(parent);
	}

	/**
	 * Register the given custom operation to make it executable
	 * 
	 * @param env
	 *            a non-null environment
	 * @param customOperation
	 *            a non-null custom operation
	 */
	private void registerCustomOperation(EcoreEnvironment env,
			CustomOperation customOperation) {
		customOperation.defineIn(env);
		_additionalOperations.put(customOperation.getSignature(),
			customOperation);
	}

	/**
	 * An extension of the Ecore evaluation environment for additional
	 * navigation facilities
	 */
	private class ExtendedEcoreEvaluationEnvironment
			extends EcoreEvaluationEnvironment {

		/**
		 * Default constructor
		 */
		public ExtendedEcoreEvaluationEnvironment() {
			super();
		}

		/**
		 * Constructor
		 */
		public ExtendedEcoreEvaluationEnvironment(
				EvaluationEnvironment<EClassifier, EOperation, EStructuralFeature, EClass, EObject> parent) {
			super(parent);
		}

		/**
		 * @see org.eclipse.ocl.ecore.EcoreEvaluationEnvironment#callOperation(org.eclipse.emf.ecore.EOperation,
		 *      int, java.lang.Object, java.lang.Object[])
		 */
		@Override
		public Object callOperation(EOperation operation, int opcode,
				Object source, Object[] args)
				throws IllegalArgumentException {
			Object result = null;
			// Try custom operation
			OperationSignature signature = new OperationSignature(operation);
			CustomOperation customOperation = _additionalOperations
				.get(signature);
			if (customOperation != null) {
				result = customOperation.executeOn(source,
					Arrays.asList(args));
			}
			// Finish
			if (result != null) {
				result = coerceValue(operation, result, false);
			} else {
				result = super.callOperation(operation, opcode, source,
					args);
			}
			return result;
		}

		/**
		 * Duplicated from superclass for visibility reasons
		 * 
		 * @see EcoreEvaluationEnvironment#coerceValue(ETypedElement, Object,
		 *      boolean)
		 */
		@Override
		public Object coerceValue(ETypedElement element, Object value,
				boolean copy) {
			CollectionKind kind = getCollectionKind(element);
			if (kind != null) {
				if (value instanceof Collection<?>) {
					return copy
						? CollectionUtil.createNewCollection(kind,
							(Collection<?>) value)
						: value;
				}
				Collection<Object> result = CollectionUtil
					.createNewCollection(kind);
				result.add(value);
				return result;
			}
			if (value instanceof Collection<?>) {
				Collection<?> collection = (Collection<?>) value;
				return collection.isEmpty()
					? null
					: collection.iterator().next();
			}
			return value;
		}
	}
}