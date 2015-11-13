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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.mdsofa.common.constant.ICommonConstants;
import org.polarsys.capella.core.data.capellacore.AbstractDependenciesPkg;
import org.polarsys.capella.core.data.cs.BlockArchitecture;
import org.polarsys.capella.core.data.cs.InterfacePkg;
import org.polarsys.capella.core.data.information.DataPkg;
import org.polarsys.capella.model2ecore.IImageKeys;
import org.polarsys.capella.model2ecore.CapellaEcoreExporterActivator;
import org.polarsys.capella.model2ecore.command.AbstractEcoreExporterCommand;
import org.polarsys.capella.model2ecore.command.DataPkgToEcoreExporterCommand;
import org.polarsys.capella.model2ecore.command.InterfacePkgToEcoreExporter;
import org.polarsys.capella.model2ecore.dialogs.ExporterDestinationDialog;
import org.polarsys.capella.model2ecore.internal.util.ModelHelper;
import org.polarsys.capella.model2ecore.preferences.IPreferenceConstants;


/**
 * Export selected data container as an EMF Model.
 * @author Stephane Fournier
 */
public class EcoreExporterAction extends BaseSelectionListenerAction {
  /**
   * Log4j reference logger.
   */
  private static final Logger __logger = Logger.getLogger(EcoreExporterAction.class.getPackage().getName());

  /**
   * Constructor.
   */
  public EcoreExporterAction() {
    super(Messages.EcoreExporterAction_Title);
    setImageDescriptor(CapellaEcoreExporterActivator.getDefault().getImageDescriptor(IImageKeys.IMG_EXPORT_MODEL));
  }

  /**
   * compute resource path according to package path.
   * @param pkg
   * @return
   */
  private String computeResourcePath(AbstractDependenciesPkg pkg, String selectedDirectory) {
    String shortResourceName = ModelHelper.getPackageFullName(pkg, null);
    return new Path(selectedDirectory).append(shortResourceName).addFileExtension(ICommonConstants.ECORE_FILE_EXTENSION).toString();
  }

  /**
   * Handle Capella package export to Ecore.
   * @param exporter
   * @param capellaPackage
   * @param resourceSet
   * @param destinationFolder
   */
  protected void handlePackage(AbstractEcoreExporterCommand exporter, AbstractDependenciesPkg capellaPackage, ResourceSet resourceSet,
      String destinationFolder) {
    if (null != capellaPackage) {
      exporter.export(capellaPackage);
      String ecoreResourceName = computeResourcePath(capellaPackage, destinationFolder);
      serialize(resourceSet, URI.createFileURI(ecoreResourceName), exporter.getEcoreRootPackages());
    }
  }

  @Override
  public void run() {
    // Get destination files.
    ExporterDestinationDialog dialog = new ExporterDestinationDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
    if (Window.OK == dialog.open()) {
      IStructuredSelection structuredSelection = getStructuredSelection();
      // Create a resource set.
      ResourceSet resourceSet = new ResourceSetImpl();
      // Create shared mapping maps between exporters.
      Map<ModelElement, EClassifier> capellaToEcoreMappings = new HashMap<ModelElement, EClassifier>(0);
      Map<AbstractDependenciesPkg, EPackage> packagesMappings = new HashMap<AbstractDependenciesPkg, EPackage>(0);
      // Get the preference store to retrieve export preferences values.
      IPreferenceStore preferenceStore = CapellaEcoreExporterActivator.getDefault().getPreferenceStore();
      BlockArchitecture architecture = (BlockArchitecture) structuredSelection.getFirstElement();
      // Get the Data Package.
      DataPkg dataPackage = architecture.getOwnedDataPkg();
      // Export the DataPackage.
      String dataPackageFolder = preferenceStore.getString(IPreferenceConstants.PREFERENCE_DATA_PACKAGE_ECORE_MODEL_FOLDER);
      handlePackage(new DataPkgToEcoreExporterCommand(capellaToEcoreMappings, packagesMappings), dataPackage, resourceSet, dataPackageFolder);

      if (preferenceStore.getBoolean(IPreferenceConstants.PREFERENCE_INTERFACE_PACKAGE_ECORE_MODEL_EXPORT)) {
        // Get the Interface Package.
        InterfacePkg ownedInterfacePkg = architecture.getOwnedInterfacePkg();
        String interfacePackageFolder = preferenceStore.getString(IPreferenceConstants.PREFERENCE_INTERFACE_PACKAGE_ECORE_MODEL_FOLDER);
        handlePackage(new InterfacePkgToEcoreExporter(capellaToEcoreMappings, packagesMappings), ownedInterfacePkg, resourceSet, interfacePackageFolder);
      }
    }
  }

  /**
   * Serialize generated Ecore model according to given URI.
   * @param resourceSet
   * @param resourceURI
   * @param packages packages to serialize.
   */
  protected void serialize(ResourceSet resourceSet, URI resourceURI, List<EPackage> packages) {

    Resource ecoreResource = resourceSet.createResource(resourceURI);
    ecoreResource.getContents().addAll(packages);
    try {
      ecoreResource.save(Collections.EMPTY_MAP);
    } catch (IOException exception) {
      StringBuilder loggerMessage = new StringBuilder("EcoreExporterCommand.serialize(..) _ "); //$NON-NLS-1$
      __logger.warn(loggerMessage.toString(), exception);
    }
  }
}
