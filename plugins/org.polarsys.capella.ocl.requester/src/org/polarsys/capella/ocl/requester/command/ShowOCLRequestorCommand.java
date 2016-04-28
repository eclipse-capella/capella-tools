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
package org.polarsys.capella.ocl.requester.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.handlers.HandlerUtil;
import org.polarsys.capella.ocl.requester.actions.ShowOCLRequestorAction;

/**
 * Command that display OCL requester.
 */
public class ShowOCLRequestorCommand
		extends AbstractHandler {

	private IObjectActionDelegate action;

	/**
	 * Construct a new instance.
	 */
	public ShowOCLRequestorCommand() {
		super();
		action = new ShowOCLRequestorAction();
	}

	/**
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) {
		action.selectionChanged(null, HandlerUtil.getCurrentSelection(event));
		action.run(null);
		return null;
	}
}
