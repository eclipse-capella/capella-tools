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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.polarsys.capella.ocl.requester.internal.l10n.OCLInterpreterMessages;

/**
 * An action that export the current output from the tree to a basic CSV file.
 */
public class ExportResultAction extends OCLInterpreterViewAction {

	private final String tip;

	/**
	 * Initializes me.
	 */
	public ExportResultAction() {
		super(OCLInterpreterMessages.OCLInterpreterView_exportResultAction_label,
			PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));

		this.tip = OCLInterpreterMessages.OCLInterpreterView_exportResultAction_tip;
	}

	@Override
	public String getToolTipText() {
		return tip;
	}

	@Override
	public void run() {
		super.run();
		Shell shell = view.getPage().getShell();

		if (view.getLastOCLExpression() != null) {
			FileDialog dlg = new FileDialog(shell, SWT.SAVE);
			dlg.setFilterExtensions(new String[]{"*.csv"}); //$NON-NLS-1$
			dlg.setText(OCLInterpreterMessages.OCLInterpreterView_exportResultDlg_title);

			String file = dlg.open();
			if (file != null) {
				try {
					// Export tree items to a CSV file
					FileWriter writer = new FileWriter(file);
					try {
						TreeItem[] items = getView().getTreeItem();
						for (int i = 0; i < items.length; i++) {
							writer.append(items[i].getText()).append(
								System.getProperty("line.separator")); //$NON-NLS-1$
						}
					} catch (Exception e) {
						MessageDialog.openError(shell,
							OCLInterpreterMessages.OCLInterpreterView_exportResultError_title,
							e.getLocalizedMessage());
					}
					writer.flush();
					writer.close();
				} catch (IOException e) {
					MessageDialog.openError(shell,
						OCLInterpreterMessages.OCLInterpreterView_exportResultError_title,
						e.getLocalizedMessage());
				}
			}
		} else {
			MessageDialog.openWarning(shell,
				OCLInterpreterMessages.OCLInterpreterView_exportResultWarn_title,
				OCLInterpreterMessages.OCLInterpreterView_exportResultWarn_noExpr);
		}
	}
}