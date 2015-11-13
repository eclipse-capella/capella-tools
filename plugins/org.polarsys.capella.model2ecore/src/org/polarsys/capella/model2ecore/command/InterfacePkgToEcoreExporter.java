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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.polarsys.capella.common.data.modellingcore.AbstractExchangeItem;
import org.polarsys.capella.common.data.modellingcore.AbstractType;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.mdsofa.common.constant.ICommonConstants;
import org.polarsys.capella.common.mdsofa.common.misc.Couple;
import org.polarsys.capella.core.data.capellacore.AbstractDependenciesPkg;
import org.polarsys.capella.core.data.capellacore.GeneralizableElement;
import org.polarsys.capella.core.data.capellacore.Generalization;
import org.polarsys.capella.core.data.cs.ExchangeItemAllocation;
import org.polarsys.capella.core.data.cs.Interface;
import org.polarsys.capella.core.data.cs.InterfacePkg;
import org.polarsys.capella.core.data.information.ExchangeItem;
import org.polarsys.capella.core.data.information.ExchangeItemElement;
import org.polarsys.capella.core.data.information.ExchangeMechanism;
import org.polarsys.capella.core.data.information.ParameterDirection;
import org.polarsys.capella.core.data.information.communication.CommunicationLinkProtocol;
import org.polarsys.capella.core.data.information.datatype.DataType;
import org.polarsys.capella.core.data.information.datavalue.LiteralNumericValue;
import org.polarsys.capella.model2ecore.internal.util.AbstractModelElementHandler;
import org.polarsys.capella.model2ecore.internal.util.ModelHelper;


/**
 * @author Stephane Fournier
 */
public class InterfacePkgToEcoreExporter extends AbstractEcoreExporterCommand {

  /**
   * 
   */
  private static final String MODEL2ECORE_AGGREGATION = "Aggregation"; //$NON-NLS-1$
  /**
   * 
   */
  private static final String MODEL2ECORE_DATA = "data"; //$NON-NLS-1$
  /**
   * 
   */
  private static final String MODEL2ECORE_MESSAGE = "Message"; //$NON-NLS-1$
  /**
   * 
   */
  private static final String MODEL2ECORE_NON_SECTIONED_MESSAGE = "nonsectionedmessage"; //$NON-NLS-1$
  /**
   * 
   */
  private static final String MODEL2ECORE_MESSAGE_LENGTH = "messageLength"; //$NON-NLS-1$
  /**
   * 
   */
  private static final String MODEL2ECORE_ONEWAY = "@oneway"; //$NON-NLS-1$
  /**
   * 
   */
  private static final String MODEL2ECORE_STEREOTYPE = "@stereotype "; //$NON-NLS-1$
  /**
   * 
   */
  private static final String MODEL2ECORE_DATAFLOW = "dataflow"; //$NON-NLS-1$
  /**
   * 
   */
  private static final String MODEL2ECORE_RPC = "rpc"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public InterfacePkgToEcoreExporter() {
    super();
  }

  /**
   * Construtor.
   * @param capellaToEcoreMappings
   * @param packagesMappings
   */
  public InterfacePkgToEcoreExporter(Map<ModelElement, EClassifier> capellaToEcoreMappings, Map<AbstractDependenciesPkg, EPackage> packagesMappings) {
    super(capellaToEcoreMappings, packagesMappings);
  }

  /**
   * Add the Model2Ecore {@link #MODEL2ECORE_AGGREGATION} annotation on specified operation
   * @param eOperation
   * @param exchangeItemAllocation
   * @param exchangeItem
   */
  protected void addModel2EcoreAggregationAnnotation(EStructuralFeature reference) {
    EAnnotation model2EcoreAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
    reference.getEAnnotations().add(model2EcoreAnnotation);
    model2EcoreAnnotation.setSource(GENMODEL_ANNOTATION);

    // Create an annotation detail entry.
    EMap<String, String> annotationEntries = model2EcoreAnnotation.getDetails();
    annotationEntries.put(DOCUMENTATION_ANNOTATION_ENTRY_KEY, MODEL2ECORE_STEREOTYPE + MODEL2ECORE_AGGREGATION);
  }

  /**
   * Add the Model2Ecore {@link #MODEL2ECORE_ONEWAY} annotation
   * @param eOperation
   * @param exchangeItemAllocation
   * @param exchangeItem
   */
  protected void addModel2EcoreAsynchronousAnnotation(EOperation eOperation, ExchangeItemAllocation exchangeItemAllocation, ExchangeItem exchangeItem) {
    EAnnotation model2EcoreAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();

    // Create an annotation detail entry.
    EMap<String, String> annotationEntries = model2EcoreAnnotation.getDetails();
    String summary = exchangeItem.getSummary();
    String annotationContent = (null != summary) ? summary : ICommonConstants.EMPTY_STRING;
    if (exchangeItemAllocation.getSendProtocol().equals(CommunicationLinkProtocol.ASYNCHRONOUS)) {
      if (!annotationContent.isEmpty()) {
        annotationContent = annotationContent + '\n' + MODEL2ECORE_ONEWAY;
      } else {
        annotationContent = MODEL2ECORE_ONEWAY;
      }
    }
    if (!annotationContent.isEmpty()) {
      annotationEntries.put(DOCUMENTATION_ANNOTATION_ENTRY_KEY, annotationContent);
      eOperation.getEAnnotations().add(model2EcoreAnnotation);
      model2EcoreAnnotation.setSource(GENMODEL_ANNOTATION);
    }
  }

  /**
   * Add the Model2Ecore {@link #MODEL2ECORE_DATA} annotation on specified operation
   * @param eOperation
   * @param exchangeItemAllocation
   * @param exchangeItem
   */
  protected void addModel2EcoreDataAnnotation(EReference reference) {
    EAnnotation model2EcoreAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
    reference.getEAnnotations().add(model2EcoreAnnotation);
    model2EcoreAnnotation.setSource(GENMODEL_ANNOTATION);

    // Create an annotation detail entry.
    EMap<String, String> annotationEntries = model2EcoreAnnotation.getDetails();
    annotationEntries.put(DOCUMENTATION_ANNOTATION_ENTRY_KEY, MODEL2ECORE_STEREOTYPE + MODEL2ECORE_DATA);
  }

  /**
   * Add the Model2Ecore {@link #MODEL2ECORE_MESSAGE_LENGTH} annotation on specified operation
   * @param attribute
   */
  protected void addModel2EcoreMessageLengthAnnotation(EAttribute attribute) {
    EAnnotation model2EcoreAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
    attribute.getEAnnotations().add(model2EcoreAnnotation);
    model2EcoreAnnotation.setSource(GENMODEL_ANNOTATION);

    // Create an annotation detail entry.
    EMap<String, String> annotationEntries = model2EcoreAnnotation.getDetails();
    annotationEntries.put(DOCUMENTATION_ANNOTATION_ENTRY_KEY, MODEL2ECORE_STEREOTYPE + MODEL2ECORE_MESSAGE_LENGTH);
  }

  /**
   * Add the Model2Ecore {@link #MODEL2ECORE_NON_SECTIONED_MESSAGE} annotation
   * @param message
   */
  protected void addModel2EcoreNonSectionedMessageAnnotation(EClass message) {
    EAnnotation model2EcoreAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
    message.getEAnnotations().add(model2EcoreAnnotation);
    model2EcoreAnnotation.setSource(GENMODEL_ANNOTATION);

    // Create an annotation detail entry.
    EMap<String, String> annotationEntries = model2EcoreAnnotation.getDetails();
    annotationEntries.put(DOCUMENTATION_ANNOTATION_ENTRY_KEY, MODEL2ECORE_STEREOTYPE + MODEL2ECORE_NON_SECTIONED_MESSAGE);
  }

  /**
   * Add the Model2Ecore Pattern annotation on specified interface if not already defined.
   * @param ecoreInterface
   * @param exchangeItem
   */
  protected void addModel2EcorePatternAnnotation(EClass ecoreInterface, ExchangeItem exchangeItem) {
    // Check if the Model2Ecore annotation is already set or not.
    List<EAnnotation> interfaceAnnotations = ecoreInterface.getEAnnotations();
    // TODO : make it smarter : i.e checking if one annotation is really the Model2Ecore one.
    if (interfaceAnnotations.isEmpty()) {
      EAnnotation model2EcoreAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
      interfaceAnnotations.add(model2EcoreAnnotation);
      model2EcoreAnnotation.setSource(GENMODEL_ANNOTATION);

      // Create an annotation detail entry.
      EMap<String, String> annotationEntries = model2EcoreAnnotation.getDetails();

      String pattern = null;
      switch (exchangeItem.getExchangeMechanism().getValue()) {
        case ExchangeMechanism.OPERATION_VALUE:
          pattern = MODEL2ECORE_RPC;
        break;
        case ExchangeMechanism.FLOW_VALUE:
          pattern = MODEL2ECORE_DATAFLOW;
        break;
        default:
          System.err.println("ExchangeMechanism not handled:" + exchangeItem.getName() + " mechanism:" + exchangeItem.getExchangeMechanism()); //$NON-NLS-1$ //$NON-NLS-2$
      }

      annotationEntries.put(DOCUMENTATION_ANNOTATION_ENTRY_KEY, MODEL2ECORE_STEREOTYPE + pattern);
    }
  }

  /**
   * Create the data flow structure.
   * @param ecoreDataFlow
   * @param exchangeItem
   */
  protected void createDataFlowContent(EClass ecoreDataFlow, ExchangeItem exchangeItem) {
    // Add Model2Ecore PatternType info in documentation
    addModel2EcorePatternAnnotation(ecoreDataFlow, exchangeItem);

    // Create a Model2Ecore message object.
    EClass message = EcoreFactory.eINSTANCE.createEClass();
    // Set the DataStructure name with its annotation.
    message.setName(exchangeItem.getName() + MODEL2ECORE_MESSAGE);
    addModel2EcoreNonSectionedMessageAnnotation(message);
    ecoreDataFlow.getEPackage().getEClassifiers().add(message);

    // Add a composition relation between the DF and its message.
    createMessageReference(ecoreDataFlow, message);

    // Fill the message structure with exchange item data.
    createMessageContent(message, exchangeItem);
  }

  /**
   * @param relatedEcoreClass
   * @param exchangeItem
   */
  protected EOperation createEOperation(EClass relatedEcoreClass, ExchangeItem exchangeItem) {
    // Create the related EOperation.
    EOperation eOperation = EcoreFactory.eINSTANCE.createEOperation();
    eOperation.setName(ModelHelper.forceCharactersToEcoreNamingConventions(exchangeItem.getName()));
    relatedEcoreClass.getEOperations().add(eOperation);

    for (ExchangeItemElement exchangeItemElement : exchangeItem.getOwnedElements()) {
      ETypedElement ecoreElement = null;
      String name = null;
      ParameterDirection direction = exchangeItemElement.getDirection();
      if (ParameterDirection.RETURN.equals(direction)) {
        // Handle Return type.
        ecoreElement = eOperation;
      } else {
        // Handle operation's parameters.
        EParameter ecoreParameter = EcoreFactory.eINSTANCE.createEParameter();
        eOperation.getEParameters().add(ecoreParameter);
        name = exchangeItemElement.getName();
        ecoreElement = ecoreParameter;
        // Add comments if any
        String summary = exchangeItemElement.getSummary();
        if ((null != summary) && !summary.isEmpty()) {
          EAnnotation model2EcoreAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
          ecoreElement.getEAnnotations().add(model2EcoreAnnotation);
          model2EcoreAnnotation.setSource(GENMODEL_ANNOTATION);
          EMap<String, String> annotationEntries = model2EcoreAnnotation.getDetails();
          annotationEntries.put(DOCUMENTATION_ANNOTATION_ENTRY_KEY, summary);
        }
      }
      AbstractType capellaType = exchangeItemElement.getAbstractType();
      EClassifier ecoreType = (null != capellaType) ? getEcoreType(capellaType) : null;
      if (null != name) {
        ecoreElement.setName(ModelHelper.forceCharactersToEcoreNamingConventions(name));
      }
      if (null != ecoreType) {
        ecoreElement.setEType(ecoreType);
      } else {
        System.err.println("Ecore Type not found for " + eOperation.getName() + " parameter:" + name); //$NON-NLS-1$ //$NON-NLS-2$
      }

      ecoreElement.setLowerBound(ModelHelper.getValue((LiteralNumericValue) exchangeItemElement.getOwnedMinCard()));
      ecoreElement.setUpperBound(ModelHelper.getValue((LiteralNumericValue) exchangeItemElement.getOwnedMaxCard()));
    }
    return eOperation;
  }

  /**
   * Create message content
   */
  protected void createMessageContent(EClass message, ExchangeItem exchangeItem) {
    // Create the messageLength attribute.
    EAttribute ecoreAttribute = EcoreFactory.eINSTANCE.createEAttribute();
    addModel2EcoreMessageLengthAnnotation(ecoreAttribute);
    ecoreAttribute.setEType(EcorePackage.Literals.EINT);
    ecoreAttribute.setName(MODEL2ECORE_MESSAGE_LENGTH);
    ecoreAttribute.setUpperBound(1);
    ecoreAttribute.setLowerBound(1);
    message.getEStructuralFeatures().add(ecoreAttribute);

    List<ExchangeItemElement> ownedElements = exchangeItem.getOwnedElements();
    for (ExchangeItemElement exchangeItemElement : ownedElements) {
      // Create an EReference.
      AbstractType abstractType = exchangeItemElement.getAbstractType();
      EStructuralFeature contentRelation = EcoreFactory.eINSTANCE.createEReference();
      if (abstractType instanceof DataType) {
        contentRelation = EcoreFactory.eINSTANCE.createEAttribute();
      } else {
        // Set relation containment.
        ((EReference)contentRelation).setContainment(true);
      }
      addModel2EcoreAggregationAnnotation(contentRelation);
      // Get the related container of the relation.
      // Add it to its class container.
      message.getEStructuralFeatures().add(contentRelation);
      contentRelation.setEType(getEcoreType(abstractType));
      // Set bounds.
      contentRelation.setUpperBound(ModelHelper.getValue((LiteralNumericValue) exchangeItemElement.getOwnedMaxCard()));
      contentRelation.setLowerBound(ModelHelper.getValue((LiteralNumericValue) exchangeItemElement.getOwnedMinCard()));
      contentRelation.setName(ModelHelper.forceCharactersToEcoreNamingConventions(exchangeItemElement.getName()));
    }
  }

  /**
   * Create an {@link EReference} for the Model2Ecore message contained in a DataFlow.
   */
  protected void createMessageReference(EClass ecoreDataFlow, EClass dataStructure) {
    // Create an EReference.
    EReference reference = EcoreFactory.eINSTANCE.createEReference();
    // Get the related container of the relation.
    // Add it to its class container.
    ecoreDataFlow.getEStructuralFeatures().add(reference);
    reference.setEType(dataStructure);
    // Set bounds.
    reference.setUpperBound(1);
    reference.setLowerBound(1);
    reference.setName(ModelHelper.forceCharactersToEcoreNamingConventions(MODEL2ECORE_DATA));
    // Set relation containment.
    reference.setContainment(true);
    addModel2EcoreDataAnnotation(reference);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void exportPackage(AbstractDependenciesPkg capellaRootPackage, EPackage ecorePackage) {
    handlePackageContentWalk((InterfacePkg) capellaRootPackage, ecorePackage);
    handleInterfaces();
    handleDataFlow();
  }

  protected void handleDataFlow() {
    // Clone the map to avoid concurrent accesses.
    Map<EObject, EClassifier> capellaToEcoreMappings = new HashMap<EObject, EClassifier>(getCapellaToEcoreMappings());
    Iterator<Entry<EObject, EClassifier>> iterator = capellaToEcoreMappings.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<EObject, EClassifier> entry = iterator.next();
      EObject key = entry.getKey();
      // Handle Interface services.
      if (key instanceof ExchangeItem) {
        ExchangeItem exchangeItem = (ExchangeItem) key;
        EClass ecoreDataflow = (EClass) entry.getValue();
        createDataFlowContent(ecoreDataflow, exchangeItem);
      }
    }
  }

  /**
   * Handle exchange item allocation (i.eservices) contained in specified interface.
   * @param capellaInterface
   * @param relatedEcoreClass
   */
  protected void handleExchangeItemAllocation(Interface capellaInterface, EClass relatedEcoreClass) {
    // Handle interface inheritance if any.
    System.out.println();
    EList<Generalization> ownedGeneralizations = capellaInterface.getOwnedGeneralizations();
    for (Generalization generalization : ownedGeneralizations) {
      GeneralizableElement superInterface = generalization.getSuper();
      relatedEcoreClass.getESuperTypes().add((EClass) getEcoreType(superInterface));
    }

    for (ExchangeItemAllocation currentExchangeItemAllocation : capellaInterface.getOwnedExchangeItemAllocations()) {
      AbstractExchangeItem allocatedItem = currentExchangeItemAllocation.getAllocatedItem();
      if (allocatedItem instanceof ExchangeItem) {
        ExchangeItem exchangeItem = (ExchangeItem) allocatedItem;
        ExchangeMechanism exchangeMechanism = exchangeItem.getExchangeMechanism();
        // Add Model2Ecore PatternType info in documentation
        addModel2EcorePatternAnnotation(relatedEcoreClass, exchangeItem);

        // Handle operation exchange item only.
        if (ExchangeMechanism.OPERATION.equals(exchangeMechanism)) {
          EOperation eOperation = createEOperation(relatedEcoreClass, exchangeItem);
          // ASS Model2Ecore PatternType in documentation
          addModel2EcoreAsynchronousAnnotation(eOperation, currentExchangeItemAllocation, exchangeItem);
        }
      }
    }
  }

  /**
   * Handle retrieved interfaces.
   */
  protected void handleInterfaces() {
    // Clone the map to avoid concurrent accesses.
    Map<EObject, EClassifier> capellaToEcoreMappings = new HashMap<EObject, EClassifier>(getCapellaToEcoreMappings());
    Iterator<Entry<EObject, EClassifier>> iterator = capellaToEcoreMappings.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<EObject, EClassifier> entry = iterator.next();
      EObject key = entry.getKey();
      // Handle Interface services.
      if (key instanceof Interface) {
        Interface currentCapellaInterface = (Interface) key;
        handleExchangeItemAllocation(currentCapellaInterface, (EClass) entry.getValue());
      }
    }
  }

  /**
   * Walk through all given package contents to retrieve all interfaces.
   * @param capellaRootPackage
   * @param ecorePackage
   */
  protected void handlePackageContentWalk(InterfacePkg capellaRootPackage, EPackage ecorePackage) {
    AbstractModelElementHandler handler = new AbstractModelElementHandler() {

      @Override
      public Couple<EClassifier, Boolean> handleElement(ModelElement element, EPackage parentPackage, AbstractEcoreExporterCommand exporter) {
        EClassifier classifier = null;
        boolean shouldPersistInResultingEcore = true;
        if (element instanceof Interface) {
          Interface interface_l = (Interface) element;
          List<ExchangeItemAllocation> ownedExchangeItemAllocations = interface_l.getOwnedExchangeItemAllocations();
          // Don't care about empty interface.
          if (!ownedExchangeItemAllocations.isEmpty()) {
            // Filter out DataFlow interface here.
            ExchangeItem allocatedItem = ownedExchangeItemAllocations.get(0).getAllocatedItem();
            if (allocatedItem.getExchangeMechanism().equals(ExchangeMechanism.OPERATION)) {
              EClass ecoreInterface = EcoreFactory.eINSTANCE.createEClass();
              ecoreInterface.setName(ModelHelper.forceCharactersToEcoreNamingConventions(((Interface) element).getName()));
              ecoreInterface.setInterface(true);
              ecoreInterface.setAbstract(true);
              classifier = ecoreInterface;
            }
          }
        } else if (element instanceof InterfacePkg) {
          // Recurse into sub package.
          InterfacePkg subCapellaPackage = (InterfacePkg) element;
          EPackage subEcorePackage = createEPackage(subCapellaPackage);
          exporter.walkThroughPackageContent(subCapellaPackage, subEcorePackage, this);
          parentPackage.getESubpackages().add(subEcorePackage);
        } else if (element instanceof ExchangeItem) {
          ExchangeItem exchangeItem = (ExchangeItem) element;
          if (exchangeItem.getExchangeMechanism().equals(ExchangeMechanism.FLOW)) {
            EClass ecoreDataFlow = EcoreFactory.eINSTANCE.createEClass();
            ecoreDataFlow.setName(ModelHelper.forceCharactersToEcoreNamingConventions(exchangeItem.getName()));
            classifier = ecoreDataFlow;
          }
        }
        return new Couple<EClassifier, Boolean>(classifier, Boolean.valueOf(shouldPersistInResultingEcore));
      }

    };
    walkThroughPackageContent(capellaRootPackage, ecorePackage, handler);
  }
}
