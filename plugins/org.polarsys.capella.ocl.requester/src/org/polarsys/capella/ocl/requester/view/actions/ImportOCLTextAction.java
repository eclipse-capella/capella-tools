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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.polarsys.capella.ocl.requester.OCLRequesterPlugin;
import org.polarsys.capella.ocl.requester.internal.l10n.OCLInterpreterMessages;

/**
 * An action that export the current output from the tree to a basic CSV file.
 */
public class ImportOCLTextAction extends OCLInterpreterViewAction {

	private final String tip;

	/**
	 * Initializes me.
	 */
	public ImportOCLTextAction() {
		super(OCLInterpreterMessages.OCLInterpreterView_importOCLTextAction_label, OCLRequesterPlugin.getImageDescriptor("icons/elcl16/oclimport.gif")); //$NON-NLS-1$
		tip = OCLInterpreterMessages.OCLInterpreterView_importOCLTextAction_tip;
	}

	@Override
	public String getToolTipText() {
		return tip;
	}

	@Override
	public void run() {
		super.run();
		Shell shell = view.getPage().getShell();

		String oclExpression = null;
		try {
			oclExpression = view.getDocument().get(0, view.getDocument().getLength());
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}

		boolean confirm = true;
		if (oclExpression != null) {
			confirm = MessageDialog
				.openConfirm(
					shell,
					OCLInterpreterMessages.OCLInterpreterView_importOCLTextWarn_title,
					OCLInterpreterMessages.OCLInterpreterView_importOCLTextWarn_noExpr);
		}

		if (confirm) {
			FileDialog dlg = new FileDialog(shell, SWT.OPEN);
			dlg.setFilterExtensions(new String[]{"*.ocl"}); //$NON-NLS-1$
			dlg.setText(OCLInterpreterMessages.OCLInterpreterView_importOCLTextDlg_title);
	
			String file = dlg.open();
			if (file != null) {
				// Import OCL expression from an OCL file
				try {
					FileReader reader = new FileReader(file);
					String line;
					BufferedReader buffer = new BufferedReader(reader);
					oclExpression = ""; //$NON-NLS-1$
					while((line = buffer.readLine()) != null) {
						oclExpression += line;
						oclExpression += System.getProperty("line.separator"); //$NON-NLS-1$
					}
					reader.close();
				} catch (IOException e) {
					MessageDialog
						.openError(
							shell,
							OCLInterpreterMessages.OCLInterpreterView_importOCLTextError_title,
							e.getLocalizedMessage());
				}
				
				if (!oclExpression.isEmpty()) {
					view.getDocument().set(oclExpression);
				}
			}
		}
	}
}