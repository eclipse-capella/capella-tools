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

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.polarsys.capella.ocl.requester.internal.l10n.OCLInterpreterMessages;

/**
 * Action that clear the console and the model output.
 */
public class ClearOutputAction extends OCLInterpreterViewAction {

	private org.eclipse.ui.console.actions.ClearOutputAction delegateAction;

	private final String tip;

	public ClearOutputAction(ITextViewer viewer) {
		super(OCLInterpreterMessages.OCLInterpreterView_clearAction_label,
			PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_ETOOL_CLEAR));

		this.tip = OCLInterpreterMessages.OCLInterpreterView_clearAction_tip;

		delegateAction = new org.eclipse.ui.console.actions.ClearOutputAction(
			viewer);
	}

	@Override
	public String getToolTipText() {
		return tip;
	}

	/**
	 * @see org.eclipse.ui.console.actions.ClearOutputAction#run()
	 */
	@Override
	public void run() {
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				delegateAction.run();
				getView().getTreeViewer().setInput(null);
			}
		});
	}
}
