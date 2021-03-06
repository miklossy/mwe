/*******************************************************************************
 * Copyright (c) 2005 - 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.mwe.internal.core.debug.communication.packages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The packet to communicate a process or direct command to the runtime process.
 */
public class CommandPackage extends AbstractPackage {

	public int command;

	// -------------------------------------------------------------------------

	public CommandPackage(final int command) {
		this.command = command;
	}

	// -------------------------------------------------------------------------

	@Override
	public void readContent(final DataInputStream in) throws IOException {
		command = in.readInt();

	}

	@Override
	public void writeContent(final DataOutputStream out) throws IOException {
		out.writeInt(command);
	}

	@Override
	public String toString() {
		return super.toString() + " type=" + command;
	}
}
