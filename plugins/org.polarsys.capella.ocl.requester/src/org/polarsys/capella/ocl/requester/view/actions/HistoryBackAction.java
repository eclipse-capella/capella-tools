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
 * Go back to the previous OCL expression.
 */
public class HistoryBackAction extends OCLInterpreterViewAction {

	private final String tip;

	/**
	 * Initializes me.
	 */
	public HistoryBackAction() {
		super(
			OCLInterpreterMessages.OCLInterpreterView_historyBackAction_label,
			PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_BACK));

		this.tip = OCLInterpreterMessages.OCLInterpreterView_historyBackAction_tip;
	}

	@Override
	public String getToolTipText() {
		return tip;
	}

	@Override
	public void run() {
		super.run();
		getView().historyBack();
	}
}
