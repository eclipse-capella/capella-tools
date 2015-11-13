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
package org.polarsys.capella.model2ecore.internal.util;

import java.util.StringTokenizer;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EcorePackage;
import org.polarsys.capella.common.data.modellingcore.ModelElement;
import org.polarsys.capella.common.mdsofa.common.constant.ICommonConstants;
import org.polarsys.capella.core.data.capellacore.AbstractDependenciesPkg;
import org.polarsys.capella.core.data.capellacore.AbstractPropertyValue;
import org.polarsys.capella.core.data.capellacore.CapellacorePackage;
import org.polarsys.capella.core.data.information.datavalue.LiteralNumericValue;

/**
 * @author Stephane Fournier
 */
public class ModelHelper {
  /**
   * Ecore Many cardinality <code>-1</code>
   */
  private static final int ECORE_MANY_CARDINALITY = -1;
  /**
   * Capella Many cardinality <code>*</code>
   */
  private static final String CAPELLA_MANY_CARDINALITY = "*"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  private ModelHelper() {
    super();
  }
  /**
   * Get Package delegation annotation name.
   * @param package_p
   * @return
   */
  public static String getPackageDelegationAnnotationName(AbstractDependenciesPkg package_p) {
    return getPackageFullName(package_p, ICommonConstants.EMPTY_STRING + ICommonConstants.POINT_CHARACTER);
  }

  /**
   * Get package full name.
   * @param package_p
   * @param replaceSequence_p string to use as delimiter replacement of the original one contained in {@link ModelElement#getFullLabel()};<br>
   *          if <code>null</code> '-' is used by default.
   * @return a not <code>null</code> string.
   */
  public static String getPackageFullName(AbstractDependenciesPkg package_p, String replaceSequence_p) {
    String newDelimiter = replaceSequence_p;
    if (null == newDelimiter) {
      newDelimiter = ICommonConstants.EMPTY_STRING + '-';
    }
    StringTokenizer tokenizer = new StringTokenizer(package_p.getFullLabel(), ICommonConstants.EMPTY_STRING + ICommonConstants.SLASH_CHARACTER);
    tokenizer.nextToken();
    StringBuilder shortResourceName = new StringBuilder();
    while (tokenizer.hasMoreElements()) {
      String token = tokenizer.nextToken();
      shortResourceName.append(token.replaceAll(ICommonConstants.EMPTY_STRING + ICommonConstants.WHITE_SPACE_CHARACTER, ICommonConstants.EMPTY_STRING));
      if (tokenizer.hasMoreTokens()) {
        shortResourceName.append(newDelimiter);
      }
    }
    return shortResourceName.toString();
  }

  /**
   * Ensure specified name is compliant with Ecore names.
   * @param name_p
   * @return a not <code>null</code> string.
   */
  public static String forceCharactersToEcoreNamingConventions(String name_p) {
    if (null == name_p) {
      return name_p;
    }
    return name_p.replaceAll(ICommonConstants.EMPTY_STRING + ICommonConstants.WHITE_SPACE_CHARACTER, ICommonConstants.EMPTY_STRING);
  }

  /**
   * Get the int value of specified numeric value.
   * @param value_p
   * @return
   */
  public static int getValue(LiteralNumericValue value_p) {
    if (value_p.getValue().contains(CAPELLA_MANY_CARDINALITY)) {
      return ECORE_MANY_CARDINALITY;
    }
    int intValue;
    try {
      intValue = Integer.parseInt(value_p.getValue());
    } catch (NumberFormatException exception_p) {
      intValue = 0;
    }
    return intValue;
  }

  /**
   * Get related Ecore classifier for specified property value.
   * @param capellaPropertyValue_p
   * @return
   */
  public static EClassifier getClassifier(AbstractPropertyValue capellaPropertyValue_p) {
    EClassifier result = null;
    switch (capellaPropertyValue_p.eClass().getClassifierID()) {
      case CapellacorePackage.BOOLEAN_PROPERTY_VALUE:
        result = EcorePackage.Literals.EBOOLEAN;
      break;
      case CapellacorePackage.FLOAT_PROPERTY_VALUE:
        result = EcorePackage.Literals.EFLOAT;
      break;
      case CapellacorePackage.INTEGER_PROPERTY_VALUE:
        result = EcorePackage.Literals.EINT;
      break;
    }
    return result;
  }
}
