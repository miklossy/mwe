/*
 * Copyright (c) 2008 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    committers of openArchitectureWare - initial API and implementation
 */

package org.eclipse.emf.mwe.ui.editor.parser;

import org.eclipse.emf.ecore.EObject;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Patrick Schoenbach
 * @version $Revision: 1.1 $
 */
public class WorkflowContentHandler extends DefaultHandler {

    protected EObject rootElement;

    /**
     * Returns the value of field <code>rootElement</code>.
     * 
     * @return value of <code>rootElement</code>.
     */
    public EObject getRootElement() {
        return rootElement;
    }

}