/*******************************************************************************
 * Copyright (c) 2005, 2006 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.mwe.internal.core.ast.parser;

public class ParseException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1943730836608506785L;
	private final Location location;

    public ParseException(final String string, final Location location) {
        super(string);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
