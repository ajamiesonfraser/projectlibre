/*
 * ProjectLibre macOS integration for Java 9+.
 *
 * This file is part of the ProjectLibre UI/UX refresh. It is distributed under
 * the same Common Public Attribution License (CPAL) 1.0 as the rest of the
 * openproj_ui module. See openproj_build/license.
 */
package com.projity.pm.graphic.frames;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * Hooks the macOS application menu (About, Quit) and Finder "open with" into
 * ProjectLibre using the standard java.awt.Desktop API (Java 9+).
 *
 * The old OSXAdapter relies on com.apple.eawt, which is no longer accessible
 * since Java 9 (and fails outright on current JDKs). Everything here is done by
 * reflection so the code still compiles for -source 1.8 and simply reports
 * "not installed" on Java 8, where the caller falls back to OSXAdapter.
 */
public final class MacIntegration {

	private MacIntegration() {
	}

	/** @return true if the Java 9+ handlers were installed */
	public static boolean install(final GraphicManager graphicManager, final boolean handleFiles) {
		try {
			Class<?> desktopClass = Class.forName("java.awt.Desktop"); //$NON-NLS-1$
			Class<?> actionClass = Class.forName("java.awt.Desktop$Action"); //$NON-NLS-1$
			Object desktop = desktopClass.getMethod("getDesktop").invoke(null); //$NON-NLS-1$
			Method isSupported = desktopClass.getMethod("isSupported", actionClass); //$NON-NLS-1$
			boolean installed = false;

			if (supported(isSupported, desktop, actionClass, "APP_ABOUT")) { //$NON-NLS-1$
				installed |= setHandler(desktopClass, desktop, "setAboutHandler", "java.awt.desktop.AboutHandler", //$NON-NLS-1$ //$NON-NLS-2$
					new InvocationHandler() {
						public Object invoke(Object proxy, Method method, Object[] args) {
							if ("handleAbout".equals(method.getName())) { //$NON-NLS-1$
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										graphicManager.showAboutDialog();
									}
								});
							}
							return objectMethod(proxy, method, args);
						}
					});
			}

			if (supported(isSupported, desktop, actionClass, "APP_QUIT_HANDLER")) { //$NON-NLS-1$
				installed |= setHandler(desktopClass, desktop, "setQuitHandler", "java.awt.desktop.QuitHandler", //$NON-NLS-1$ //$NON-NLS-2$
					new InvocationHandler() {
						public Object invoke(Object proxy, Method method, Object[] args) {
							if ("handleQuitRequestWith".equals(method.getName())) { //$NON-NLS-1$
								final Object response = args[1];
								// quitApplication() blocks while projects are closed (and may ask
								// to save), so never run it on the event dispatch thread.
								new Thread("macQuit") { //$NON-NLS-1$
									public void run() {
										boolean quit = false;
										try {
											quit = graphicManager.quitApplication();
										} catch (Exception e) {
											e.printStackTrace();
										}
										try {
											response.getClass().getMethod(quit ? "performQuit" : "cancelQuit").invoke(response); //$NON-NLS-1$ //$NON-NLS-2$
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}.start();
								return null;
							}
							return objectMethod(proxy, method, args);
						}
					});
			}

			if (handleFiles && supported(isSupported, desktop, actionClass, "APP_OPEN_FILE")) { //$NON-NLS-1$
				installed |= setHandler(desktopClass, desktop, "setOpenFileHandler", "java.awt.desktop.OpenFilesHandler", //$NON-NLS-1$ //$NON-NLS-2$
					new InvocationHandler() {
						public Object invoke(Object proxy, Method method, Object[] args) {
							if ("openFiles".equals(method.getName())) { //$NON-NLS-1$
								try {
									List<?> files = (List<?>) args[0].getClass().getMethod("getFiles").invoke(args[0]); //$NON-NLS-1$
									for (Object f : files) {
										graphicManager.openFile(((File) f).getAbsolutePath());
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								return null;
							}
							return objectMethod(proxy, method, args);
						}
					});
			}
			return installed;
		} catch (Throwable t) {
			return false; // Java 8 or no desktop support
		}
	}

	private static boolean supported(Method isSupported, Object desktop, Class<?> actionClass, String action) {
		try {
			Object value = actionClass.getField(action).get(null);
			return ((Boolean) isSupported.invoke(desktop, value)).booleanValue();
		} catch (Throwable t) {
			return false;
		}
	}

	private static boolean setHandler(Class<?> desktopClass, Object desktop, String setter, String handlerInterface,
			InvocationHandler handler) throws Exception {
		Class<?> iface = Class.forName(handlerInterface);
		Object proxy = Proxy.newProxyInstance(MacIntegration.class.getClassLoader(), new Class[] { iface }, handler);
		desktopClass.getMethod(setter, iface).invoke(desktop, proxy);
		return true;
	}

	/** Handles equals/hashCode/toString calls made on a proxy. */
	private static Object objectMethod(Object proxy, Method method, Object[] args) {
		String name = method.getName();
		if ("equals".equals(name)) //$NON-NLS-1$
			return Boolean.valueOf(args != null && args.length == 1 && proxy == args[0]);
		if ("hashCode".equals(name)) //$NON-NLS-1$
			return Integer.valueOf(System.identityHashCode(proxy));
		if ("toString".equals(name)) //$NON-NLS-1$
			return "ProjectLibre MacIntegration handler"; //$NON-NLS-1$
		return null;
	}
}
