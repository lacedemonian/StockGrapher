/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.ui.tabbedui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import org.jfree.ui.tabbedui.AbstractTabbedUI;

public class TabbedDialog
extends JDialog {
    private AbstractTabbedUI tabbedUI;

    public TabbedDialog() {
    }

    public TabbedDialog(Dialog owner) {
        super(owner);
    }

    public TabbedDialog(Dialog owner, boolean modal) {
        super(owner, modal);
    }

    public TabbedDialog(Dialog owner, String title) {
        super(owner, title);
    }

    public TabbedDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    public TabbedDialog(Frame owner) {
        super(owner);
    }

    public TabbedDialog(Frame owner, boolean modal) {
        super(owner, modal);
    }

    public TabbedDialog(Frame owner, String title) {
        super(owner, title);
    }

    public TabbedDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    protected final AbstractTabbedUI getTabbedUI() {
        return this.tabbedUI;
    }

    public void init(AbstractTabbedUI tabbedUI) {
        this.tabbedUI = tabbedUI;
        this.tabbedUI.addPropertyChangeListener("jMenuBar", new MenuBarChangeListener());
        this.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent e) {
                TabbedDialog.this.getTabbedUI().getCloseAction().actionPerformed(new ActionEvent(this, 1001, null, 0));
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add((Component)tabbedUI, "Center");
        this.setContentPane(panel);
        this.setJMenuBar(tabbedUI.getJMenuBar());
    }

    private class MenuBarChangeListener
    implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("jMenuBar")) {
                TabbedDialog.this.setJMenuBar(TabbedDialog.this.getTabbedUI().getJMenuBar());
            }
        }
    }

}

