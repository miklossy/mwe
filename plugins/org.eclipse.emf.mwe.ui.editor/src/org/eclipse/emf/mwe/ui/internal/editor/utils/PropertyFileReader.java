/*******************************************************************************
 * Copyright (c) 2005, 2009 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.emf.mwe.ui.internal.editor.utils;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.mwe.ui.internal.editor.elements.AbstractWorkflowElement;
import org.eclipse.emf.mwe.ui.internal.editor.elements.IPropertyContainer;
import org.eclipse.emf.mwe.ui.internal.editor.elements.IWorkflowAttribute;
import org.eclipse.emf.mwe.ui.internal.editor.elements.Property;
import org.eclipse.emf.mwe.ui.internal.editor.factories.WorkflowSyntaxFactory;

/**
 * @author Patrick Schoenbach - Initial API and implementation
 * @version $Revision: 1.1 $
 */

public final class PropertyFileReader {

	private static final String PROPERTY_REGEX = "^(.*?)\\s*=\\s*(.*)$";

	/**
	 * Don't allow instantiation.
	 */
	private PropertyFileReader() {
		throw new UnsupportedOperationException();
	}

	public static IPropertyContainer parse(IProject project, final AbstractWorkflowElement element)
			throws FileNotFoundException {
		if (project == null || element == null || !element.hasAttribute(IWorkflowAttribute.FILE_ATTRIBUTE))
			throw new IllegalArgumentException();

		IPropertyContainer container = WorkflowSyntaxFactory.getInstance().newPropertyContainer();
		final IWorkflowAttribute attribute = element.getAttribute(IWorkflowAttribute.FILE_ATTRIBUTE);
		final String content = TypeUtils.getFileContent(project, attribute);
		if (content == null)
			throw new FileNotFoundException("File '" + attribute.getValue() + "' not found");

		final Pattern p = Pattern.compile(PROPERTY_REGEX, Pattern.MULTILINE);
		final Matcher m = p.matcher(content);
		while (m.find()) {
			final String propertyName = m.group(1);
			String propertyValue = m.group(2);
			Property prop = new Property(propertyName);
			prop.setValue(propertyValue);
			container.addProperty(prop);
		}
		return container;
	}

}