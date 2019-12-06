/*******************************************************************************
 * Copyright (c) 2015 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Soyatec - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.groovy.ui.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jdt.internal.debug.ui.IJavaDebugHelpContextIds;
import org.eclipse.jdt.internal.debug.ui.JavaDebugImages;
import org.eclipse.jdt.internal.debug.ui.actions.ControlAccessibleListener;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;
import org.polarsys.capella.groovy.CapellaGroovyConstants;
import org.polarsys.capella.groovy.ui.CapellaGroovyUIPlugin;

public final class CapellaScriptArgumentsTab extends AbstractLaunchConfigurationTab {

  // Program arguments widgets
  private Text prgmArgumentsText;

  protected static final String EMPTY_STRING = ""; //$NON-NLS-1$

  /**
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(Composite)
   */
  public void createControl(Composite parent) {
    Font font = parent.getFont();
    Composite comp = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout(1, true);
    comp.setLayout(layout);
    comp.setFont(font);

    GridData gd = new GridData(GridData.FILL_BOTH);
    comp.setLayoutData(gd);
    setControl(comp);
    setHelpContextId();

    Group group = new Group(comp, SWT.NONE);
    group.setFont(font);
    layout = new GridLayout();
    group.setLayout(layout);
    group.setLayoutData(new GridData(GridData.FILL_BOTH));

    String controlName = ("Script Arguments");
    group.setText(controlName);

    prgmArgumentsText = new Text(group, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
    prgmArgumentsText.addTraverseListener(new TraverseListener() {
      public void keyTraversed(TraverseEvent e) {
        switch (e.detail) {
        case SWT.TRAVERSE_ESCAPE:
        case SWT.TRAVERSE_PAGE_NEXT:
        case SWT.TRAVERSE_PAGE_PREVIOUS:
          e.doit = true;
          break;
        case SWT.TRAVERSE_RETURN:
        case SWT.TRAVERSE_TAB_NEXT:
        case SWT.TRAVERSE_TAB_PREVIOUS:
          if ((prgmArgumentsText.getStyle() & SWT.SINGLE) != 0) {
            e.doit = true;
          } else {
            if (!prgmArgumentsText.isEnabled() || (e.stateMask & SWT.MODIFIER_MASK) != 0) {
              e.doit = true;
            }
          }
          break;
        }
      }
    });
    gd = new GridData(GridData.FILL_BOTH);
    gd.heightHint = 40;
    gd.widthHint = 100;
    prgmArgumentsText.setLayoutData(gd);
    prgmArgumentsText.setFont(font);
    prgmArgumentsText.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent evt) {
        scheduleUpdateJob();
      }
    });
    ControlAccessibleListener.addListener(prgmArgumentsText, group.getText());

    String buttonLabel = LauncherMessages.JavaArgumentsTab_5;
    Button pgrmArgVariableButton = createPushButton(group, buttonLabel, null);
    pgrmArgVariableButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
    pgrmArgVariableButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
        dialog.open();
        String variable = dialog.getVariableExpression();
        if (variable != null) {
          prgmArgumentsText.insert(variable);
        }
      }
    });
  }

  /**
   * Set the help context id for this launch config tab. Subclasses may override this method.
   */
  protected void setHelpContextId() {
    PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
        IJavaDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_ARGUMENTS_TAB);
  }

  /**
   * Defaults are empty.
   * 
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(ILaunchConfigurationWorkingCopy)
   */
  public void setDefaults(ILaunchConfigurationWorkingCopy config) {
    config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, (String) null);
  }

  /**
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(ILaunchConfiguration)
   */
  @Override
  public void initializeFrom(ILaunchConfiguration configuration) {
    try {
      prgmArgumentsText
          .setText(configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "")); //$NON-NLS-1$
    } catch (CoreException e) {
      StatusManager.getManager().handle(e, CapellaGroovyUIPlugin.PLUGIN_ID);
    }
  }

  /**
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(ILaunchConfigurationWorkingCopy)
   */
  public void performApply(ILaunchConfigurationWorkingCopy configuration) {
    configuration.setAttribute(CapellaGroovyConstants.LAUNCH_ATTR_PROGRAM_ARGS.name(),
        getAttributeValueFrom(prgmArgumentsText));
  }

  /**
   * Returns the string in the text widget, or <code>null</code> if empty.
   * 
   * @param text
   *          the widget to get the value from
   * @return text or <code>null</code>
   */
  protected String getAttributeValueFrom(Text text) {
    String content = text.getText().trim();
    if (content.length() > 0) {
      return content;
    }
    return null;
  }

  /**
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
   */
  public String getName() {
    return "Script Arguments";
  }

  /**
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
   */
  @Override
  public Image getImage() {
    return JavaDebugImages.get(JavaDebugImages.IMG_VIEW_ARGUMENTS_TAB);
  }

  /**
   * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getId()
   * 
   * @since 3.3
   */
  @Override
  public String getId() {
    return "org.polarsys.capella.groovy.ui.launch.capellaScriptArgumentsTab"; //$NON-NLS-1$
  }

}
