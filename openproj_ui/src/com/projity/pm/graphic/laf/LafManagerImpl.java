/*
The contents of this file are subject to the Common Public Attribution License
Version 1.0 (the "License"); you may not use this file except in compliance with
the License. You may obtain a copy of the License at
http://www.projity.com/license . The License is based on the Mozilla Public
License Version 1.1 but Sections 14 and 15 have been added to cover use of
software over a computer network and provide for limited attribution for the
Original Developer. In addition, Exhibit A has been modified to be consistent
with Exhibit B.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the
specific language governing rights and limitations under the License. The
Original Code is OpenProj. The Original Developer is the Initial Developer and
is Projity, Inc. All portions of the code written by Projity are Copyright (c)
2006, 2007. All Rights Reserved. Contributors Projity, Inc.

Alternatively, the contents of this file may be used under the terms of the
Projity End-User License Agreeement (the Projity License), in which case the
provisions of the Projity License are applicable instead of those above. If you
wish to allow use of your version of this file only under the terms of the
Projity License and not to allow others to use your version of this file under
the CPAL, indicate your decision by deleting the provisions above and replace
them with the notice and other provisions required by the Projity  License. If
you do not delete the provisions above, a recipient may use your version of this
file under either the CPAL or the Projity License.

[NOTE: The text of this license may differ slightly from the text of the notices
in Exhibits A and B of the license at http://www.projity.com/license. You should
use the latest text at http://www.projity.com/license for your modifications.
You may not remove this license text from the source files.]

Attribution Information: Attribution Copyright Notice: Copyright � 2006, 2007
Projity, Inc. Attribution Phrase (not exceeding 10 words): Powered by OpenProj,
an open source solution from Projity. Attribution URL: http://www.projity.com
Graphic Image as provided in the Covered Code as file:  openproj_logo.png with
alternatives listed on http://www.projity.com/logo

Display of Attribution Information is required in Larger Works which are defined
in the CPAL as a work which combines Covered Code or portions thereof with code
not governed by the terms of the CPAL. However, in addition to the other notice
obligations, all copies of the Covered Code in Executable and Source Code form
distributed must, as a form of attribution of the original author, include on
each user interface screen the "OpenProj" logo visible to all users.  The
OpenProj logo should be located horizontally aligned with the menu bar and left
justified on the top left of the screen adjacent to the File menu.  The logo
must be at least 100 x 25 pixels.  When users click on the "OpenProj" logo it
must direct them back to http://www.projity.com.
*/
package com.projity.pm.graphic.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.projity.graphic.configuration.shape.Colors;
import com.projity.pm.graphic.frames.GraphicManager;
import com.projity.util.Environment;

public class LafManagerImpl implements LafManager {
    protected static LookAndFeel plaf = null; // for substance
    protected static GraphicManager graphicManager;
	private static Boolean lafOK = null;
    public LafManagerImpl(GraphicManager graphicManager){
    	this.graphicManager=graphicManager;
    }

	/* (non-Javadoc)
	 * @see com.projity.pm.graphic.laf.LafManager1#clean()
	 */
	public void clean(){
		if (plaf!=null){
			plaf.uninitialize();
			plaf = null;
		}
	}

    public static boolean isLafOk() {
    	if (lafOK == null) {
	    	UIDefaults d = UIManager.getDefaults();
	    	Object cl = d.get("ClassLoader");
	    	JPanel target = new JPanel();
	    	ClassLoader uiClassLoader =
	    		(cl != null) ? (ClassLoader)cl : target.getClass().getClassLoader();
	    	Class uiClass = d.getUIClass(target.getUIClassID(), uiClassLoader);
	    	lafOK = (uiClass != null);
    	}
    	return lafOK;
    }


    /* (non-Javadoc)
	 * @see com.projity.pm.graphic.laf.LafManager1#getPlaf()
	 */
    public LookAndFeel getPlaf() {
    	if (plaf == null) {
    		String choice = getLafChoice();
    		boolean ok = false;
    		if (!LAF_CLASSIC.equals(choice) && !LAF_SYSTEM.equals(choice)) {
    			ok = installModernLaf(choice);
    		}
    		if (!ok) {
    			try {
    				int os = Environment.getOs();
    				if (LAF_CLASSIC.equals(choice) || os == Environment.LINUX) //$NON-NLS-1$
    					UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); //$NON-NLS-1$
    				else
    					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		plaf = UIManager.getLookAndFeel();
    		if (graphicManager != null && graphicManager.getContainer() != null)
    			SwingUtilities.updateComponentTreeUI(graphicManager.getContainer());
    	}
    	return plaf;
    }

    /** System property / preference key used to pick the look and feel. */
    public static final String LAF_PROPERTY = "projectlibre.laf"; //$NON-NLS-1$
    public static final String LAF_LIGHT = "light"; //$NON-NLS-1$
    public static final String LAF_DARK = "dark"; //$NON-NLS-1$
    public static final String LAF_CLASSIC = "classic"; //$NON-NLS-1$
    public static final String LAF_SYSTEM = "system"; //$NON-NLS-1$

    /**
     * Resolves which look and feel to use. Order: -Dprojectlibre.laf=..., then the
     * saved user preference, then the default (modern light theme).
     */
    public static String getLafChoice() {
    	String choice = System.getProperty(LAF_PROPERTY);
    	if (choice == null || choice.length() == 0) {
    		try {
    			choice = Preferences.userNodeForPackage(LafManagerImpl.class).get(LAF_PROPERTY, LAF_LIGHT);
    		} catch (Exception e) {
    			choice = LAF_LIGHT;
    		}
    	}
    	return choice.trim().toLowerCase();
    }

    /** Persists the look and feel choice; it takes effect on next start. */
    public static void setLafChoice(String choice) {
    	try {
    		Preferences.userNodeForPackage(LafManagerImpl.class).put(LAF_PROPERTY, choice);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Installs the FlatLaf based theme. FlatLaf is loaded by class name so the
     * application still starts (with the classic theme) if the jar is missing.
     */
    protected boolean installModernLaf(String choice) {
    	String className = LAF_DARK.equals(choice)
    		? "com.formdev.flatlaf.FlatDarkLaf" //$NON-NLS-1$
    		: "com.formdev.flatlaf.FlatLightLaf"; //$NON-NLS-1$
    	try {
    		ModernTheme.installDefaults(LAF_DARK.equals(choice));
    		UIManager.setLookAndFeel(className);
    		ModernTheme.installOverrides();
    		return true;
    	} catch (Throwable t) {
    		System.err.println("Could not install modern look and feel " + className + ": " + t); //$NON-NLS-1$ //$NON-NLS-2$
    		return false;
    	}
    }

    /* (non-Javadoc)
	 * @see com.projity.pm.graphic.laf.LafManager1#initLookAndFeel()
	 */
    public void initLookAndFeel() {
		if (plaf == null)
			getPlaf();
    }


	/* (non-Javadoc)
	 * @see com.projity.pm.graphic.laf.LafManager1#setColorTheme(java.lang.String)
	 */
	public void setColorTheme(String viewName ) {
	}

	/* (non-Javadoc)
	 * @see com.projity.pm.graphic.laf.LafManager1#changePalette()
	 */
	public void changePalette(){
	}

	/* (non-Javadoc)
	 * @see com.projity.pm.graphic.laf.LafManager1#isChangePaletteAllowed(javax.swing.LookAndFeel)
	 */
	public boolean isChangePaletteAllowed(LookAndFeel lookAndFeel){
		return false;

	}

	/* (non-Javadoc)
	 * @see com.projity.pm.graphic.laf.LafManager1#paintComponent(java.awt.Graphics, java.awt.Component, boolean)
	 */
	public void paintComponent(Graphics g,Component component,boolean selected){
		if (Environment.isMac()){
			g.setColor(GraphicManager.getInstance().getLafManager().getUnselectedBackgroundColor());
			Rectangle bounds = component.getBounds();
			g.fillRect(0, 0,bounds.width,bounds.height);
		}
	}

	/* (non-Javadoc)
	 * @see com.projity.pm.graphic.laf.LafManager1#setUI(javax.swing.JTabbedPane)
	 */
	public void setUI(JTabbedPane component){

	}

	/* (non-Javadoc)
	 * @see com.projity.pm.graphic.laf.LafManager1#setColorScheme(javax.swing.JComponent)
	 */
	public void setColorScheme(JComponent component){
	}

	/* (non-Javadoc)
	 * @see com.projity.pm.graphic.laf.LafManager1#paintTimeScale(java.awt.Graphics2D, int, int, int, int, java.awt.Shape[])
	 */
	public void paintTimeScale(Graphics2D g2,int x,int y,int w,int h,Shape[] shapes){

	}


	public Color getSelectedBackgroundColor() {
		if (ModernTheme.isActive()) return ModernTheme.SELECTION_BACKGROUND;
		return Environment.isMac()?Colors.NOT_TOO_DARK_GRAY:Color.DARK_GRAY;
	}
	public Color getUnselectedBackgroundColor() {
		LookAndFeel laf = UIManager.getLookAndFeel();
		if (Environment.isMac())
			return Environment.isMac()?Colors.VERY_LIGHT_GRAY:laf.getDefaults().getColor("TableHeader.background");//table.getTableHeader ().getBackground()
		Color c = laf.getDefaults().getColor("TableHeader.focusCellForeground"); //$NON-NLS-1$
		if (c == null) c = laf.getDefaults().getColor("TableHeader.background"); //$NON-NLS-1$
		return c == null ? ModernTheme.HEADER_BACKGROUND : c;
	}

	public void dumpUIValues() {
		String v[] = new String[] {
			"Label.background"
			,"Table.focusCellBackground"
			,"Menu.selectionBackground"
			,"Table.focusCellBackground"
			,"TabbedPane.darkShadow"
			,"Table.focusCellForeground"
			,"Table.selectionBackground"
			,"TableHeader.background"
			,"TextField.selectionBackground"
		};
		for (int i = 0; i < v.length; i++)
			System.out.println(v[i] + "=" + UIManager.get(v[i]));

	}

	public static void main(String [] x) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("windows laf" + isWindowsLAF());
		System.out.println("LAF : " + UIManager.getLookAndFeel().getClass().getName());
		outputSwingDefs();
	}
	public static void outputSwingDefs() {
		String lineSep = System.getProperty("line.separator");
		javax.swing.UIDefaults uid = javax.swing.UIManager.getDefaults();
		java.util.Enumeration uidKeys = uid.keys();

		while (uidKeys.hasMoreElements()) {
			Object aKey = uidKeys.nextElement();
			Object aValue = uid.get(aKey);
			String str = "KEY: " + aKey + ", VALUE: " + aValue + lineSep;
			System.out.println(str);
		}
	}

	public static boolean isWindowsLAF() {
		return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel".equals(UIManager.getLookAndFeel().getClass().getName());
	}
	public boolean isToolbarOpaque() {
		return Environment.isNewLaf() || isWindowsLAF();
	}

}
