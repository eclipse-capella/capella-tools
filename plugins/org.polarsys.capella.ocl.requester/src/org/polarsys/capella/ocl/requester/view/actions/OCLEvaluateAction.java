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

import org.polarsys.capella.ocl.requester.OCLRequesterPlugin;
import org.polarsys.capella.ocl.requester.internal.l10n.OCLInterpreterMessages;

/**
 * Evaluate the current OCL expression.
 */
public class OCLEvaluateAction extends OCLInterpreterViewAction {

	private final String tip;

	/**
	 * Initializes me.
	 */
	public OCLEvaluateAction() {
		super(OCLInterpreterMessages.OCLInterpreterView_evaluateAction_label,
			OCLRequesterPlugin
				.getImageDescriptor("icons/elcl16/start_task.gif")); //$NON-NLS-1$

		this.tip = OCLInterpreterMessages.OCLInterpreterView_evaluateAction_tip;
	}

	@Override
	public String getToolTipText() {
		return tip;
	}

	@Override
	public void run() {
		super.run();
		getView().evaluate();
	}
}
