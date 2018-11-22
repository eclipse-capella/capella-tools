/*******************************************************************************
 * Copyright (c) 2018 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * Contributors:
 *    Felix Dorner <felix.dorner@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.groovy;

import org.eclipse.emf.common.util.TreeIterator;

/**
 * Helper to allow using the groovy iterator extension methods each/grep etc
 * on EMF containment trees. The mentioned extension methods do not work
 * on the iterator returned by eAllContents() because that iterator is also
 * a list, and groovy dispatches the extension method calls to the wrong
 * implementation.
 */
public class ForwardingTreeIterator<E> implements TreeIterator<E> {

  public ForwardingTreeIterator(TreeIterator<E> delegate){
    this.delegate = delegate;
  }

  private final TreeIterator<E> delegate;
  
  @Override
  public boolean hasNext() {
    return delegate.hasNext();
  }

  @Override
  public E  next() {
    return delegate.next();
  }

  @Override
  public void prune() {
    delegate.prune();
  }

}
