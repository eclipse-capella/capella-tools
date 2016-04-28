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
package org.polarsys.capella.ocl.requester.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.polarsys.capella.ocl.requester.OCLRequesterPlugin;
import org.polarsys.capella.ocl.requester.view.OCLInterpreterView;

/**
 * Show OCL Interpreter to perform OCL requests.
 */
public class ShowOCLRequestorAction
		extends Action
		implements IObjectActionDelegate, IEditorActionDelegate {

	/**
	 * The list of selected objects.
	 */
	protected List<EObject> selection;

	/**
	 * {@inheritDoc}<br/>
	 * Here the selection represents the list of
	 * {@link org.eclipse.sirius.business.api.session.Session}s which should
	 * contain the new Remote Resource.<br/>
	 */
	public void setSelection(List<Object> selectionToSet) {
		this.selection = new ArrayList<EObject>();
		Iterator<Object> iterator = selectionToSet.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof EObject) {
				selection.add((EObject) next);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		// Open OCL Requestor and initialise context
		// open view
		IWorkbenchWindow window = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow();
		try {
			// any view in your application
			window.getActivePage().showView(OCLInterpreterView.ID);
		} catch (PartInitException e1) {
			OCLRequesterPlugin
				.getDefault()
				.getLog()
				.log(
					new Status(IStatus.ERROR, OCLRequesterPlugin
						.getPluginId(), "Impossible to show view")); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	@SuppressWarnings("unchecked")
	public void selectionChanged(IAction action, ISelection selection2) {
		if (selection2 instanceof IStructuredSelection) {
			setSelection(((IStructuredSelection) selection2).toList());
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

	/**
	 * {@inheritDoc}
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {

	}
}
