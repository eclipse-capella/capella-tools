/*******************************************************************************
 * Copyright (c) 2011 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.ocl.requester.view.provider;

import java.util.Iterator;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.ocl.types.TupleType;
import org.eclipse.ocl.util.Tuple;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * This adapter implementation provides reflective support that emulates the
 * behavior of a default generated item provider.
 */
public class TupleItemProvider
		extends ItemProviderAdapter
		implements IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

	public TupleItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		@SuppressWarnings("unchecked")
		Tuple<?, Object> tuple = (Tuple<?, Object>) object;
		TupleType<?, ?> tupleType = tuple.getTupleType();

		for (Object next : tupleType.oclProperties()) {

			TupleItemProviderAdapterFactory factory = (TupleItemProviderAdapterFactory) adapterFactory;
			IItemLabelProvider labeler = (IItemLabelProvider) factory
				.getRootAdapterFactory().adapt(tuple.getValue(next),
					IItemLabelProvider.class);

			if (labeler != null) {
				return labeler.getImage(tuple.getValue(next));
			}
		}

		return PlatformUI.getWorkbench().getSharedImages()
			.getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}

	/**
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		@SuppressWarnings("unchecked")
		Tuple<?, Object> tuple = (Tuple<?, Object>) object;
		TupleType<?, ?> tupleType = tuple.getTupleType();

		StringBuffer result = new StringBuffer();

		for (Iterator<?> iter = tupleType.oclProperties().iterator(); iter
			.hasNext();) {

			Object next = iter.next();

			TupleItemProviderAdapterFactory factory = (TupleItemProviderAdapterFactory) adapterFactory;
			IItemLabelProvider labeler = (IItemLabelProvider) factory
				.getRootAdapterFactory().adapt(tuple.getValue(next),
					IItemLabelProvider.class);

			if (labeler != null) {
				result.append(labeler.getText(tuple.getValue(next)));
			}

			if (iter.hasNext()) {
				result.append(";"); //$NON-NLS-1$
			}
		}

		return result.toString();
	}
}
