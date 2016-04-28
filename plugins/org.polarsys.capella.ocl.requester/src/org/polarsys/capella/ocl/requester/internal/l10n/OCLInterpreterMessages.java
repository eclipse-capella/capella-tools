/**
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM - Initial API and implementation
 * Thales - Capella adaptation
 */
package org.polarsys.capella.ocl.requester.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 * 
 * @author Christian Vogt (cvogt)
 */
public class OCLInterpreterMessages extends NLS {

	private static final String BUNDLE_NAME = "org.polarsys.capella.ocl.requester.internal.l10n.OCLInterpreterMessages"; //$NON-NLS-1$

	public static String console_title;
	public static String console_noContext;
	public static String console_badContextForQuery;
	public static String console_badContextForConstraint;
	public static String console_outputExc;
	public static String console_closeAction_label;
	public static String console_closeAction_tip;
	public static String console_evaluating;
	public static String console_results;
	public static String console_saveAction_label;
	public static String console_saveAction_tip;
	public static String console_saveDlg_title;
	public static String console_saveError_title;
	public static String console_saveWarn_title;
	public static String console_saveWarn_noExpr;
	public static String console_loadAction_label;
	public static String console_loadAction_tip;
	public static String console_loadDlg_title;
	public static String console_loadError_title;
	public static String console_loadWarn_title;
	public static String console_loadWarn_noExpr;
	public static String console_metamodelMenu;
	public static String console_metamodelTip;
	public static String console_metamodel_ecore;
	public static String console_metamodel_ecoreTip;
	public static String console_metamodel_uml;
	public static String console_metamodel_umlTip;
	public static String console_modelingLevel;
	public static String console_modelingLevelTip;
	public static String console_parsed;

	public static String OCLInterpreterView_tbtmModelOutput_text;

	public static String OCLInterpreterView_historyBackAction_label;
	public static String OCLInterpreterView_historyBackAction_tip;
	public static String OCLInterpreterView_historyForwardAction_label;
	public static String OCLInterpreterView_historyForwardAction_tip;

	public static String OCLInterpreterView_evaluateAction_label;
	public static String OCLInterpreterView_evaluateAction_tip;

	public static String OCLInterpreterView_importOCLTextAction_label;
	public static String OCLInterpreterView_importOCLTextAction_tip;
	public static String OCLInterpreterView_importOCLTextDlg_title;
	public static String OCLInterpreterView_importOCLTextError_title;
	public static String OCLInterpreterView_importOCLTextWarn_noExpr;
	public static String OCLInterpreterView_importOCLTextWarn_title;

	public static String OCLInterpreterView_exportOCLTextAction_label;
	public static String OCLInterpreterView_exportOCLTextAction_tip;
	public static String OCLInterpreterView_exportOCLTextDlg_title;
	public static String OCLInterpreterView_exportOCLTextError_title;
	public static String OCLInterpreterView_exportOCLTextWarn_noExpr;
	public static String OCLInterpreterView_exportOCLTextWarn_title;

	public static String OCLInterpreterView_exportResultAction_label;
	public static String OCLInterpreterView_exportResultAction_tip;
	public static String OCLInterpreterView_exportResultDlg_title;
	public static String OCLInterpreterView_exportResultError_title;
	public static String OCLInterpreterView_exportResultWarn_noExpr;
	public static String OCLInterpreterView_exportResultWarn_title;

	public static String OCLInterpreterView_clearAction_label;
	public static String OCLInterpreterView_clearAction_tip;

	static {
		NLS.initializeMessages(BUNDLE_NAME, OCLInterpreterMessages.class);
	}
}
