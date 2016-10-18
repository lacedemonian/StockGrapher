/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.ui.about;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import org.jfree.base.Library;
import org.jfree.ui.about.AboutPanel;
import org.jfree.ui.about.ContributorsPanel;
import org.jfree.ui.about.LibraryPanel;
import org.jfree.ui.about.ProjectInfo;
import org.jfree.ui.about.SystemPropertiesPanel;

public class AboutFrame
extends JFrame {
    public static final Dimension PREFERRED_SIZE = new Dimension(560, 360);
    public static final Border STANDARD_BORDER = BorderFactory.createEmptyBorder(5, 5, 5, 5);
    private ResourceBundle resources;
    private String application;
    private String version;
    private String copyright;
    private String info;
    private Image logo;
    private List contributors;
    private String licence;
    private List libraries;

    public AboutFrame(String title, ProjectInfo project) {
        this(title, project.getName(), "Version " + project.getVersion(), project.getInfo(), project.getLogo(), project.getCopyright(), project.getLicenceText(), project.getContributors(), Arrays.asList(project.getLibraries()));
    }

    public AboutFrame(String title, String application, String version, String info, Image logo, String copyright, String licence, List contributors, List libraries) {
        super(title);
        this.application = application;
        this.version = version;
        this.copyright = copyright;
        this.info = info;
        this.logo = logo;
        this.contributors = contributors;
        this.licence = licence;
        this.libraries = libraries;
        String baseName = "org.jfree.ui.about.resources.AboutResources";
        this.resources = ResourceBundle.getBundle("org.jfree.ui.about.resources.AboutResources");
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(STANDARD_BORDER);
        JTabbedPane tabs = this.createTabs();
        content.add(tabs);
        this.setContentPane(content);
        this.pack();
    }

    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }

    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();
        JPanel aboutPanel = this.createAboutPanel();
        aboutPanel.setBorder(STANDARD_BORDER);
        String aboutTab = this.resources.getString("about-frame.tab.about");
        tabs.add(aboutTab, aboutPanel);
        SystemPropertiesPanel systemPanel = new SystemPropertiesPanel();
        systemPanel.setBorder(STANDARD_BORDER);
        String systemTab = this.resources.getString("about-frame.tab.system");
        tabs.add(systemTab, systemPanel);
        return tabs;
    }

    private JPanel createAboutPanel() {
        JPanel about = new JPanel(new BorderLayout());
        AboutPanel details = new AboutPanel(this.application, this.version, this.copyright, this.info, this.logo);
        boolean includetabs = false;
        JTabbedPane tabs = new JTabbedPane();
        if (this.contributors != null) {
            ContributorsPanel contributorsPanel = new ContributorsPanel(this.contributors);
            contributorsPanel.setBorder(STANDARD_BORDER);
            String contributorsTab = this.resources.getString("about-frame.tab.contributors");
            tabs.add(contributorsTab, contributorsPanel);
            includetabs = true;
        }
        if (this.licence != null) {
            JPanel licencePanel = this.createLicencePanel();
            licencePanel.setBorder(STANDARD_BORDER);
            String licenceTab = this.resources.getString("about-frame.tab.licence");
            tabs.add(licenceTab, licencePanel);
            includetabs = true;
        }
        if (this.libraries != null) {
            LibraryPanel librariesPanel = new LibraryPanel(this.libraries);
            librariesPanel.setBorder(STANDARD_BORDER);
            String librariesTab = this.resources.getString("about-frame.tab.libraries");
            tabs.add(librariesTab, librariesPanel);
            includetabs = true;
        }
        about.add((Component)details, "North");
        if (includetabs) {
            about.add(tabs);
        }
        return about;
    }

    private JPanel createLicencePanel() {
        JPanel licencePanel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea(this.licence);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setCaretPosition(0);
        area.setEditable(false);
        licencePanel.add(new JScrollPane(area));
        return licencePanel;
    }
}

