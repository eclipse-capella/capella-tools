/*******************************************************************************
 * Copyright (c) 2017 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.resourceset.viewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class ResourceSetMonitor extends WorkbenchWindowControlContribution {

  protected ProgressBar semanticObjects;
  protected ProgressBar siriusObjects;
  protected ProgressBar gmfObjects;

  public ResourceSetMonitor() {
  }

  public ResourceSetMonitor(String id) {
    super(id);
  }

  @Override
  protected Control createControl(Composite parent) {
    GridLayout gl = new GridLayout(3, true);
    gl.marginBottom = -10;
    gl.marginTop = -10;
//    gl.marginHeight = 0;
//    gl.marginLeft = 0;
//    gl.marginRight = 0;
//    gl.marginWidth = 0;
    parent.setLayout(gl);
    GridData gd = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
//    gd.widthHint = 10;
    parent.setLayoutData(gd);

    semanticObjects = createProgressBar(parent, "Semantic objects");
    siriusObjects = createProgressBar(parent, "Sirius objects");
    gmfObjects = createProgressBar(parent, "GMF objects");

    return parent;
  }
  
  ProgressBar createProgressBar(Composite parent, String tooltip) {
    ProgressBar progressBar = new ProgressBar(parent, SWT.HORIZONTAL);
    progressBar.addControlListener(new ControlListener() {
      @Override
      public void controlResized(ControlEvent e) {
        //System.out.println("");
      }
      @Override
      public void controlMoved(ControlEvent e) {
        //System.out.println("");
      }
    });
    progressBar.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, true));
    progressBar.setMinimum(0);
    progressBar.setMaximum(100);
    progressBar.setSelection(50);
    progressBar.setLayoutData(new GridData());
    progressBar.setToolTipText(tooltip);
    return progressBar;
  }

}