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

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.polarsys.capella.ocl.requester.internal.l10n.OCLInterpreterMessages;

/**
 * Go to the next OCL expression if it has been defined.
 */
public class HistoryForwardAction extends OCLInterpreterViewAction {

	private final String tip;

	/**
	 * Initializes me.
	 */
	public HistoryForwardAction() {
		super(
			OCLInterpreterMessages.OCLInterpreterView_historyForwardAction_label,
			PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));

		this.tip = OCLInterpreterMessages.OCLInterpreterView_historyForwardAction_tip;
	}

	@Override
	public String getToolTipText() {
		return tip;
	}

	@Override
	public void run() {
		super.run();
		getView().historyForward();
	}
}
