/*******************************************************************************
 * Copyright (c) 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.mwe.internal.ui.debug.breakpoint.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.emf.mwe.ui.Messages;
import org.eclipse.emf.mwe.ui.debug.model.MWEBreakpoint;
import org.eclipse.emf.mwe.ui.debug.processing.PluginAdapter;
import org.eclipse.emf.mwe.ui.debug.processing.PluginExtensionManager;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * Toggle breakpoint action that can be used both at vertical ruler and editor context menu.
 * 
 */
public class ToggleBreakpointAction extends Action {

	private final TextEditor editor;

	private final BreakpointActionGroup group;

	public ToggleBreakpointAction(final TextEditor editor, final BreakpointActionGroup group) {
		this.editor = editor;
		this.group = group;
		setText(Messages.ToggleBreakpointAction_Title);
		setToolTipText(Messages.ToggleBreakpointAction_Description);
	}

	public void updateText() {
		IResource resource = editor.getEditorInput().getAdapter(IResource.class);

		PluginAdapter adapter = PluginExtensionManager.getDefault().getAdapterByResourceExtension(resource.getFileExtension());
		if (adapter == null) {
			return;
		}

		int line = group.getLastSelectedLine() + 1;
		int start = group.getLastSelectedOffset();
		int end = calculateEnd(line, start);
		
		if (group.isRulerSelected()) {
			setEnabled(true);
		} else {
			setEnabled(adapter.isToggleBpEnabled(resource, start, end, line));
		}
	}

	@Override
	public void run() {
		try {
			toggleBreakpoint();
		} catch (CoreException e) {
		}
	}

	protected void toggleBreakpoint() throws CoreException {
		IResource resource = editor.getEditorInput().getAdapter(IResource.class);

		// Hint: doc line numbers start at 0, but markers at 1, therefore + 1
		int line = group.getLastSelectedLine() + 1;
		int start = group.getLastSelectedOffset();

		PluginAdapter adapter = PluginExtensionManager.getDefault().getAdapterByResourceExtension(resource.getFileExtension());
		if (adapter == null) {
			return;
		}

		// check if a BP already exists on that line and remove it
		int end = calculateEnd(line, start); 
		IBreakpointManager breakpointManager = DebugPlugin.getDefault().getBreakpointManager();
		IBreakpoint[] breakpoints = breakpointManager.getBreakpoints(MWEBreakpoint.DEBUG_MODEL_ID);
		IBreakpoint bp = adapter.checkBreakpoints(breakpoints,resource,start, end, line);
		if(bp != null){
			bp.delete();
			return;
		}
		// else: register it
		bp = adapter.createBreakpoint(resource, start, end, line);
		if (bp == null) {
			return;
		}
		breakpointManager.addBreakpoint(bp);
		
	}
	
	private int calculateEnd(int line, int start) {
        int end;
        try {
            end = group.isRulerSelected() ? group.getOffsetAtLine(line) : start;
        } catch (IllegalArgumentException e) {
            end = start;
        }
        return end;
    }
	
}
