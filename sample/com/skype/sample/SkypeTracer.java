/*******************************************************************************
 * Copyright (c) 2006 Koji Hisano <hisano@gmail.com> - UBION Inc. Developer
 * Copyright (c) 2006 UBION Inc. <http://www.ubion.co.jp/> All rights reserved.
 * 
 * Copyright (c) 2006 Skype Technologies S.A. <http://www.skype.com/>
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: Koji Hisano - initial API and implementation
 ******************************************************************************/
package com.skype.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.skype.connector.Connector;
import com.skype.connector.ConnectorException;

public class SkypeTracer extends Shell {
    public static void main(final String args[]) throws Exception {
        final Display display = Display.getDefault();
        final SkypeTracer shell = new SkypeTracer(display, SWT.SHELL_TRIM);
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public SkypeTracer(Display display, int style) throws ConnectorException {
        super(display, style);
        createContents();
    }

    private void createContents() throws ConnectorException {
        setText("Skype Tracer");
        setSize(400, 300);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);

        final Text fromSkype = new Text(this, SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY | SWT.BORDER);
        fromSkype.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
        Connector.getInstance().setDebugOut(new PrintWriter(new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                final String appended = new String(cbuf, off, len);
                Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        fromSkype.append(appended);
                    }
                });
            }

            @Override
            public void flush() throws IOException {
                // Do nothing
            }

            @Override
            public void close() throws IOException {
                // Do nothing
            }
        }));
        Connector.getInstance().setDebug(true);

        final Text toSkype = new Text(this, SWT.BORDER);
        toSkype.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

        final Button send = new Button(this, SWT.NONE);
        send.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                final String command = toSkype.getText();
                new Thread() { // Use execute(String) without waiting
                    @Override
                    public void run() {
                        try {
                            Connector.getInstance().execute(command);
                        } catch (ConnectorException e) {
                            // Skip not Skype errors
                        }
                    }
                }.start();
            }
        });
        send.setText("&Send");
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}