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
package org.polarsys.capella.ocl.requester.decorators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.ComposedImage.Point;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.polarsys.capella.common.mdsofa.common.misc.Couple;

/**
 * Abstract Decorator to help decorates native AND legacy object in JFace
 * viewers.
 * 
 * Can accept any EObject and only doDecorate native AND legacy ones.
 * 
 * The enablement of the decorator has to be on : objectClass
 * name="org.eclipse.emf.ecore.EObject"
 */
public abstract class AbstractDecorator implements ILabelDecorator {

	/**
	 * Image overlay positions.
	 */
	protected enum OverlayPosition {
		/**
		 * North East.
		 */
		NE(9, 0),
		/**
		 * North West.
		 */
		NW(0, 0),
		/**
		 * South East.
		 */
		SE(9, 9),
		/**
		 * South West.
		 */
		SO(0, 9);

		/**
		 * Coordinates.
		 */
		protected final Point point;

		OverlayPosition(int x, int y) {
			point = new Point();
			point.x = x;
			point.y = y;
		}
	}

	/**
	 * ComposedImage cache.
	 */
	protected Map<String, ComposedImage> composedImagesCache = new HashMap<String, ComposedImage>(
		0);

	/**
	 * {@inheritDoc}
	 */
	public void addListener(ILabelProviderListener listener) {
		// Do nothing.
	}

	/**
	 * {@inheritDoc}
	 */
	public Image decorateImage(Image initialImage, Object element) {
		Image result = null; /* means no decoration see javadoc */
		EObject eObject = null;
		// If the selected element can be adapted as an EObject
		if (element instanceof IAdaptable) {
			// We consider the EObject
			eObject = (EObject) ((IAdaptable) element)
				.getAdapter(EObject.class);
		} else if (element instanceof EObject) {
			eObject = (EObject) element;
		}

		List<String> suffixes = new ArrayList<String>(0);
		Image imageLock = doDecorateImage(eObject, element, suffixes);
		if (null != imageLock) {
			// Need to get a decorated image.
			Couple<List<Object>, List<Point>> imageStructures = getImageStructures(
				eObject, initialImage);
			List<Object> images = imageStructures.getKey();
			// Add the overlay image.
			images.add(imageLock);
			List<Point> positions = imageStructures.getValue();
			// Add the overlay position.
			positions.add(getOverlayPosition().point);
			// Compute if needed a composed image.
			result = getComposedImage(getImageKey(eObject, suffixes), images,
				positions);
		}

		return result;
	}

	/**
	 * actually compute the image decoration.
	 * 
	 * @param eObjectElement
	 *            original element
	 * @param element
	 *            corresponding element
	 * @param suffixes
	 *            possible image suffixes
	 * 
	 * @return a composed Image
	 */
	protected abstract Image doDecorateImage(EObject eObjectElement, Object element, List<String> suffixes);

	/**
	 * {@inheritDoc}
	 */
	public String decorateText(String initialText, Object element) {
		String result = null; /* means no decoration see javadoc */

		EObject eObject = null;
		// If the selected element can be adapted as an EObject
		if (element instanceof IAdaptable) {
			// We consider the EObject
			eObject = (EObject) ((IAdaptable) element)
				.getAdapter(EObject.class);
		} else if (element instanceof EObject) {
			eObject = (EObject) element;
		}

		result = doDecorateText(initialText, element, eObject);

		return result;
	}

	/**
	 * actually compute the decorated text.
	 * 
	 * @param initialText
	 *            initial text
	 * @param element
	 *            original element
	 * @param eObjectElement
	 *            corresponding element
	 * 
	 * @return a new decorated text
	 */
	protected abstract String doDecorateText(String initialText,
			Object element, EObject eObjectElement);

	/**
	 * Get default Lock overlay position.<br>
	 * Default position is bottom right i.e South East.
	 * 
	 * @return a not <code>null</code> {@link OverlayPosition}.
	 */
	protected OverlayPosition getOverlayPosition() {
		return OverlayPosition.SE;
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose() {
		// Do nothing.
	}

	/**
	 * Get image for specified key, and/or composition.<br>
	 * If no image exists for specified key, a new one is created from specified
	 * composition.
	 * 
	 * @param key
	 *            composed image key.
	 * @param images
	 *            images involved in the composed image.
	 * @param positions
	 *            positions of images involved in the composed image.
	 * @return a composed image, should be not <code>null</code>.
	 */
	protected Image getComposedImage(String key, List<Object> images,
			final List<Point> positions) {
		// Try to get the image from the cache.
		ComposedImage result = composedImagesCache.get(key);
		if (null == result) {
			result = new ComposedImage(images) {

				@Override
				public List<Point> getDrawPoints(Size size) {
					return positions;
				}
			};
			composedImagesCache.put(key, result);
		}
		return ExtendedImageRegistry.getInstance().getImage(result);
	}

	/**
	 * Compute image key from element and specified suffixes.
	 * 
	 * @param object
	 *            Object we get an image for.
	 * @param suffixes
	 *            all overlay images added suffixes, used to compute the
	 *            composed image name.
	 * @return a unique image id.
	 */
	protected String getImageKey(EObject object, List<String> suffixes) {
		StringBuilder builder = new StringBuilder(object.eClass().getName());
		for (String string : suffixes) {
			if (null != string) {
				builder.append('_').append(string);
			}
		}
		return builder.toString();
	}

	/**
	 * Get minimum image structures for specified element.
	 * 
	 * @param object
	 *            Object we get the image structures for.
	 * @param initialImage
	 *            non decorated image related to specified eObject.
	 * @return image structures.
	 */
	private Couple<List<Object>, List<Point>> getImageStructures(
			EObject object, Image initialImage) {
		List<Object> images = new ArrayList<Object>(1);
		images.add(initialImage);
		List<Point> positions = new ArrayList<Point>(1);
		positions.add(OverlayPosition.NW.point);
		return new Couple<List<Object>, List<Point>>(images, positions);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLabelProperty(Object element, String property) {
		// Do nothing.
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeListener(ILabelProviderListener listener) {
		// Do nothing.
	}

}