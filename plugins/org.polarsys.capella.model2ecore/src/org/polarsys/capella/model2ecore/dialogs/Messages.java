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

import org.eclipse.osgi.util.NLS;

/**
 * @author Stephane Fournier
 *
 */
public class Messages extends NLS {
  private static final String BUNDLE_NAME = "org.polarsys.capella.model2ecore.dialogs.messages"; //$NON-NLS-1$
  public static String ExporterDestinationDialog_DataPackage_Title;
  public static String ExporterDestinationDialog_IncludeButton_Title;
  public static String ExporterDestinationDialog_InterfacePackage_Title;
  public static String ExporterDestinationDialog_Message;
  public static String ExporterDestinationDialog_Title;
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}
