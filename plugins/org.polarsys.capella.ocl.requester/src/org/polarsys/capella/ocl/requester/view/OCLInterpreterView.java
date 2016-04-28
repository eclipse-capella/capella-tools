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
package org.polarsys.capella.ocl.requester.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.ConstraintKind;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.ocl.types.TupleType;
import org.eclipse.ocl.util.Tuple;
import org.eclipse.sirius.ui.tools.internal.views.interpreterview.VariableContentProvider;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.polarsys.capella.core.model.handler.helpers.CapellaAdapterHelper;
import org.polarsys.capella.ocl.requester.OCLRequesterPlugin;
import org.polarsys.capella.ocl.requester.console.IOCLFactory;
import org.polarsys.capella.ocl.requester.console.text.ColorManager;
import org.polarsys.capella.ocl.requester.console.text.OCLDocument;
import org.polarsys.capella.ocl.requester.console.text.OCLSourceViewer;
import org.polarsys.capella.ocl.requester.internal.l10n.OCLInterpreterMessages;
import org.polarsys.capella.ocl.requester.level.ModelingLevel;
import org.polarsys.capella.ocl.requester.patterns.EcoreOCLFactory;
import org.polarsys.capella.ocl.requester.view.actions.ClearOutputAction;
import org.polarsys.capella.ocl.requester.view.actions.DropDownAction;
import org.polarsys.capella.ocl.requester.view.actions.EcoreMetamodelAction;
import org.polarsys.capella.ocl.requester.view.actions.ExportOCLTextAction;
import org.polarsys.capella.ocl.requester.view.actions.ExportResultAction;
import org.polarsys.capella.ocl.requester.view.actions.HistoryBackAction;
import org.polarsys.capella.ocl.requester.view.actions.HistoryForwardAction;
import org.polarsys.capella.ocl.requester.view.actions.ImportOCLTextAction;
import org.polarsys.capella.ocl.requester.view.actions.ModelingLevelAction;
import org.polarsys.capella.ocl.requester.view.actions.OCLEvaluateAction;
import org.polarsys.capella.ocl.requester.view.provider.TupleItemProviderAdapterFactory;

/**
 * View that permit the user to run OCL expression on the model.
 */
public class OCLInterpreterView extends ViewPart {

	/**
	 * Default constructor.
	 */
	public OCLInterpreterView() {
	}

	/**
	 * Plugin status
	 */
	private static int BUNDLE_AVAILABLE = Bundle.RESOLVED | Bundle.ACTIVE
		| Bundle.STARTING;

	/**
	 * View ID.
	 */
	public static final String ID = "org.polarsys.capella.ocl.requester.view.OCLInterpreterID"; //$NON-NLS-1$

	/**
	 * The root widget
	 */
	private Composite page;

	/**
	 * Tree viewer to render model output
	 */
	private TreeViewer treeViewer;

	/**
	 * The text viewer for console output
	 */
	private ITextViewer output;

	/**
	 * The text viewer for input
	 */
	protected OCLSourceViewer input;

	/**
	 * The in-memory input document
	 */
	protected OCLDocument document;

	/**
	 * A registry of color resources
	 */
	private ColorManager colorManager;

	/**
	 * The last successfully parsed OCL expression
	 */
	private String lastOCLExpression;

	/**
	 * The non-null context (self)
	 */
	protected EObject context;

	/**
	 * Service in charge to provide current selection.
	 */
	private ISelectionService selectionService;

	/**
	 * Selection listener
	 */
	private ISelectionListener selectionListener;

	/**
	 * OCL Factory
	 */
	private IOCLFactory<Object> oclFactory;

	/**
	 * OCL
	 */
	private OCL<?, Object, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> ocl;

	/**
	 * Modeling level
	 */
	private ModelingLevel modelingLevel = ModelingLevel.M2Simplified;

	private static final AdapterFactory reflectiveAdapterFactory = new ReflectiveItemProviderAdapterFactory();

	private static final AdapterFactory defaultAdapterFactory = new ComposedAdapterFactory(
		ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

	private ITreeContentProvider contentProvider;

	/**
	 * Label that display the selected object of the model.
	 */
	private Label contextName;

	/**
	 * Label that display errors.
	 */
	private Label errorOutput;

	/**
	 * Label Provider used by Model output
	 */
	private ILabelProvider labelProvider;

	/**
	 * Drop down menu to select the metamodel action.
	 */
	private DropDownAction metamodelAction = new DropDownAction();

	/**
	 * Drop down menu to select the level action.
	 */
	private DropDownAction levelAction = new DropDownAction();

	private boolean evaluationSuccess = false;

	private List<String> history = new ArrayList<String>();

	private int currentHistoryPointer = 0;

	/**
	 * Label Provider used by Console output.
	 */
	public IItemLabelProvider tupleTypeLabelProvider = new IItemLabelProvider() {

		public Object getImage(Object object) {
			return null;
		}

		public String getText(Object object) {
			@SuppressWarnings("unchecked")
			Tuple<?, Object> tuple = (Tuple<?, Object>) object;
			TupleType<?, ?> tupleType = tuple.getTupleType();

			StringBuffer result = new StringBuffer();
			//			result.append("Tuple{");//$NON-NLS-1$

			for (Iterator<?> iter = tupleType.oclProperties().iterator(); iter
				.hasNext();) {

				Object next = iter.next();

				// result.append(oclFactory.getName(next));
				//				result.append(" = "); //$NON-NLS-1$
				result.append(OCLInterpreterView.this.toString(tuple
					.getValue(next)));

				if (iter.hasNext()) {
					result.append(","); //$NON-NLS-1$
				}
			}

			// result.append('}');

			return result.toString();
		}
	};

	@Override
	public void createPartControl(Composite parent) {

		// force left-to-right text direction in the console, because it
		// works with OCL text and the OCL language is based on English
		page = new SashForm(parent, SWT.VERTICAL | SWT.LEFT_TO_RIGHT);
		page.setFont(JFaceResources.getHeaderFont());

		colorManager = new ColorManager();
		document = new OCLDocument();
		document.setModelingLevel(modelingLevel);

		contextName = new Label(page, SWT.NONE);

		errorOutput = new Label(page, SWT.NONE);
		errorOutput.setForeground(colorManager
			.getColor(ColorManager.OUTPUT_ERROR));

		input = new OCLSourceViewer(page, colorManager, SWT.BORDER | SWT.MULTI);

		input.setDocument(document);
		input.getTextWidget().addKeyListener(new InputKeyListener());
		input.getTextWidget().setFont(
			JFaceResources.getFont(JFaceResources.TEXT_FONT));

		CTabFolder tabFolder = new CTabFolder(page, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
			SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tabItem1 = new CTabItem(tabFolder, SWT.NONE);
		tabItem1
			.setText(OCLInterpreterMessages.OCLInterpreterView_tbtmModelOutput_text);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem1.setControl(composite);
		composite.setLayout(new TreeColumnLayout());

		ComposedAdapterFactory adapterFactory = (ComposedAdapterFactory) SiriusEditPlugin
			.getPlugin().getItemProvidersAdapterFactory();
		adapterFactory
			.insertAdapterFactory(new TupleItemProviderAdapterFactory());
		labelProvider = new AdapterFactoryLabelProvider(adapterFactory);

		treeViewer = new TreeViewer(composite, SWT.BORDER | SWT.MULTI);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		treeViewer.setContentProvider(getContentProvider());
		treeViewer.setLabelProvider(labelProvider);

		CTabItem tabItem2 = new CTabItem(tabFolder, SWT.NONE);
		tabItem2.setText("Console Output"); //$NON-NLS-1$

		output = new TextViewer(tabFolder, SWT.BORDER | SWT.MULTI
			| SWT.V_SCROLL | SWT.H_SCROLL);
		StyledText styledTextOutput = output.getTextWidget();
		tabItem2.setControl(styledTextOutput);
		output.getTextWidget().setLayoutData(new GridData(GridData.FILL_BOTH));
		output.getTextWidget().setFont(
			JFaceResources.getFont(JFaceResources.TEXT_FONT));
		output.setEditable(false);
		output.setDocument(new Document());

		tabFolder.setSelection(0);

		selectionListener = new ISelectionListener() {

			public void selectionChanged(IWorkbenchPart part,
					ISelection selection) {
				OCLInterpreterView.this.selectionChanged(selection);
			}
		};
		selectionService = getSite().getWorkbenchWindow().getSelectionService();
		selectionService.addPostSelectionListener(selectionListener);

		// get current selection
		ISelection selection = selectionService.getSelection();
		if (selection == null) {
			selection = getActiveSelection();
		}
		selectionChanged(selection);

		((SashForm) page).setWeights(new int[]{1, 1, 5, 8});

		createActions();
	}

	/**
	 * Create view actions.
	 */
	public void createActions() {

		OCLEvaluateAction evaluate = new OCLEvaluateAction();
		evaluate.setView(this);

		HistoryBackAction historyBack = new HistoryBackAction();
		historyBack.setView(this);

		HistoryForwardAction historyForward = new HistoryForwardAction();
		historyForward.setView(this);

		ClearOutputAction clear = new ClearOutputAction(output);
		clear.setView(this);

		ExportOCLTextAction exportOCLText = new ExportOCLTextAction();
		exportOCLText.setView(this);

		ImportOCLTextAction importOCLText = new ImportOCLTextAction();
		importOCLText.setView(this);

		ExportResultAction exportResult = new ExportResultAction();
		exportResult.setView(this);

		IMenuManager menu = getViewSite().getActionBars().getMenuManager();
		menu.add(evaluate);
		menu.add(historyForward);
		menu.add(historyBack);
		menu.add(new Separator());
		menu.add(importOCLText);
		menu.add(exportOCLText);
		menu.add(new Separator());
		menu.add(exportResult);
		menu.add(clear);
		menu.add(new Separator());

		IMenuManager metamodelMenu = new MenuManager(
			OCLInterpreterMessages.console_metamodelMenu,
			"org.polarsys.capella.ocl.requester.metamodel"); //$NON-NLS-1$
		menu.add(metamodelMenu);
		metamodelAction
			.setToolTipText(OCLInterpreterMessages.console_metamodelTip);
		setMetamodelAction(metamodelMenu);

		IMenuManager levelMenu = new MenuManager(
			OCLInterpreterMessages.console_modelingLevel);
		menu.add(levelMenu);
		levelAction
			.setToolTipText(OCLInterpreterMessages.console_modelingLevelTip);
		ModelingLevelAction m2s = new ModelingLevelAction(ModelingLevel.M2Simplified);
		m2s.setView(this);
		m2s.setChecked(true);
		levelMenu.add(m2s);
		levelAction.addAction(m2s);
		ModelingLevelAction m2 = new ModelingLevelAction(ModelingLevel.M2);
		m2.setView(this);
		levelMenu.add(m2);
		levelAction.addAction(m2);
		// ModelingLevelAction m1 = new ModelingLevelAction(ModelingLevel.M1);
		// m1.setView(this);
		// levelMenu.add(m1);
		// levelAction.addAction(m1);

		//ActionContributionItem metamodelItem = new ActionContributionItem(
		//	metamodelAction);
		//metamodelItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);

		IToolBarManager toolbar = getViewSite().getActionBars().getToolBarManager();
		// toolbar.add(metamodelItem);
		toolbar.add(evaluate);
		toolbar.add(new Separator());
		toolbar.add(historyBack);
		toolbar.add(historyForward);
		toolbar.add(new Separator());
		toolbar.add(levelAction);
		toolbar.add(new Separator());
		toolbar.add(importOCLText);
		toolbar.add(exportOCLText);
		toolbar.add(new Separator());
		toolbar.add(exportResult);
		toolbar.add(clear);
	}

	/**
	 * @return the content provider for console.
	 */
	ITreeContentProvider getContentProvider() {
		if (contentProvider == null) {
			contentProvider = new VariableContentProvider(SiriusEditPlugin
				.getPlugin().getItemProvidersAdapterFactory());
		}
		return contentProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {
	}

	/**
	 * Evaluates an OCL expression using the OCL Interpreter's {@link OCLHelper}
	 * API.
	 * 
	 * @param expression
	 *            an OCL expression
	 * 
	 * @return <code>true</code> on successful evaluation; <code>false</code> if
	 *         the expression failed to parse or evaluate
	 */
	boolean evaluate(String expression) {
		boolean result = true;

		if (context == null) {
			result = false;
			error(OCLInterpreterMessages.console_noContext);
		} else {
			// create an OCL helper to do our parsing and evaluating
			if (oclFactory == null) {
				oclFactory = new EcoreOCLFactory(context);
			}
			ocl = oclFactory.createOCL(modelingLevel);
			OCLHelper<Object, ?, ?, ?> helper = ocl.createOCLHelper();

			try {
				// set our helper's context classifier to parse against it
				ConstraintKind kind = modelingLevel.setContext(helper, context,
					oclFactory);

				IDocument doc = getOutputDocument();
				Color outputDefault = colorManager
					.getColor(ColorManager.DEFAULT);
				Color outputResults = colorManager
					.getColor(ColorManager.OUTPUT_RESULTS);

				if (doc.getLength() > 0) {
					// separate previous output by a blank line
					append("", outputDefault, false); //$NON-NLS-1$
				}

				append(OCLInterpreterMessages.console_evaluating,
					outputDefault, true);
				append(expression, outputDefault, false);
				append(OCLInterpreterMessages.console_results, outputDefault,
					true);

				switch (modelingLevel) {
					case M2 :
					case M2Simplified :
						OCLExpression<Object> parsed = helper
							.createQuery(expression);

						Object r = ocl.evaluate(context, parsed);

						handleExpressionResult(r);

						// evaluate the query
						print(r, outputResults, false);
						break;
					case M1 :
						helper.createConstraint(kind, expression);

						// just report a successful parse
						print(OCLInterpreterMessages.console_parsed,
							outputResults, false);
						break;
				}

				// Clear error output
				errorOutput.setText(""); //$NON-NLS-1$

				// store the successfully parsed expression
				lastOCLExpression = expression;
			} catch (Exception e) {
				result = false;
				error((e.getLocalizedMessage() == null)
					? e.getClass().getName()
					: e.getLocalizedMessage());
			}
		}

		return result;
	}

	/**
	 * A key listener that listens for the Enter key to evaluate the OCL
	 * expression.
	 */
	private class InputKeyListener
			implements KeyListener {

		public void keyPressed(KeyEvent e) {
			switch (e.keyCode) {
				case SWT.CR :
					if (!input.isContentAssistActive()
						&& ((e.stateMask & (SWT.CTRL | SWT.SHIFT)) == 0)) {
						evaluate();
					}
					break;
				case SWT.PAGE_UP :
					if (!input.isContentAssistActive()
						&& ((e.stateMask & (SWT.CTRL | SWT.SHIFT)) == 0)) {
						historyBack();
					}
					break;
				case SWT.PAGE_DOWN :
					if (!input.isContentAssistActive()
						&& ((e.stateMask & (SWT.CTRL | SWT.SHIFT)) == 0)) {
						historyForward();
					}
					break;
			}
		}

		public void keyReleased(KeyEvent e) {
			switch (e.keyCode) {
				case SWT.CR :
					if ((e.stateMask & SWT.CTRL) == 0) {
						historyClean();
					}
					break;
				case ' ' :
					if ((e.stateMask & SWT.CTRL) == SWT.CTRL) {
						showPossibleCompletions();
					}
			}
		}

	}

	public void evaluate() {
		String text = document.get();
		evaluationSuccess = evaluate(text.trim());
	}

	public void historyBack() {
		// history
		if ((currentHistoryPointer == 0) && (history.size() > 0)) {
			if ((history.size() > 0) && (history.get(0).length() == 0)) {
				history.remove(0);
			}
			history.add(0, document.get().trim());
			currentHistoryPointer = 1;
			setTextFromHistory();
		} else if (currentHistoryPointer < (history.size() - 1)) {
			currentHistoryPointer++;
			setTextFromHistory();
		}
	}

	public void historyForward() {
		// history
		if (currentHistoryPointer > 0) {
			currentHistoryPointer--;
			setTextFromHistory();
		}
	}

	public void historyClean() {

		if (evaluationSuccess) {
			document.set(""); //$NON-NLS-1$
			// history
			if ((history.size() > 0) && (history.get(0).trim().length() == 0)) {
				history.remove(0);
			}
			if ((history.size() == 0)
				|| !history.get(0).equals(lastOCLExpression.trim())) {
				history.add(0, lastOCLExpression.trim());
			}
			currentHistoryPointer = 0;
		}
		evaluationSuccess = false;
	}

	public void showPossibleCompletions() {
		input.getContentAssistant().showPossibleCompletions();
	}

	protected void setTextFromHistory() {
		String newText = history.get(currentHistoryPointer);
		document.set(newText);
		input.setSelectedRange(newText.length(), 0);
	}

	/**
	 * Obtains the document in the output viewer.
	 * 
	 * @return the output document
	 */
	public IDocument getOutputDocument() {
		return output.getDocument();
	}

	/**
	 * Prints the specified <code>object</code> to the output viewer. The object
	 * is converted to a string using the best matching EMF label provider
	 * adapter if it is an {@link EObject}; otherwise, just use
	 * {@link String#valueOf(java.lang.Object)} on it. If the
	 * <code>object</code> is a collection or an array, then we print each
	 * element on a separate line.
	 * 
	 * @param object
	 *            the object or collection to print
	 * @param color
	 *            the color to print the <code>object</code> with
	 * @param bold
	 *            whether to display it in bold text
	 */
	private void print(Object object, Color color, boolean bold) {
		Collection<?> toPrint;

		if (object == null) {
			toPrint = Collections.EMPTY_SET;
		} else if (object instanceof Collection) {
			toPrint = (Collection<?>) object;
		} else if (object.getClass().isArray()) {
			toPrint = Arrays.asList((Object[]) object);
		} else {
			toPrint = Collections.singleton(object);
		}

		for (Object name : toPrint) {
			append(toString(name), color, bold);
		}

		scrollText();
	}

	/**
	 * Converts a single object to a string, according to the rules described
	 * for the {@link #print(Object, Color, boolean)} method.
	 * 
	 * @param object
	 *            the object to print (not a collection type)
	 * @return the string form of the <code>object</code>
	 * 
	 * @see #print(Object, Color, boolean)
	 */
	public String toString(Object object) {
		if ((ocl != null) && ocl.isInvalid(object)) {
			return "OclInvalid"; //$NON-NLS-1$
		} else if (object instanceof String) {
			return "'" + object + "'"; //$NON-NLS-1$//$NON-NLS-2$
		} else if (object instanceof Tuple) {
			return tupleTypeLabelProvider.getText(object);
		} else if (object instanceof EObject) {
			EObject eObject = (EObject) object;

			IItemLabelProvider labeler = (IItemLabelProvider) defaultAdapterFactory
				.adapt(eObject, IItemLabelProvider.class);

			if (labeler == null) {
				labeler = (IItemLabelProvider) reflectiveAdapterFactory.adapt(
					eObject, IItemLabelProvider.class);
			}

			if (labeler != null) {
				return labeler.getText(object);
			}
		}

		return String.valueOf(object);
	}

	/**
	 * Prints an error message to the output viewer, in red text.
	 * 
	 * @param message
	 *            the error message to print
	 */
	private void error(String message) {
		// Write error to the default error output.
		errorOutput.setText(message);

		// Write error to the console
		append(message, colorManager.getColor(ColorManager.OUTPUT_ERROR), false);
		scrollText();
	}

	/**
	 * Ensures that the last text printed to the output viewer is shown.
	 */
	private void scrollText() {
		output.revealRange(getOutputDocument().getLength(), 0);
	}

	/**
	 * Appends the specidied text to the output viewer.
	 * 
	 * @param text
	 *            the text to append
	 * @param color
	 *            the color to print the text with
	 * @param bold
	 *            whether to print the text bold
	 */
	private void append(String text, Color color, boolean bold) {

		IDocument doc = getOutputDocument();
		try {
			int offset = doc.getLength();
			int length = text.length();

			text = text + '\n';

			if (offset > 0) {
				doc.replace(offset, 0, text);
			} else {
				doc.set(text);
			}

			StyleRange style = new StyleRange();
			style.start = offset;
			style.length = length;
			style.foreground = color;

			if (bold) {
				style.fontStyle = SWT.BOLD;
			}

			output.getTextWidget().setStyleRange(style);
		} catch (BadLocationException e) {
			IStatus status = new Status(IStatus.ERROR,
				OCLRequesterPlugin.getPluginId(), 1,
				OCLInterpreterMessages.console_outputExc, e);

			OCLRequesterPlugin.getDefault().getLog().log(status);
		}
	}

	/**
	 * Utils method that returns the active selection.
	 * 
	 * @return the active selection.
	 */
	private ISelection getActiveSelection() {
		try {
			IWorkbenchSite site = getSite();
			if (site == null) {
				return null;
			}
			IWorkbenchWindow workbenchWindow = site.getWorkbenchWindow();
			if (workbenchWindow == null) {
				return null;
			}
			IWorkbenchPage activePage = workbenchWindow.getActivePage();
			if (activePage == null) {
				return null;
			}
			IEditorPart activeEditor = activePage.getActiveEditor();
			if (activeEditor == null) {
				return null;
			}
			IEditorSite editorSite = activeEditor.getEditorSite();
			if (editorSite == null) {
				return null;
			}
			ISelectionProvider selectionProvider = editorSite
				.getSelectionProvider();
			if (selectionProvider == null) {
				return null;
			}
			return selectionProvider.getSelection();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param sel
	 */
	private void selectionChanged(ISelection sel) {
		if (sel instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) sel;

			if (!ssel.isEmpty()) {
				Object selected = ssel.getFirstElement();

				context = CapellaAdapterHelper.resolveSemanticObject(selected);
				document.setOCLContext(context);

				if (!contextName.isDisposed()) {
					contextName.setText(OCLInterpreterView.this
						.toString(context));
				}

				levelAction.run();
				metamodelAction.run();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Integer handleExpressionResult(final Object result) {
		if (result != null) {
			final Collection<Object> input;
			if (result instanceof Collection) {
				input = (Collection) result;
			} else {
				input = new ArrayList<Object>(1);
				input.add(result);
			}
			this.treeViewer.setInput(input);
			return input.size();
		} else {
			this.treeViewer.setInput(null);
			return 0;
		}

	}

	/**
	 * Adds actions for the available target metamodels to the action bars.
	 * 
	 * @param metamodelMenu
	 *            the console's drop-down action bar menu
	 */
	private void setMetamodelAction(IMenuManager metamodelMenu) {
		EcoreMetamodelAction ecore = new EcoreMetamodelAction();
		ecore.setView(this);
		ecore.setChecked(true);
		ecore.run();
		ImageDescriptor img = getImage(EcoreFactory.eINSTANCE.getEPackage());
		if (img != null) {
			ecore.setImageDescriptor(img);
		}

		metamodelMenu.add(ecore);
		metamodelAction.addAction(ecore);
	}

	/**
	 * Queries whether a bundle is available.
	 * 
	 * @param bundle
	 *            a bundle
	 * @return whether it is active or resolved
	 */
	static boolean isAvailable(Bundle bundle) {
		return (bundle.getState() & BUNDLE_AVAILABLE) != 0;
	}

	/**
	 * Gets the editor image for the specified element.
	 * 
	 * @param element
	 *            a model element
	 * 
	 * @return the corresponding image
	 */
	private ImageDescriptor getImage(EObject element) {
		ImageDescriptor result = null;

		IItemLabelProvider provider = (IItemLabelProvider) new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE).adapt(element,
			IItemLabelProvider.class);
		if (provider != null) {
			Object image = provider.getImage(element);
			if (image != null) {
				result = ExtendedImageRegistry.INSTANCE
					.getImageDescriptor(image);
			}
		}

		return result;
	}

	/**
	 * The parent composite of the view
	 * 
	 * @return page
	 */
	public Composite getPage() {
		return page;
	}

	/**
	 * The last given OCL expression.
	 * 
	 * @return last given OCL expression.
	 */
	public String getLastOCLExpression() {
		return lastOCLExpression;
	}

	/**
	 * The selected modeling level.
	 * 
	 * @return the current modeling level.
	 */
	public ModelingLevel getModelingLevel() {
		return modelingLevel;
	}

	/**
	 * Set the current modeling level.
	 * 
	 * @param modelingLevel
	 */
	public void setModelingLevel(ModelingLevel modelingLevel) {
		this.modelingLevel = modelingLevel;
	}

	/**
	 * The OCL Document.
	 * 
	 * @return the current OCL document.
	 */
	public OCLDocument getDocument() {
		return document;
	}

	/**
	 * Assign OCL document.
	 * 
	 * @param document
	 *            : the current OCL document
	 */
	public void setDocument(OCLDocument document) {
		this.document = document;
	}

	/**
	 * @return the current OCL factory
	 */
	public IOCLFactory<Object> getOclFactory() {
		return oclFactory;
	}

	/**
	 * Set OCL factory.
	 * 
	 * @param oclFactory
	 *            : the current OCL factory
	 */
	public void setOclFactory(IOCLFactory<Object> oclFactory) {
		this.oclFactory = oclFactory;
	}

	/**
	 * Current context : self.
	 * 
	 * @return the current context
	 */
	public EObject getContext() {
		return context;
	}

	/**
	 * The tree item array.
	 * 
	 * @return the array of tree items.
	 */
	public TreeItem[] getTreeItem() {
		return treeViewer.getTree().getItems();
	}

	/**
	 * @return the treeViewer
	 */
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

}
