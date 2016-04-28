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
package org.polarsys.capella.ocl.requester.view.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * Action that is used to select the metamodel and the level on which ocl
 * expressions should be performed.
 */
public class DropDownAction extends Action implements IMenuCreator {

	private Menu menu;

	private List<IAction> actions = new java.util.ArrayList<IAction>();

	private IPropertyChangeListener listener = new IPropertyChangeListener() {

		public void propertyChange(PropertyChangeEvent event) {
			if (IAction.CHECKED.equals(event.getProperty())) {
				if (Boolean.TRUE.equals(event.getNewValue())) {
					actionChecked((IAction) event.getSource());
				}
			}
		}
	};

	public DropDownAction() {
		super();

		setMenuCreator(this);
	}

	public void addAction(IAction action) {
		actions.add(action);
		action.addPropertyChangeListener(listener);

		if (action.isChecked()) {
			actionChecked(action);
		}
	}

	private void actionChecked(IAction action) {
		setImageDescriptor(action.getImageDescriptor());
		setText(action.getText());
	}

	public Menu getMenu(Control parent) {
		if (menu == null) {
			menu = new Menu(parent);

			for (IAction action : actions) {
				addAction(menu, action);
			}
		}

		return menu;
	}

	private void addAction(Menu m, IAction action) {
		ActionContributionItem contrib = new ActionContributionItem(action);
		contrib.fill(m, -1);
	}

	public void dispose() {
		if (menu != null) {
			menu.dispose();
		}
	}

	public Menu getMenu(Menu parent) {
		return null;
	}

	@Override
	public void run() {
		for (IAction action : actions) {
			if (action.isChecked()) {
				action.run();
			}
		}
	}
}