# CLAUDE.md

Guidance for Claude Code (and other AI assistants) working in this repository.

## Project

ProjectLibre 1.6.2: an open-source Microsoft Project alternative. Java Swing
desktop app, Ant build, ~1,700 Java files. The current focus is a **UI/UX
refresh modelled on Microsoft Project** (desktop). Read
`docs/ui-ux/RESEARCH.md` (why), `docs/ui-ux/ROADMAP.md` (what's next, with
code pointers) and `docs/ui-ux/DESIGN-SYSTEM.md` (colors and sizes) before
UI work. `DEVELOPMENT.md` has the module map and the "where to look" table.

## Commands

```bash
scripts/setup-dev.sh                 # once: JDK 21, Ant, Xvfb, ImageMagick, xdotool (apt/brew/dnf)
scripts/build.sh                     # ant dist -> openproj_build/dist/projectlibre.jar (~15 s)
scripts/build.sh clean dist          # full rebuild
scripts/run.sh [file.pod]            # run (needs a display)
scripts/screenshot.sh out.png [file] [jvm opts]   # headless: Xvfb + screenshot, fresh user.home
```

There are no unit tests worth running. Verify changes by building and taking
a screenshot, then **look at the PNG** (Read tool). Compare with
`docs/ui-ux/screenshots/before-gantt.png` and `after-*.png`. For interaction,
start Xvfb yourself and drive it with `xdotool`. Flamingo ribbon command
buttons don't always react to synthetic clicks, but sheet rows and dialogs do.
Double-clicking a task row opens Task Information.

## Rules

* **Encoding:** `.java`, `.xml` and `.properties` files are **ISO-8859-1**.
  When editing with scripts, read and write with `encoding='iso-8859-1'`.
  Never re-save as UTF-8: it corrupts the `©` in every license header.
  Preserve tabs.
* **License:** keep the CPAL header in files you touch. **Don't remove or
  hide the OpenProj/ProjectLibre logo** at the top left of the ribbon (CPAL
  attribution requirement). New files get a short CPAL note (see
  `ModernTheme.java`).
* **Runtime range:** users run anything from Java 11 to the newest release
  (25 at the time of writing). Before shipping, run the app on the newest JDK
  as well as 21, through the no-file first-run path (license → tips →
  Create Project), not just with a file argument.
* **Java level:** code must compile with `-source 1.8`. No `var`, no Java 9+
  APIs. FlatLaf and other optional libraries are loaded by class name where a
  failure should fall back gracefully (see `LafManagerImpl.installModernLaf`).
* **Colors and sizes** go in the token files listed in DESIGN-SYSTEM.md
  (`ModernTheme`, `Colors.GANTT_*`, `view.xml`, `configuration.xml`), not
  inline in renderers. Update DESIGN-SYSTEM.md when you add a token.
* **Keep the classic theme working:** `-Dprojectlibre.laf=classic` (Metal
  widgets) must still render correctly. Gate widget styling that only suits
  FlatLaf with `ModernTheme.isActive()`. Chart styling applies to all themes.
* Don't change the `.pod` file format or scheduling behavior in UI work.
* `//$NON-NLS-1$` marks non-translatable strings. User-visible text goes in
  `strings/*.properties`.
* Third-party jars live in `openproj_contrib/lib/` and are merged into
  `openproj-contrib.jar` by `openproj_contrib/build.xml`. Add new ones there
  (unsigned jars only) and note their license.
* Saved user preferences can hide default-layout changes. Test with a fresh
  `-Duser.home` (screenshot.sh does this).

## Git

* Remotes: `origin` = the user's fork, `upstream` = `smartqubit/projectlibre`
  (read-only). Default branch: `master`.
* One roadmap item per branch (`ui/<topic>`), committed with before and after
  screenshots in `docs/ui-ux/screenshots/` when visuals change. Tick the item
  in ROADMAP.md.
* Only open pull requests when asked. Target the fork's `master` unless the
  user explicitly says to contribute upstream.

## Key code locations

| Area | Path |
|---|---|
| Look and feel, theme tokens | `openproj_ui/src/com/projity/pm/graphic/laf/` |
| Gantt painting | `openproj_ui/src/com/projity/pm/graphic/gantt/GanttRenderer.java` |
| Bar styles (data) | `openproj_core/src/com/projity/configuration/view.xml` |
| Row and bar metrics | `openproj_core/src/com/projity/configuration/configuration.xml` (`<graphic>`) |
| Sheet rendering | `openproj_ui/src/com/projity/pm/graphic/spreadsheet/` |
| Ribbon and menus | `openproj_ui/src/com/projity/menu/`, `pm/graphic/frames/GraphicManager.java` |
| Main window | `openproj_ui/src/com/projity/pm/graphic/frames/MainRibbonFrame.java` |
| Startup flow | `openproj_ui/src/com/projity/pm/graphic/frames/StartupFactory.java` |
| Ribbon library (Flamingo 6.2, vendored source) | `openproj_ui/src/org/pushingpixels/flamingo/` |
