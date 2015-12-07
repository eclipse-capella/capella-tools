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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.internal.debug.ui.IJavaDebugHelpContextIds;
import org.eclipse.jdt.internal.debug.ui.JavaDebugImages;
import org.eclipse.jdt.internal.debug.ui.actions.ControlAccessibleListener;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.ui.statushandlers.StatusManager;
import org.polarsys.capella.groovy.CapellaGroovyConstants;
import org.polarsys.capella.groovy.ui.CapellaGroovyUIPlugin;

public class CapellaScriptMainTab extends AbstractLaunchConfigurationTab {

  // Program arguments widgets
  private Label scriptLocationLabel;
  private Text scriptLocationText;

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

    String controlName = "Script Location";
    group.setText(controlName);

    scriptLocationText = new Text(group, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
    scriptLocationText.addTraverseListener(new TraverseListener() {
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
          if ((scriptLocationText.getStyle() & SWT.SINGLE) != 0) {
            e.doit = true;
          } else {
            if (!scriptLocationText.isEnabled() || (e.stateMask & SWT.MODIFIER_MASK) != 0) {
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
    scriptLocationText.setLayoutData(gd);
    scriptLocationText.setFont(font);
    scriptLocationText.addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent evt) {
        scheduleUpdateJob();
      }
    });
    ControlAccessibleListener.addListener(scriptLocationText, group.getText());

    String buttonLabel = "Browse...";
    Button browseButton = createPushButton(group, buttonLabel, null);
    browseButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
    browseButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        final Collection<IResource> allScripts = new ArrayList<IResource>();
        try {
          ResourcesPlugin.getWorkspace().getRoot().accept(new IResourceVisitor() {
            @Override
            public boolean visit(IResource resource) throws CoreException {
              if (resource.getType() == IResource.FILE) {
                if (CapellaGroovyConstants.GROOVY_EXTENSION.equals(((IFile) resource).getFileExtension())) {
                  allScripts.add(resource);
                }
              }
              return true;
            }
          });
          ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(getShell(),
              allScripts.toArray(new IResource[allScripts.size()]));
          if (dialog.open() == Window.OK) {
            scriptLocationText.setText(((IFile) dialog.getResult()[0]).getFullPath().toString());
          }
        } catch (CoreException exception) {
          StatusManager.getManager().handle(exception, CapellaGroovyUIPlugin.PLUGIN_ID);
        }
      }
    });
  }

  /**
   * Set the help context id for this launch config tab. Subclasses may override this method.
   */
  protected void setHelpContextId() {
    PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
        IJavaDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_MAIN_TAB);
  }

  /**
   * Defaults are empty.
   * 
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(ILaunchConfigurationWorkingCopy)
   */
  public void setDefaults(ILaunchConfigurationWorkingCopy config) {
    config.setAttribute(CapellaGroovyConstants.LAUNCH_ATTR_SCRIPT_LOCATION.name(), (String) null);
  }

  /**
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(ILaunchConfiguration)
   */
  @Override
  public void initializeFrom(ILaunchConfiguration configuration) {
    try {
      scriptLocationText
          .setText(configuration.getAttribute(CapellaGroovyConstants.LAUNCH_ATTR_SCRIPT_LOCATION.name(), "")); //$NON-NLS-1$
    } catch (CoreException e) {
      StatusManager.getManager().handle(e, CapellaGroovyUIPlugin.PLUGIN_ID);
    }
  }

  /**
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(ILaunchConfigurationWorkingCopy)
   */
  public void performApply(ILaunchConfigurationWorkingCopy configuration) {
    configuration.setAttribute(CapellaGroovyConstants.LAUNCH_ATTR_SCRIPT_LOCATION.name(),
        getAttributeValueFrom(scriptLocationText));
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
    return "Main";
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
    return "org.polarsys.capella.groovy.ui.launch.capellaScriptMainTab"; //$NON-NLS-1$
  }

}
