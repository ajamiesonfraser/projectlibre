/*
 * ProjectLibre modern theme.
 *
 * This file is part of the ProjectLibre UI/UX refresh. It is distributed under
 * the same Common Public Attribution License (CPAL) 1.0 as the rest of the
 * openproj_ui module. See openproj_build/license.
 */
package com.projity.pm.graphic.laf;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;

/**
 * Central place for the "design tokens" of the modern ProjectLibre look:
 * accent colour, grid colours, row heights and corner radii.
 *
 * The values are inspired by Microsoft Project's desktop Gantt view (light,
 * low-contrast grid; soft blue task bars; strong selection highlight) and by
 * current web planners (generous row height, rounded controls).
 *
 * Everything in here is applied through UIManager so that individual
 * components do not need to know about the theme. FlatLaf specific keys are
 * harmless when another look and feel is active.
 */
public final class ModernTheme {

	private ModernTheme() {
	}

	private static boolean active = false;

	/** @return true when the modern (FlatLaf based) theme was installed at startup */
	public static boolean isActive() {
		return active;
	}

	/** Foreground for text on selected rows: dark on light blue (modern), white on dark gray (classic). */
	public static Color selectionForeground() {
		return active ? SELECTION_FOREGROUND : Color.WHITE;
	}

	/** Border colour of the focused cell. */
	public static Color focusBorder() {
		return active ? ACCENT : Color.BLACK;
	}

	/** Accent colour used for selection, focus rings and default buttons. */
	public static final Color ACCENT = new Color(0x2F6DB5);
	/** Light grid lines in sheets, similar to MS Project's default gridlines. */
	public static final Color GRID = new Color(0xE3E6EA);
	/** Background of selected rows in sheets. */
	public static final Color SELECTION_BACKGROUND = new Color(0xCFE2F7);
	public static final Color SELECTION_FOREGROUND = new Color(0x1B1B1B);
	/** Header background of sheets and the timescale. */
	public static final Color HEADER_BACKGROUND = new Color(0xF3F4F6);
	public static final Color HEADER_FOREGROUND = new Color(0x3A3F45);

	/**
	 * Called before the FlatLaf class is installed. Sets FlatLaf "extra
	 * defaults" (variables such as @accentColor) through reflection so that the
	 * code compiles and runs even if FlatLaf is not on the classpath.
	 */
	static void installDefaults(boolean dark) {
		Map<String, String> extra = new HashMap<String, String>();
		extra.put("@accentColor", toHex(ACCENT)); //$NON-NLS-1$
		extra.put("Component.arc", "6"); //$NON-NLS-1$ //$NON-NLS-2$
		extra.put("Button.arc", "6"); //$NON-NLS-1$ //$NON-NLS-2$
		extra.put("TextComponent.arc", "4"); //$NON-NLS-1$ //$NON-NLS-2$
		extra.put("ScrollBar.showButtons", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		extra.put("ScrollBar.thumbArc", "999"); //$NON-NLS-1$ //$NON-NLS-2$
		extra.put("ScrollBar.thumbInsets", "2,2,2,2"); //$NON-NLS-1$ //$NON-NLS-2$
		extra.put("ScrollBar.width", "12"); //$NON-NLS-1$ //$NON-NLS-2$
		extra.put("TabbedPane.showTabSeparators", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		extra.put("SplitPane.dividerSize", "6"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			Class<?> flatLaf = Class.forName("com.formdev.flatlaf.FlatLaf"); //$NON-NLS-1$
			Method m = flatLaf.getMethod("setGlobalExtraDefaults", Map.class); //$NON-NLS-1$
			m.invoke(null, extra);
		} catch (Throwable t) {
			// FlatLaf older than 1.4 or not present: ignore, overrides below still apply
		}
	}

	/** Called after the look and feel is installed. */
	static void installOverrides() {
		active = true;
		// Sheets (task / resource tables)
		UIManager.put("Table.showHorizontalLines", Boolean.TRUE); //$NON-NLS-1$
		UIManager.put("Table.showVerticalLines", Boolean.TRUE); //$NON-NLS-1$
		UIManager.put("Table.gridColor", new ColorUIResource(GRID)); //$NON-NLS-1$
		UIManager.put("Table.selectionBackground", new ColorUIResource(SELECTION_BACKGROUND)); //$NON-NLS-1$
		UIManager.put("Table.selectionForeground", new ColorUIResource(SELECTION_FOREGROUND)); //$NON-NLS-1$
		UIManager.put("Table.selectionInactiveBackground", new ColorUIResource(SELECTION_BACKGROUND)); //$NON-NLS-1$
		UIManager.put("Table.selectionInactiveForeground", new ColorUIResource(SELECTION_FOREGROUND)); //$NON-NLS-1$
		UIManager.put("Table.intercellSpacing", new java.awt.Dimension(1, 1)); //$NON-NLS-1$
		UIManager.put("TableHeader.background", new ColorUIResource(HEADER_BACKGROUND)); //$NON-NLS-1$
		UIManager.put("TableHeader.foreground", new ColorUIResource(HEADER_FOREGROUND)); //$NON-NLS-1$
		UIManager.put("TableHeader.separatorColor", new ColorUIResource(GRID)); //$NON-NLS-1$
		UIManager.put("TableHeader.bottomSeparatorColor", new ColorUIResource(new Color(0xC9CED6))); //$NON-NLS-1$
		UIManager.put("TableHeader.cellMargins", new InsetsUIResource(3, 6, 3, 6)); //$NON-NLS-1$
		// Legacy code asks for this key (it does not exist in FlatLaf)
		if (UIManager.getColor("TableHeader.focusCellForeground") == null) //$NON-NLS-1$
			UIManager.put("TableHeader.focusCellForeground", new ColorUIResource(HEADER_BACKGROUND)); //$NON-NLS-1$
		UIManager.put("ScrollPane.border", javax.swing.BorderFactory.createEmptyBorder()); //$NON-NLS-1$
		UIManager.put("Button.margin", new InsetsUIResource(3, 10, 3, 10)); //$NON-NLS-1$
	}

	private static String toHex(Color c) {
		return String.format("#%02x%02x%02x", Integer.valueOf(c.getRed()), Integer.valueOf(c.getGreen()), Integer.valueOf(c.getBlue())); //$NON-NLS-1$
	}
}
