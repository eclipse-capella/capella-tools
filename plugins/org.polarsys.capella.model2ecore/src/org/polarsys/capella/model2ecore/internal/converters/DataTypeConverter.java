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
package org.polarsys.capella.model2ecore.internal.converters;

import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.polarsys.capella.core.data.capellacore.AbstractPropertyValue;
import org.polarsys.capella.core.data.information.datatype.BooleanType;
import org.polarsys.capella.core.data.information.datatype.DataType;
import org.polarsys.capella.core.data.information.datatype.Enumeration;
import org.polarsys.capella.core.data.information.datatype.NumericType;
import org.polarsys.capella.core.data.information.datatype.NumericTypeKind;
import org.polarsys.capella.core.data.information.datatype.StringType;
import org.polarsys.capella.core.data.information.datatype.util.DatatypeSwitch;
import org.polarsys.capella.core.data.information.datavalue.DataValue;
import org.polarsys.capella.core.data.information.datavalue.EnumerationLiteral;
import org.polarsys.capella.core.data.information.datavalue.LiteralNumericValue;
import org.polarsys.capella.model2ecore.internal.util.ModelHelper;


/**
 * @author Stephane Fournier
 */
public class DataTypeConverter extends DatatypeSwitch<EClassifier> {
  /**
   * {@inheritDoc}
   */
  @Override
  public EClassifier caseBooleanType(BooleanType booleanType) {
    EClassifier result = null;
    if (Boolean.class.getSimpleName().equals(booleanType.getName())) {
      result = EcorePackage.Literals.EBOOLEAN;
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EClassifier caseDataType(DataType dataType) {
    EClassifier result = EcoreFactory.eINSTANCE.createEDataType();
    String dataTypeName = ModelHelper.forceCharactersToEcoreNamingConventions(dataType.getName());
    result.setName(dataTypeName);
    result.setInstanceTypeName(dataTypeName);
    return result;
  }

  /**
   * Create an Ecore enum for specified Capella Enum. {@inheritDoc}
   */
  @Override
  public EClassifier caseEnumeration(Enumeration capellaEnum) {
    EEnum ecoreEnum = EcoreFactory.eINSTANCE.createEEnum();
    ecoreEnum.setName(ModelHelper.forceCharactersToEcoreNamingConventions(capellaEnum.getName()));
    // Create literals according to Capella ones.
    for (EnumerationLiteral capellaLiteral : capellaEnum.getOwnedLiterals()) {
      EEnumLiteral ecoreEnumLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
      // Set name, default Literal is name value
      ecoreEnumLiteral.setName(ModelHelper.forceCharactersToEcoreNamingConventions(capellaLiteral.getName()));
      DataValue integerValue = capellaLiteral.getDomainValue();
      if (integerValue instanceof LiteralNumericValue) {
        ecoreEnumLiteral.setValue(ModelHelper.getValue((LiteralNumericValue) integerValue));
      }
      ecoreEnum.getELiterals().add(ecoreEnumLiteral);
    }
    return ecoreEnum;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EClassifier caseNumericType(NumericType numericType) {
    EClassifier result = null;
    // Handle Predefined data types of SA.
    if (Byte.class.getSimpleName().equals(numericType.getName())) {
      result = EcorePackage.Literals.EBYTE;
    } else if (Double.class.getSimpleName().equals(numericType.getName())) {
      result = EcorePackage.Literals.EDOUBLE;
    } else if (Float.class.getSimpleName().equals(numericType.getName())) {
      result = EcorePackage.Literals.EFLOAT;
    } else if ("UnsignedInteger".equals(numericType.getName()) || Integer.class.getSimpleName().equals(numericType.getName())) { //$NON-NLS-1$
      result = EcorePackage.Literals.EINT;
    } else if ("UnsignedLong".equals(numericType.getName()) || Long.class.getSimpleName().equals(numericType.getName())) { //$NON-NLS-1$
      result = EcorePackage.Literals.ELONG;
    } else if ("UnsignedShort".equals(numericType.getName()) || Short.class.getSimpleName().equals(numericType.getName())) { //$NON-NLS-1$
      result = EcorePackage.Literals.ESHORT;
    } else if ("UnsignedLongLong".equals(numericType.getName()) || "LongLong".equals(numericType.getName())) { //$NON-NLS-1$ //$NON-NLS-2$
      result = EcorePackage.Literals.EBIG_INTEGER;
    } else {
      if (Date.class.getSimpleName().equals(numericType.getName())) {
        result = EcorePackage.Literals.EDATE;
      } else {
        // Handle Capella Data Type as EClass if the capella data type contains attributes.
        List<AbstractPropertyValue> propertyValues = numericType.getOwnedPropertyValues();
        String dataTypeName = ModelHelper.forceCharactersToEcoreNamingConventions(numericType.getName());
        if (!propertyValues.isEmpty()) {
          result = EcoreFactory.eINSTANCE.createEClass();
          result.setName(dataTypeName);
          // Loop over property values to create related Ecore attributes.
          for (AbstractPropertyValue currentPropertyValue : propertyValues) {
            EAttribute ecoreAttribute = EcoreFactory.eINSTANCE.createEAttribute();
            ((EClass) result).getEStructuralFeatures().add(ecoreAttribute);
            ecoreAttribute.setName(currentPropertyValue.getName());
            ecoreAttribute.setEType(ModelHelper.getClassifier(currentPropertyValue));
            ecoreAttribute.setLowerBound(1);
            ecoreAttribute.setUpperBound(1);
          }
        } else {
          // Create areal DataTypes.
          result = EcoreFactory.eINSTANCE.createEDataType();
          result.setName(dataTypeName);
          switch (numericType.getKind().getValue()) {
            case NumericTypeKind.INTEGER_VALUE:
              result.setInstanceClassName("int");
              result.setInstanceTypeName("int");
            break;
            case NumericTypeKind.FLOAT_VALUE:
              result.setInstanceClassName("float");
              result.setInstanceTypeName("float");
            break;
            default:
            break;
          }
        }
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EClassifier caseStringType(StringType stringType) {
    EClassifier result = null;
    if (String.class.getSimpleName().equals(stringType.getName())) {
      result = EcorePackage.Literals.ESTRING;
    } else if ("Char".equals(stringType.getName())) { //$NON-NLS-1$
      result = EcorePackage.Literals.ECHAR;
    }
    return result;
  }

  /**
   * Get the model package this switch is running against.
   * @return a not <code>null</code> package.
   */
  public EPackage getModelPackage() {
    return modelPackage;
  }
}
