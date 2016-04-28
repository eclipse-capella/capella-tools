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
package org.polarsys.capella.ocl.requester.view.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.polarsys.capella.ocl.requester.view.OCLInterpreterView;

/**
 * Abstract action that is the common parent class for all action provided by
 * OCLInterpreterView.
 */
abstract public class OCLInterpreterViewAction extends Action {

	/**
	 * Refer to the OCLInterpreterView.
	 */
	protected OCLInterpreterView view;

	/**
	 * Default constructor.
	 */
	public OCLInterpreterViewAction() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param text
	 *            label of the button
	 * @param image
	 *            image of the button
	 */
	public OCLInterpreterViewAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	/**
	 * Constructor.
	 * 
	 * @param text
	 *            label of the button
	 * @param style
	 *            style of the button
	 */
	public OCLInterpreterViewAction(String text, int style) {
		super(text, style);
	}

	/**
	 * Constructor.
	 * 
	 * @param text
	 *            label of the button
	 */
	public OCLInterpreterViewAction(String text) {
		super(text);
	}

	/**
	 * Getter to retrieve the associated view.
	 * 
	 * @return the OCLInterpreterView
	 */
	public OCLInterpreterView getView() {
		return view;
	}

	/**
	 * Setter to define the associated view.
	 * 
	 * @param view
	 *            the OCLInterpreterView
	 */
	public void setView(OCLInterpreterView view) {
		this.view = view;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		if (view == null) {
			throw new IllegalStateException("View can not be null"); //$NON-NLS-1$
		}
	}
}
