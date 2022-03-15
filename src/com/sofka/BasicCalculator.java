/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sofka;

/*
 * Basic Calculator
 * This code is based in sample KeyEventDemo code, found in Java documentation
 *  See https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/events/KeyEventDemoProject/src/events/KeyEventDemo.java
 *
 * This exercise required to implement the solution as a console app, but the program was made using the Java swing library instead
 * due to restriction related to blocking reading input in java console, that doesn't allow it to read each single key-pressed input by user,
 * but wait until "ENTER" key to read the input.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

public class BasicCalculator extends JFrame
        implements KeyListener,
        ActionListener {
    JTextArea displayArea;
    JTextField typingArea;
    static final String newline = System.getProperty("line.separator");

    float op1 = 0.0f;
    float op2 = 0.0f;
    String buffer = "";
    char previous = '+';

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        BasicCalculator frame = new BasicCalculator("KeyEventDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        frame.addComponentsToPane();


        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void addComponentsToPane() {

        JButton button = new JButton("Borrar");
        button.addActionListener(this);

        typingArea = new JTextField(20);
        typingArea.addKeyListener(this);

        //Uncomment this if you wish to turn off focus
        //traversal.  The focus subsystem consumes
        //focus traversal keys, such as Tab and Shift Tab.
        //If you uncomment the following line of code, this
        //disables focus traversal and the Tab events will
        //become available to the key event listener.
        //typingArea.setFocusTraversalKeysEnabled(false);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(375, 125));

        getContentPane().add(typingArea, BorderLayout.PAGE_START);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(button, BorderLayout.PAGE_END);
    }

    public BasicCalculator(String name) {
        super(name);
    }


    /**
     * Handle the key typed event from the text field.
     */
    public void keyTyped(KeyEvent e) {
        char code = evaluateKey(e, "");
        evaluateCode(code);
    }

    private void evaluateCode(char c) {

        System.out.println("valor retornado es: " + c);
        if (c >= 48 && c <= 57 || c >= 96 && c <= 105) {
            buffer += Character.toString(c);
        } else/* if(keyCode >= 107 && keyCode <= 116)*/ {
            op2 = Float.parseFloat(buffer);
            switch (previous) {
                case '+':
                    op1 += op2;
                    break;
                case '-':
                    op1 -= op2;
                    break;
                case '/':
                    if(op2 == 0) {
                        // Clear values in op1 and op2:
                        op1 = 0.0f;
                        op2 = 0.0f;
                        previous = '+';
                        typingArea.setEditable(false);
                        displayArea.setText("Error, presione Borrar para continuar");
                        typingArea.setText("");
                    }
                    break;
                case '*':
                    op1 *= op2;
                    break;
                case '%':
                    op1 %= op2;
                    break;
                default:
                    break;
            }
            previous = c;
            buffer = "";
            op2 = op1;
            // Cleaning displayArea previous any new calculation.
            displayArea.setText("");
            displayArea.append("" + op2);
            displayArea.setCaretPosition(displayArea.getDocument().getLength());
        }
    }

    /**
     * Handle the key pressed event from the text field.
     */
    public void keyPressed(KeyEvent e) {
        evaluateKey(e, "");
    }

    /**
     * Handle the key released event from the text field.
     */
    public void keyReleased(KeyEvent e) {
        evaluateKey(e, "");
    }

    /**
     * Handle the button click.
     */
    public void actionPerformed(ActionEvent e) {
        //Clear the text components.
        displayArea.setText("");
        typingArea.setText("");
        typingArea.setEditable(true);

        // Clear values in op1 and op2:
        op1 = 0.0f;
        op2 = 0.0f;

        //Return the focus to the typing area.
        typingArea.requestFocusInWindow();
    }

    /*
     * We have to jump through some hoops to avoid
     * trying to print non-printing characters
     * such as Shift.  (Not only do they not print,
     * but if you put them in a String, the characters
     * afterward won't show up in the text area.)
     */
    private char evaluateKey(KeyEvent e, String keyStatus) {

        //You should only rely on the key char if the event
        //is a key typed event.

        System.out.println("in method displayInfo");
        char c;
        int id = e.getID();
        int keyCode = e.getKeyCode();
        String keyString = "";
        if (id != KeyEvent.KEY_TYPED) {
            keyCode = e.getKeyCode();
            System.out.println(keyCode);
        } else {
            c = e.getKeyChar();
            System.out.println(c);
            return c;
        }
        return 0;
    }

}