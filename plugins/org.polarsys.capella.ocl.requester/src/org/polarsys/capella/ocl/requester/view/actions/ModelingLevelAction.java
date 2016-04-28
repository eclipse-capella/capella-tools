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

import org.polarsys.capella.ocl.requester.level.ModelingLevel;

/**
 * Action to select the modeling level.
 */
public class ModelingLevelAction extends OCLInterpreterViewAction {

	private final ModelingLevel level;

	/**
	 * Initializes me.
	 */
	public ModelingLevelAction(ModelingLevel level) {
		super(level.name());

		this.level = level;
	}

	@Override
	public int getStyle() {
		return AS_RADIO_BUTTON;
	}

	@Override
	public void run() {
		super.run();
		view.setModelingLevel(level);
		view.getDocument().setModelingLevel(level);
	}
}