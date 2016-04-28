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

import java.io.FileWriter;
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
public class ExportOCLTextAction extends OCLInterpreterViewAction {

	private final String tip;

	/**
	 * Initializes me.
	 */
	public ExportOCLTextAction() {
		super(OCLInterpreterMessages.OCLInterpreterView_exportOCLTextAction_label, OCLRequesterPlugin.getImageDescriptor("icons/elcl16/oclexport.gif")); //$NON-NLS-1$);
		tip = OCLInterpreterMessages.OCLInterpreterView_exportOCLTextAction_tip;
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

		if (oclExpression != null) {
			FileDialog dlg = new FileDialog(shell, SWT.SAVE);
			dlg.setFilterExtensions(new String[]{"*.ocl"}); //$NON-NLS-1$
			dlg.setText(OCLInterpreterMessages.OCLInterpreterView_exportOCLTextDlg_title);

			String file = dlg.open();
			if (file != null) {
				try {
					// Export OCL expression to an OCL file
					FileWriter writer = new FileWriter(file);
					try {
						writer.append(oclExpression);
					} catch (IOException e) {
						MessageDialog.openError(shell,
							OCLInterpreterMessages.OCLInterpreterView_exportOCLTextError_title,
							e.getLocalizedMessage());
					}
					writer.flush();
					writer.close();
				} catch (IOException e) {
					MessageDialog.openError(shell,
						OCLInterpreterMessages.OCLInterpreterView_exportOCLTextError_title,
						e.getLocalizedMessage());
				}
			}
		} else {
			MessageDialog.openWarning(shell,
				OCLInterpreterMessages.OCLInterpreterView_exportOCLTextWarn_title,
				OCLInterpreterMessages.OCLInterpreterView_exportOCLTextWarn_noExpr);
		}
	}
}