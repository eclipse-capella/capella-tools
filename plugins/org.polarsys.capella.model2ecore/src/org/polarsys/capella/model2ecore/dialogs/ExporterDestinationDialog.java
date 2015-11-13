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
package org.polarsys.capella.model2ecore.dialogs;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.polarsys.capella.common.mdsofa.common.constant.ICommonConstants;
import org.polarsys.capella.common.mdsofa.common.misc.Couple;
import org.polarsys.capella.core.commands.preferences.service.BooleanFieldEditor2;
import org.polarsys.capella.model2ecore.CapellaEcoreExporterActivator;
import org.polarsys.capella.model2ecore.preferences.IPreferenceConstants;


/**
 * @author Stephane Fournier
 */
public class ExporterDestinationDialog extends TitleAreaDialog {
  private Couple<DirectoryFieldEditor, BooleanFieldEditor2> _dataPackageFieldEditors;
  private Couple<DirectoryFieldEditor, BooleanFieldEditor2> _interfaceFieldEditors;

  /**
   * Constructor.
   * @param parentShell
   */
  public ExporterDestinationDialog(Shell parentShell) {
    super(parentShell);
  }

  /**
   * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
   */
  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText(Messages.ExporterDestinationDialog_Title);
  }

  /**
   * @see org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    setTitle(Messages.ExporterDestinationDialog_Message);
    Composite containingComposite = new Composite(parent, SWT.NONE);
    containingComposite.setLayout(new GridLayout(1, true));
    containingComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
    _dataPackageFieldEditors =
        createDirectoryFieldEditor(containingComposite, Messages.ExporterDestinationDialog_DataPackage_Title,
            IPreferenceConstants.PREFERENCE_DATA_PACKAGE_ECORE_MODEL_FOLDER, null);

    _interfaceFieldEditors =
        createDirectoryFieldEditor(containingComposite, Messages.ExporterDestinationDialog_InterfacePackage_Title,
            IPreferenceConstants.PREFERENCE_INTERFACE_PACKAGE_ECORE_MODEL_FOLDER, IPreferenceConstants.PREFERENCE_INTERFACE_PACKAGE_ECORE_MODEL_EXPORT);
    return containingComposite;
  }

  /**
   * Create a directory field editor.
   * @param parent
   * @param labelText
   * @param directoryFolderPreferenceKey
   * @param activatedPreferenceKey can be <code>null</code>
   * @return
   */
  protected Couple<DirectoryFieldEditor, BooleanFieldEditor2> createDirectoryFieldEditor(Composite parent, String labelText,
      String directoryFolderPreferenceKey, String activatedPreferenceKey) {
    // Create a group to contain the directory field editor.
    Group group = new Group(parent, SWT.NONE);
    group.setLayout(new GridLayout(1, true));
    GridData layoutData = new GridData(GridData.FILL_BOTH);
    layoutData.horizontalIndent = 5;
    group.setLayoutData(layoutData);
    group.setText(labelText);

    // Create a checkbox to include or not the export for related package.
    IPreferenceStore preferenceStore = CapellaEcoreExporterActivator.getDefault().getPreferenceStore();
    BooleanFieldEditor2 includeFieldEditor = null;
    if (null != activatedPreferenceKey) {
      includeFieldEditor = new BooleanFieldEditor2(activatedPreferenceKey, Messages.ExporterDestinationDialog_IncludeButton_Title, group);
      includeFieldEditor.setPreferenceStore(preferenceStore);
      includeFieldEditor.load();
      Button checkboxButton = includeFieldEditor.getChangeControl(group);
      GridData buttonLayoutData = new GridData();
      buttonLayoutData.horizontalSpan = 2;
      checkboxButton.setLayoutData(buttonLayoutData);
    }
    // Create the directory field editor to select a directory.
    DirectoryFieldEditor directoryEditor = new DirectoryFieldEditor(directoryFolderPreferenceKey, ICommonConstants.EMPTY_STRING, group);
    directoryEditor.setPreferenceStore(preferenceStore);
    Label labelControl = directoryEditor.getLabelControl(group);
    layoutData = (new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
    labelControl.setLayoutData(layoutData);
    // Make it non editable.
    directoryEditor.getTextControl(group).setEditable(false);
    // Load its preference value.
    directoryEditor.load();
    return new Couple<DirectoryFieldEditor, BooleanFieldEditor2>(directoryEditor, includeFieldEditor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void okPressed() {
    // Store Data Package value.
    storeAndDispose(_dataPackageFieldEditors);
    // Store Interface Package value.
    storeAndDispose(_interfaceFieldEditors);
    super.okPressed();
  }

  /**
   * Store and dispose.
   * @param fieldEditors
   */
  private void storeAndDispose(Couple<DirectoryFieldEditor, BooleanFieldEditor2> fieldEditors) {
    fieldEditors.getKey().store();
    fieldEditors.getKey().dispose();
    BooleanFieldEditor2 value = fieldEditors.getValue();
    if (null != value) {
      value.store();
      value.dispose();
    }
  }
}