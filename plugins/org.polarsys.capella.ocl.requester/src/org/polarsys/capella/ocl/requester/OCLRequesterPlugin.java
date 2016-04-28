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
package org.polarsys.capella.ocl.requester;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Main class of the plugin.
 */
public class OCLRequesterPlugin extends AbstractUIPlugin {

	// The shared instance.
	private static OCLRequesterPlugin plugin;

	/**
	 * The constructor.
	 */
	public OCLRequesterPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Obtains my plug-in ID.
	 * 
	 * @return my plug-in ID
	 */
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static OCLRequesterPlugin getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String key) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(getPluginId(), key);
	}
}
