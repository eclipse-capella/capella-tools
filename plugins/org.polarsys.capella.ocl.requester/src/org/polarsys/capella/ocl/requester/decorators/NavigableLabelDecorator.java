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
package org.polarsys.capella.ocl.requester.decorators;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;
import org.polarsys.capella.ocl.requester.OCLRequesterPlugin;

/**
 * Used to decorate images in completion mechanism.
 */
public class NavigableLabelDecorator extends AbstractDecorator {

	/**
	 * @see AbstractDecorator#doDecorateImage(org.eclipse.emf.ecore.EObject, java.lang.Object, java.util.List)
	 */
	@Override
	protected Image doDecorateImage(EObject eObjectElement, Object element, List<String> suffixes) {
		suffixes.add("navigable"); //$NON-NLS-1$
		return OCLRequesterPlugin.getImageDescriptor("icons/navigable.gif").createImage(); //$NON-NLS-1$
	}

	/**
	 * @see AbstractDecorator#doDecorateText(java.lang.String, java.lang.Object, org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected String doDecorateText(String initialText, Object element, EObject eObjectElement) {
		return null;
	}
}
