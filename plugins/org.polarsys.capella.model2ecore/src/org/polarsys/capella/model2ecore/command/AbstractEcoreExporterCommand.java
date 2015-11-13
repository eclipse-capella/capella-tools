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
package org.polarsys.capella.model2ecore.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.polarsys.capella.common.data.modellingcore.AbstractType;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.helpers.EcoreUtil2;
import org.polarsys.capella.common.mdsofa.common.constant.ICommonConstants;
import org.polarsys.capella.common.mdsofa.common.misc.Couple;
import org.polarsys.capella.core.data.capellacore.AbstractDependenciesPkg;
import org.polarsys.capella.core.data.capellacore.CapellacorePackage;
import org.polarsys.capella.model2ecore.internal.converters.DataTypeConverter;
import org.polarsys.capella.model2ecore.internal.util.AbstractModelElementHandler;
import org.polarsys.capella.model2ecore.internal.util.ModelHelper;


/**
 * @author Stephane Fournier
 */
public abstract class AbstractEcoreExporterCommand {
  /**
   * NS_URI prefix.
   */
  protected static final String NS_URI_PREFIX = "http://www.polarsys.org"; //$NON-NLS-1$
  /**
   * Ecore Annotation where source annotation's value is <code>http://www.eclipse.org/emf/2002/Ecore</code>
   */
  protected static final String ECORE_ANNOTATION = "http://www.eclipse.org/emf/2002/Ecore"; //$NON-NLS-1$
  /**
   * GenModel Annotation where source annotation's value is <code>http://www.eclipse.org/emf/2002/GenModel</code>
   */
  protected static final String GENMODEL_ANNOTATION = "http://www.eclipse.org/emf/2002/GenModel"; //$NON-NLS-1$
  /**
   * Ecore Annotation entry key to use EMF EOperation delegation mechanism.
   */
  protected static final String DELEGATE_ANNOTATION_ENTRY_KEY = "invocationDelegates"; //$NON-NLS-1$
  /**
   * GenModel Annotation entry key for documentation.
   */
  protected static final String DOCUMENTATION_ANNOTATION_ENTRY_KEY = "documentation"; //$NON-NLS-1$

  /**
   * Ecore root packages to serialize.
   */
  private List<EPackage> ecoreRootPackages;
  /**
   * Mappings between Capella objects and Ecore ones.
   */
  private Map<ModelElement, EClassifier> _capellaToEcoreMappings;
  /**
   * Mappings between Capella data packages and Ecore ones.
   */
  private Map<AbstractDependenciesPkg, EPackage> packagesMappings;

  /**
   * Constructor
   */
  protected AbstractEcoreExporterCommand() {
    this(new HashMap<ModelElement, EClassifier>(0), new HashMap<AbstractDependenciesPkg, EPackage>(0));
  }

  /**
   * Constructor.
   */
  protected AbstractEcoreExporterCommand(Map<ModelElement, EClassifier> capellaToEcoreMappings, Map<AbstractDependenciesPkg, EPackage> packagesMappings) {
    this._capellaToEcoreMappings = capellaToEcoreMappings;
    this.packagesMappings = packagesMappings;
    this.ecoreRootPackages = new ArrayList<EPackage>(1);
  }

  /**
   * Create an Ecore package for specified Capella Data package.
   * @param pkg
   * @return a not <code>null</code> package.
   */
  protected EPackage createEPackage(AbstractDependenciesPkg pkg) {
    EPackage ecorePackage = EcoreFactory.eINSTANCE.createEPackage();
    String packageName = ModelHelper.forceCharactersToEcoreNamingConventions(pkg.getName());
    ecorePackage.setName(packageName);
    ecorePackage.setNsPrefix(packageName);

    // Compute a default nsURI.
    StringBuilder nsURI = new StringBuilder();
    StringTokenizer tokenizer = new StringTokenizer(pkg.getFullLabel(), ICommonConstants.EMPTY_STRING + ICommonConstants.SLASH_CHARACTER);
    tokenizer.nextToken();
    while (tokenizer.hasMoreTokens()) {
      nsURI.append(ICommonConstants.SLASH_CHARACTER).append(tokenizer.nextToken());
    }
    ecorePackage.setNsURI(NS_URI_PREFIX + ModelHelper.forceCharactersToEcoreNamingConventions(nsURI.toString()));
    // Add the mapping.
    getPackagesMappings().put(pkg, ecorePackage);
    return ecorePackage;
  }

  /**
   * Dispose internal collections.
   */
  protected void dispose() {
    // Do nothing.
  }

  /**
   * Export specified Capella root package (and its content) as an Ecore model.
   * @param capellaRootPackage The capella root package to export.
   */
  public void export(AbstractDependenciesPkg capellaRootPackage) {
    // Clear the Ecore packages to serialize before export operation.
    getEcoreRootPackages().clear();
    EPackage rootEPackage = createEPackage(capellaRootPackage);
    getEcoreRootPackages().add(rootEPackage);
    exportPackage(capellaRootPackage, rootEPackage);
    // Clean internal collections.
    dispose();
  }

  /**
   * Do the export.
   * @param capellaRootPackage
   * @param ecorePackage the Ecore package related given capella root package.
   */
  protected abstract void exportPackage(AbstractDependenciesPkg capellaRootPackage, EPackage ecorePackage);

  /**
   * Get Ecore root packages to serialize.
   * @return a not <code>null</code> instance.
   */
  public List<EPackage> getEcoreRootPackages() {
    return ecoreRootPackages;
  }

  /**
   * Get Type for specified capella Type.
   * @param capellaType
   * @param ecoreDataType
   * @return
   */
  protected EClassifier getEcoreType(AbstractType capellaType) {
    EClassifier ecoreDataType = _capellaToEcoreMappings.get(capellaType);
    if (null == ecoreDataType) {
      // Handle the case where the data type was never met at this moment.
      ecoreDataType = new DataTypeConverter().doSwitch(capellaType);
      // Check if returned DataType is a default Ecore one e.g EInt, EString...
      if (!EcorePackage.eINSTANCE.equals(ecoreDataType.eContainer())) {
        // Handle the creation of this outer root package data type.
        AbstractDependenciesPkg dataPackageContainer =
            (AbstractDependenciesPkg) EcoreUtil2.getFirstContainer(capellaType, CapellacorePackage.Literals.ABSTRACT_DEPENDENCIES_PKG);
        EPackage ecorePackage = packagesMappings.get(dataPackageContainer);
        if (null == ecorePackage) {
          ecorePackage = createEPackage(dataPackageContainer);
          ecoreRootPackages.add(ecorePackage);
        }
        ecorePackage.getEClassifiers().add(ecoreDataType);
      }
      _capellaToEcoreMappings.put(capellaType, ecoreDataType);
    }
    return ecoreDataType;
  }

  /**
   * Get mappings between Capella objects and Ecore ones.
   * @return a not <code>null</code> instance.
   */
  protected Map<ModelElement, EClassifier> getCapellaToEcoreMappings() {
    return _capellaToEcoreMappings;
  }

  /**
   * Get mappings between Capella data packages and Ecore ones.
   * @return a not <code>null</code> instance.
   */
  protected Map<AbstractDependenciesPkg, EPackage> getPackagesMappings() {
    return packagesMappings;
  }

  /**
   * Walk through package content.
   * @param capellaPackage capella package to export.
   * @param parentPackage the Ecore package container of exported elements.
   * @param contentHandler
   */
  protected void walkThroughPackageContent(AbstractDependenciesPkg capellaPackage, EPackage parentPackage, AbstractModelElementHandler contentHandler) {
    EList<EObject> contents = capellaPackage.eContents();

    Iterator<EObject> iterator = contents.iterator();
    EList<EClassifier> eClassifiers = parentPackage.getEClassifiers();

    // Iterate over Capella Data Package contents.
    while (iterator.hasNext()) {
      ModelElement currentElement = (ModelElement) iterator.next();
      Couple<EClassifier, Boolean> result = contentHandler.handleElement(currentElement, parentPackage, this);
      EClassifier classifier = result.getKey();
      boolean shouldPersistInResultingEcore = result.getValue().booleanValue();
      // Add it to the Ecore root package.
      if (null != classifier) {
        if (shouldPersistInResultingEcore) {
          eClassifiers.add(classifier);
        }
        _capellaToEcoreMappings.put(currentElement, classifier);
      }
    }
  }
}
