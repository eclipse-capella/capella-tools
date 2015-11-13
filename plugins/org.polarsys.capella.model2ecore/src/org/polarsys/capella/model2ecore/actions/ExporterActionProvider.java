/*******************************************************************************
 * Copyright (c) 2009 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Stephane Fournier - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.model2ecore.actions;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.polarsys.capella.core.platform.sirius.ui.navigator.actions.SelectionHelper;

/**
 * Add Ecore Exporter actions to the Capella Explorer.
 * @author Stephane Fournier
 */
public class ExporterActionProvider extends CommonActionProvider {
  /**
   * Obfuscate a model action.
   */
  private EcoreExporterAction _ecoreExporterAction;

  /**
   * @see org.eclipse.ui.actions.ActionGroup#dispose()
   */
  @Override
  public void dispose() {
    ISelectionProvider selectionProvider = getActionSite().getViewSite().getSelectionProvider();
    if (null != _ecoreExporterAction) {
      selectionProvider.removeSelectionChangedListener(_ecoreExporterAction);
      _ecoreExporterAction = null;
    }
    super.dispose(); // test
  }

  /**
   * @see org.eclipse.ui.actions.ActionGroup#fillActionBars(org.eclipse.ui.IActionBars)
   */
  @Override
  public void fillActionBars(IActionBars actionBars) {
    // Do nothing.
  }

  /**
   * @see org.eclipse.ui.actions.ActionGroup#fillContextMenu(org.eclipse.jface.action.IMenuManager)
   */
  @Override
  public void fillContextMenu(IMenuManager menu) {
    updateActionBars();
    menu.appendToGroup(ICommonMenuConstants.GROUP_GENERATE, _ecoreExporterAction);
  }

  /**
   * @see org.eclipse.ui.navigator.CommonActionProvider#init(org.eclipse.ui.navigator.ICommonActionExtensionSite)
   */
  @Override
  public void init(ICommonActionExtensionSite site) {
    super.init(site);
    ISelectionProvider selectionProvider = site.getViewSite().getSelectionProvider();
    _ecoreExporterAction = new EcoreExporterAction();
    SelectionHelper.registerToSelectionChanges(_ecoreExporterAction, selectionProvider);
  }
}
