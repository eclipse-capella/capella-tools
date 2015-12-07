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
package org.polarsys.capella.groovy;

import java.io.Reader;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import groovy.lang.GroovyShell;

/**
 * @author Soyatec
 */
public class RunCapellaScriptCommand extends RecordingCommand {

  private final GroovyShell shell;
  private final Reader reader;

  RunCapellaScriptCommand(TransactionalEditingDomain ted, GroovyShell shell, Reader reader) {
    super(ted, "Run Capella Script");
    this.shell = shell;
    this.reader = reader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {
    shell.evaluate(reader);
  }
}
