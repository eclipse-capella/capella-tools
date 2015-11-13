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
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.polarsys.capella.common.data.modellingcore.AbstractType;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.helpers.EcoreUtil2;
import org.polarsys.capella.common.mdsofa.common.misc.Couple;
import org.polarsys.capella.core.data.capellacore.AbstractDependenciesPkg;
import org.polarsys.capella.core.data.capellacore.CapellacorePackage;
import org.polarsys.capella.core.data.capellacore.GeneralizableElement;
import org.polarsys.capella.core.data.capellacore.Generalization;
import org.polarsys.capella.core.data.information.AggregationKind;
import org.polarsys.capella.core.data.information.Association;
import org.polarsys.capella.core.data.information.Class;
import org.polarsys.capella.core.data.information.DataPkg;
import org.polarsys.capella.core.data.information.Operation;
import org.polarsys.capella.core.data.information.Parameter;
import org.polarsys.capella.core.data.information.ParameterDirection;
import org.polarsys.capella.core.data.information.Property;
import org.polarsys.capella.core.data.information.datatype.DataType;
import org.polarsys.capella.core.data.information.datavalue.LiteralNumericValue;
import org.polarsys.capella.model2ecore.internal.converters.ClassConverter;
import org.polarsys.capella.model2ecore.internal.converters.DataTypeConverter;
import org.polarsys.capella.model2ecore.internal.util.AbstractModelElementHandler;
import org.polarsys.capella.model2ecore.internal.util.ModelHelper;


/**
 * Command that export a Capella {@link DataPkg} and its contents as an autonomous Ecore model.
 * @author Stephane Fournier
 */
public class DataPkgToEcoreExporterCommand extends AbstractEcoreExporterCommand {

  /**
   * Capella Associations look up during model walk through.
   */
  private List<Association> _capellaAssociations;

  /**
   * Constructor.
   */
  public DataPkgToEcoreExporterCommand() {
    super();
    _capellaAssociations = new ArrayList<Association>(0);
  }

  /**
   * Constructor.
   * @param capellaToEcoreMappings
   * @param packagesMappings
   */
  public DataPkgToEcoreExporterCommand(Map<ModelElement, EClassifier> capellaToEcoreMappings, Map<AbstractDependenciesPkg, EPackage> packagesMappings) {
    super(capellaToEcoreMappings, packagesMappings);
    _capellaAssociations = new ArrayList<Association>(0);
  }

  /**
   * Add EOperation delegation mechanism.
   * @param pkg
   * @param ecorePackage
   */
  private void addDelegationAnnotation(AbstractDependenciesPkg pkg, EPackage ecorePackage) {
    EAnnotation delegationAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
    ecorePackage.getEAnnotations().add(delegationAnnotation);
    delegationAnnotation.setSource(ECORE_ANNOTATION);
    // Create an anotation detail entry.
    EMap<String, String> annotationEntries = delegationAnnotation.getDetails();
    annotationEntries.put(DELEGATE_ANNOTATION_ENTRY_KEY, ModelHelper.getPackageDelegationAnnotationName(pkg));
  }

  /**
   * Create an {@link EAttribute} according to specified parameters.
   * @param ownerClass
   * @param capellaAttribute
   */
  protected void createEAttribute(EClass ownerClass, Property capellaAttribute) {
    EAttribute ecoreAttribute = EcoreFactory.eINSTANCE.createEAttribute();
    ownerClass.getEStructuralFeatures().add(ecoreAttribute);
    // Set its name.
    ecoreAttribute.setName(ModelHelper.forceCharactersToEcoreNamingConventions(capellaAttribute.getName()));
    // Set bounds.
    ecoreAttribute.setUpperBound(ModelHelper.getValue((LiteralNumericValue) capellaAttribute.getOwnedMaxCard()));
    ecoreAttribute.setLowerBound(ModelHelper.getValue((LiteralNumericValue) capellaAttribute.getOwnedMinCard()));
    // Set type.
    AbstractType capellaAttributeType = capellaAttribute.getAbstractType();
    EClassifier ecoreDataType = getEcoreType(capellaAttributeType);
    ecoreAttribute.setEType(ecoreDataType);
    ecoreAttribute.setID(capellaAttribute.isUnique());
  }

  /**
   * Create an {@link EOperation} for specified parameters.
   * @param ownerClass
   * @param capellaOperation
   */
  protected void createEOperation(EClass ownerClass, Operation capellaOperation) {
    EOperation eOperation = EcoreFactory.eINSTANCE.createEOperation();
    eOperation.setName(ModelHelper.forceCharactersToEcoreNamingConventions(capellaOperation.getName()));
    ownerClass.getEOperations().add(eOperation);
    // Create and add delegation annotation.
    EAnnotation delegationAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
    AbstractDependenciesPkg capellaPackage =
        (AbstractDependenciesPkg) EcoreUtil2.getFirstContainer(capellaOperation, CapellacorePackage.Literals.ABSTRACT_DEPENDENCIES_PKG);
    delegationAnnotation.setSource(ModelHelper.getPackageDelegationAnnotationName(capellaPackage));
    eOperation.getEAnnotations().add(delegationAnnotation);

    // Set all operation data.
    for (Parameter currentParameter : capellaOperation.getOwnedParameters()) {
      ETypedElement ecoreElement = null;
      String name = null;
      if (ParameterDirection.RETURN.equals(currentParameter.getDirection())) {
        // Handle the return type.
        ecoreElement = eOperation;
      } else {
        // Handle operation's parameters.
        EParameter ecoreParameter = EcoreFactory.eINSTANCE.createEParameter();
        eOperation.getEParameters().add(ecoreParameter);
        ecoreElement = ecoreParameter;
        name = currentParameter.getName();
      }
      // Set parameter data.
      AbstractType capellaType = currentParameter.getAbstractType();
      EClassifier ecoreType = getEcoreType(capellaType);
      ecoreElement.setEType(ecoreType);
      if (null != name) {
        ecoreElement.setName(ModelHelper.forceCharactersToEcoreNamingConventions(name));
      }
      ecoreElement.setLowerBound(ModelHelper.getValue((LiteralNumericValue) currentParameter.getOwnedMinCard()));
      ecoreElement.setUpperBound(ModelHelper.getValue((LiteralNumericValue) currentParameter.getOwnedMaxCard()));
    }
  }

  /**
   * Create an {@link EReference} for specified parameters.
   * @param capellaClassOwner
   * @param relationProperty
   * @return a not <code>null</code> reference.
   */
  protected EReference createEReference(ModelElement capellaClassOwner, Property relationProperty) {
    // Create an EReference.
    EReference reference = EcoreFactory.eINSTANCE.createEReference();
    // Get the related container of the relation.
    EClass ecoreClassOwner = (EClass) getEcoreType((AbstractType) capellaClassOwner);
    // Add it to its class container.
    ecoreClassOwner.getEStructuralFeatures().add(reference);
    // Set the relation target type.
    AbstractType capellaTargetClass = relationProperty.getAbstractType();
    reference.setEType(getEcoreType(capellaTargetClass));
    // Set bounds.
    reference.setUpperBound(ModelHelper.getValue((LiteralNumericValue) relationProperty.getOwnedMaxCard()));
    reference.setLowerBound(ModelHelper.getValue((LiteralNumericValue) relationProperty.getOwnedMinCard()));
    reference.setName(ModelHelper.forceCharactersToEcoreNamingConventions(relationProperty.getName()));
    // Set relation containment.
    AggregationKind aggregationKind = relationProperty.getAggregationKind();
    reference.setContainment((AggregationKind.COMPOSITION == aggregationKind || AggregationKind.AGGREGATION == aggregationKind));
    return reference;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void dispose() {
    super.dispose();
    _capellaAssociations.clear();
  }

  /**
   * Handle attributes.<br>
   * Called when handling Classes.
   * @param capellaClass
   * @param relatedEcoreClass
   */
  protected void handleAttributes(org.polarsys.capella.core.data.information.Class capellaClass, EClass relatedEcoreClass) {
    // Loop over class properties.
    for (Property currentProperty : capellaClass.getContainedProperties()) {
      // Retrieve type information.
      AbstractType abstractType = currentProperty.getAbstractType();
      // Filter out Capella property related to associations.
      EClassifier ecoreDataType = getEcoreType(abstractType);
      if (abstractType instanceof DataType) {
        if (ecoreDataType instanceof EDataType) {
          createEAttribute(relatedEcoreClass, currentProperty);
        } else if (ecoreDataType instanceof EClass) {
          // Handle the case a Capella DataType is exported as an EClass.
          createEReference(capellaClass, currentProperty);
        }
      }
    }
  }

  /**
   * Handle generated all Ecore classes.
   */
  protected void handleClasses() {
    // Clone the map to avoid concurrent accesses.
    Map<EObject, EClassifier> capellaToEcoreMappings = new HashMap<EObject, EClassifier>(getCapellaToEcoreMappings());
    Iterator<Entry<EObject, EClassifier>> iterator = capellaToEcoreMappings.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<EObject, EClassifier> entry = iterator.next();
      EObject key = entry.getKey();
      // Handle Class attribute.
      if (key instanceof org.polarsys.capella.core.data.information.Class) {
    	  org.polarsys.capella.core.data.information.Class currentCapellaClass = (org.polarsys.capella.core.data.information.Class) key;
        handleAttributes(currentCapellaClass, (EClass) entry.getValue());
        handleEClassSuperType(currentCapellaClass, (EClass) entry.getValue());
        handleEOperations(currentCapellaClass, (EClass) entry.getValue());
      }
    }
  }

  /**
   * Handle EClass super type i.e inheritance.
   * @param currentCapellaClass
   * @param relatedEcoreClass
   */
  protected void handleEClassSuperType(org.polarsys.capella.core.data.information.Class currentCapellaClass, EClass relatedEcoreClass) {
    for (Generalization currentGeneralization : currentCapellaClass.getOwnedGeneralizations()) {
      GeneralizableElement superType = currentGeneralization.getSuper();
      // Lookup in capellaToEcore mappings to the converted element for superType.
      EClassifier ecoreSuperType = getEcoreType(superType);
      if (ecoreSuperType instanceof EClass) {
        relatedEcoreClass.getESuperTypes().add((EClass) ecoreSuperType);
      }
    }
  }

  /**
   * Handle EClass's operations.
   * @param capellaClass
   * @param value
   */
  protected void handleEOperations(Class capellaClass, EClass value) {
    // Loop over class operations.
    for (Operation currentOperation : capellaClass.getContainedOperations()) {
      createEOperation(value, currentOperation);
    }
  }

  /**
   * Walk through all given package contents to retrieve all classes and data types.
   * @param pkg
   * @param ecorePackage
   */
  protected void handlePackageContentWalk(DataPkg pkg, EPackage ecorePackage) {
    addDelegationAnnotation(pkg, ecorePackage);
    AbstractModelElementHandler handler = new AbstractModelElementHandler() {
      private ClassConverter _informationSwitch = new ClassConverter();
      private DataTypeConverter _dataTypeEcoreSwitch = new DataTypeConverter();

      /**
       * {@inheritDoc}
       */
      @SuppressWarnings("synthetic-access")
      @Override
      public Couple<EClassifier, Boolean> handleElement(ModelElement element, EPackage parentPackage, AbstractEcoreExporterCommand exporter) {
        boolean shouldPersistInResultingEcore = true;
        EClassifier classifier = null;
        if (element instanceof org.polarsys.capella.core.data.information.Class) {
          classifier = _informationSwitch.doSwitch(element);
        } else if (element instanceof DataType) {
          classifier = _dataTypeEcoreSwitch.doSwitch(element);
          // Check if returned DataType is a default Ecore one e.g EInt, EString...
          shouldPersistInResultingEcore = !(EcorePackage.eINSTANCE.equals(classifier.eContainer()));
        } else if (element instanceof Association) {
          _capellaAssociations.add((Association) element);
        } else if (element instanceof DataPkg) {
          // Recurse into sub package.
          DataPkg subCapellaPackage = (DataPkg) element;
          EPackage subEcorePackage = createEPackage(subCapellaPackage);
          addDelegationAnnotation(subCapellaPackage, subEcorePackage);
          exporter.walkThroughPackageContent(subCapellaPackage, subEcorePackage, this);
          parentPackage.getESubpackages().add(subEcorePackage);
        }
        return new Couple<EClassifier, Boolean>(classifier, Boolean.valueOf(shouldPersistInResultingEcore));
      }
    };
    walkThroughPackageContent(pkg, ecorePackage, handler);
  }

  /**
   * Handle References.
   */
  protected void handleReferences() {
    for (Association currentAssociation : _capellaAssociations) {
      // Two cases to handle.
      // Bi-directional relations : no property in OwnedMembers, 2 property in NavigableMembers.
      // One way relation : 1 property in OwnedMembers, 1 property in NavigableMembers.
      List<Property> ownedMembers = currentAssociation.getOwnedMembers();
      if (ownedMembers.isEmpty()) {
        // Bi-directional.
        // Get first relation property.
        Property property = currentAssociation.getNavigableMembers().get(0);
        AbstractType type = property.getAbstractType();
        // Get opposite relation property
        Property oppositeProperty = currentAssociation.getNavigableMembers().get(1);
        AbstractType oppositeType = oppositeProperty.getAbstractType();
        // Create two references set as opposite ones.
        EReference reference = createEReference(oppositeType, property);
        EReference oppositeReference = createEReference(type, oppositeProperty);
        reference.setEOpposite(oppositeReference);
        oppositeReference.setEOpposite(reference);
      } else {
        // One way.
        Property sourceProperty = ownedMembers.get(0);
        AbstractType capellaSourceContainer = sourceProperty.getAbstractType();
        // Get the targeted class.
        Property targetProperty = currentAssociation.getNavigableMembers().get(0);
        createEReference(capellaSourceContainer, targetProperty);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void exportPackage(AbstractDependenciesPkg capellaRootPackage, EPackage ecorePackage) {
    handlePackageContentWalk((DataPkg) capellaRootPackage, ecorePackage);
    handleClasses();
    handleReferences();
  }
}
