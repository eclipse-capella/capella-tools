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

import org.polarsys.capella.ocl.requester.internal.l10n.OCLInterpreterMessages;
import org.polarsys.capella.ocl.requester.patterns.EcoreOCLFactory;

/**
 * Action to select the Ecore metamodel.
 */
public class EcoreMetamodelAction extends OCLInterpreterViewAction {

	private final String tip;

	/**
	 * Initializes me.
	 */
	public EcoreMetamodelAction() {
		super(OCLInterpreterMessages.console_metamodel_ecore);
		tip = OCLInterpreterMessages.console_metamodel_ecoreTip;
	}

	@Override
	public int getStyle() {
		return AS_RADIO_BUTTON;
	}

	@Override
	public String getToolTipText() {
		return tip;
	}

	@Override
	public void run() {
		super.run();
		this.view.setOclFactory(new EcoreOCLFactory(view.getContext()));
		this.view.getDocument().setOCLFactory(this.view.getOclFactory());
	}
}