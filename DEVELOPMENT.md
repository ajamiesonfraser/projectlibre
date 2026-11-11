# Developing ProjectLibre

This guide gets you from a fresh machine to a running, modified ProjectLibre.
For the GitHub side (forks, branches, pull requests) see
[docs/GIT-WORKFLOW.md](docs/GIT-WORKFLOW.md). For the UI/UX work, see
[docs/ui-ux/](docs/ui-ux/).

## 1. What you are working with

ProjectLibre 1.6.2 is a **Java Swing desktop application** (about 1,700 Java
files) built with **Apache Ant**. It descends from OpenProj (Projity, 2007) and
uses a Microsoft Office-style ribbon from the bundled
[Flamingo](https://github.com/kirill-grouchnikov/flamingo) library.

| Module | What's inside |
|---|---|
| `openproj_build/` | **The main build.** `build.xml` compiles every module into `dist/projectlibre.jar`. Also launchers, installers, and sample projects (`resources/samples/`). |
| `openproj_core/` | Scheduling engine, data model, fields, and **configuration XML** (`src/com/projity/configuration/view.xml` holds the Gantt bar styles, `configuration.xml` holds row heights, timescales and fields). |
| `openproj_ui/` | **All of the UI**: frames, ribbon, Gantt, sheets, dialogs, look-and-feel. It also contains a copy of the Flamingo ribbon source (`org/pushingpixels/flamingo`). |
| `openproj_contrib/` | Third-party jars (`lib/`) bundled into `openproj-contrib.jar`, plus a few patched third-party sources. |
| `openproj_exchange/`, `projectlibre_exchange/` | Import and export (MS Project `.mpp`/XML via MPXJ, POI). |
| `openproj_reports/` | Jasper reports. |
| `projectlibre_core/` | ProjectLibre additions to the core. |

### Where to look for UI work

| You want to change... | Look in |
|---|---|
| Theme, colours, fonts of Swing widgets | `openproj_ui/src/com/projity/pm/graphic/laf/ModernTheme.java`, `LafManagerImpl.java` |
| Gantt bar colours and shapes | `openproj_core/src/com/projity/configuration/view.xml` (`<bar>` section) |
| Gantt drawing (bars, links, labels, today line) | `openproj_ui/src/com/projity/pm/graphic/gantt/GanttRenderer.java` |
| Row height, bar height | `openproj_core/src/com/projity/configuration/configuration.xml` (`<graphic .../>`) |
| Sheet (table) cells, selection, columns | `openproj_ui/src/com/projity/pm/graphic/spreadsheet/` |
| Ribbon tabs and buttons | `openproj_ui/src/com/projity/menu/` (`menu.properties`, `MenuManager`) and `pm/graphic/frames/` |
| Main window | `openproj_ui/src/com/projity/pm/graphic/frames/MainRibbonFrame.java`, `GraphicManager.java` |
| Dialogs | `openproj_ui/src/com/projity/dialog/` |
| Timescale header | `openproj_ui/src/com/projity/pm/graphic/timescale/` |
| PERT / network diagram | `openproj_ui/src/com/projity/pm/graphic/network/`, `pert/` |
| Icons | `openproj_ui/src/com/projity/pm/graphic/images/` |
| UI text (translatable) | `openproj_core/src/com/projity/strings/` and `projectlibre_core/src/org/projectlibre/strings/` (`*.properties`) |

## 2. One-time setup

You need:

* **JDK 11 or newer** (JDK 21 is what this branch is developed and tested with).
  [Adoptium Temurin](https://adoptium.net) is a good free choice.
* **Apache Ant 1.10+**
* Optional, Linux only: **Xvfb, ImageMagick and xdotool** for headless
  screenshots and UI automation.

On macOS (Homebrew), Debian/Ubuntu or Fedora, run:

```bash
scripts/setup-dev.sh
```

**Windows:** install Temurin JDK 21 (tick "Set JAVA_HOME"), download the Ant
binary zip from https://ant.apache.org/bindownload.cgi, unzip it to
`C:\ant`, and add `C:\ant\bin` to your `PATH`. Then use `scripts\build.cmd`.

**No local setup at all:** open the repository on GitHub, click
**Code → Codespaces → Create codespace**. The `.devcontainer/` config installs
everything and builds the project. Port 6080 shows a desktop in the browser
(password `vscode`), so you can run the app there.

## 3. Build and run

```bash
scripts/build.sh                 # builds openproj_build/dist/projectlibre.jar
```

```bash
scripts/run.sh                   # starts ProjectLibre
```

```bash
scripts/run.sh "openproj_build/resources/samples/New Product.pod"
```

A full clean rebuild takes about 15 seconds:

```bash
scripts/build.sh clean dist
```

Without the scripts: `cd openproj_build && ant dist`, then
`java -jar dist/projectlibre.jar`.

### Theme switches

The modern (FlatLaf) theme is the default. To compare with the original look,
or to try the experimental dark theme:

```bash
scripts/run.sh "" -Dprojectlibre.laf=classic
```

```bash
scripts/run.sh "" -Dprojectlibre.laf=dark
```

Accepted values are `light` (default), `dark` (experimental: the Gantt chart is
still light), `classic` (the original Metal widgets), and `system` (the OS look).
The theme only affects Swing widgets. The Gantt palette, fonts and metrics
apply to every theme.

### Headless screenshots

For cloud machines, CI, and AI assistants: this launches the app on a virtual
display, opens a sample project, and saves a PNG.

```bash
scripts/screenshot.sh build/screenshots/gantt.png
```

Pass a project file as the second argument and JVM options after that, for
example `scripts/screenshot.sh out.png "" -Dprojectlibre.laf=classic`.

Opening a file on the command line skips the first-run license and welcome
dialogs, which keeps automated runs simple.

## 4. Things that will bite you

* **File encoding is ISO-8859-1**, not UTF-8. The build passes
  `encoding="ISO-8859-1"`, and `.editorconfig` tells editors to use it.
  Saving a file as UTF-8 breaks the `©` in its license header. In IntelliJ,
  set *Settings → Editor → File Encodings* to ISO-8859-1 for this project.
* **The license requires the OpenProj logo.** The CPAL header in every file
  says each screen must show the "OpenProj" logo next to the File menu (top
  left) and link to projity.com. Don't remove or hide it in UI redesigns;
  restyling around it is fine.
* **`//$NON-NLS-1$` comments** mark strings that should not be translated.
  User-visible text belongs in the `strings/*.properties` bundles.
* **Java level.** `build.xml` compiles with `-source/-target 1.8` (properties
  `javac.source` and `javac.target`) so the jar runs on Java 8+. Newer JDKs
  can't emit the original 1.5 bytecode. Avoid Java 9+ APIs in the code.
* **JAXB** was removed from the JDK in Java 11. Its jars are now in
  `openproj_contrib/lib/jaxb/` and bundled into `openproj-contrib.jar`.
* **Saved workspaces.** Column widths and splitter positions are saved per
  user (Java Preferences). If a layout default change seems to have no effect,
  test with a fresh home directory: `java -Duser.home=/tmp/fresh -jar ...`.
  `scripts/screenshot.sh` already does this.
* **Startup console noise** such as `Invalid item: ProjectLibre it must be a
  menu item...` is harmless and was already there.
* **IDE projects.** There are no IntelliJ or Eclipse project files. The
  easiest setup is to import as a plain Java project with these source roots:
  `projectlibre_core/src`, `openproj_core/src`, `openproj_ui/src`,
  `openproj_reports/src`, `projectlibre_exchange/src`, `openproj_exchange/src`,
  `openproj_contrib/src`, plus every jar under `openproj_contrib/lib/**` and
  `projectlibre_exchange/lib/`. Keep using Ant (or `scripts/build.sh`) as the
  real build.

## 5. Checking a UI change

1. `scripts/build.sh`
2. `scripts/screenshot.sh build/screenshots/after.png` (or run the app and look)
3. Compare with `docs/ui-ux/screenshots/` and check the classic theme still
   works (`-Dprojectlibre.laf=classic`).
4. Open a few sample projects from `openproj_build/resources/samples/`,
   including `.mpp` files, and try: Gantt, Tracking Gantt, Network, Resource
   Usage, Task Information dialog, print preview.
5. Commit on a feature branch and open a pull request (see
   [docs/GIT-WORKFLOW.md](docs/GIT-WORKFLOW.md)). CI builds on Java 11 and 21
   and attaches a screenshot to the run.
