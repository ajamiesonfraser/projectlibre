# UI/UX roadmap

Ordered by value to a Microsoft Project user versus effort. Each item names
the code to start from, so a future session (human or Claude) can pick one
up directly. Use one branch and one pull request per item.

## Done: round 1 ("Modern Gantt")

Branch `ui/modern-gantt-refresh`. Before and after screenshots are in
[screenshots/](screenshots/).

- [x] Builds and runs on JDK 11-21 (javac level as a property, JAXB bundled)
- [x] FlatLaf light theme by default; `-Dprojectlibre.laf=classic|system|dark`
- [x] MS Project-style bar palette (soft fill and outline) via `fillColorName` and `#hex` colors in `view.xml`
- [x] Antialiased Gantt and network rendering
- [x] Sans-serif bar labels, painted above link lines with a light backing
- [x] Today (current date) line; flat non-working-time shading; lighter timescale separators
- [x] Row height 19→22 px, bar 11→12 px
- [x] Main window opens at 85% of the screen, centered
- [x] Wider Name column, narrower Indicators column
- [x] Light-blue row selection, accent focus border
- [x] Fixed startup NullPointerException in `CommonSpreadSheetModel.getActionList`

## Next

### R1. Timescale header refresh (small)
Use a clearer two-tier layout like MS Project (e.g. "Jan 2 '00" over
"M T W T F S S"). Use the header background from `ModernTheme`, light
separators, and centered labels. Highlight today's column.
*Code:* `openproj_ui/.../pm/graphic/timescale/`, `TimeScaleComponent.java`,
the timescale patterns in `configuration.xml` (`<timescales>`).

### R2. Zoom slider and view switcher in a status bar (medium)
Add a status bar to the main frame. Put view shortcut icons (Gantt / Network /
Resource Sheet / Usage) on the right with a zoom slider wired to the existing
timescale zoom actions, and scheduling info on the left.
*Code:* `MainRibbonFrame`, `GraphicManager` (zoom actions:
`ACTION_ZOOM_IN/OUT`), `TimeScaleManager`.

### R3. Icon refresh (medium)
Replace the glossy 2008 icons with a consistent flat set: FlatLaf-compatible
SVG via `FlatSVGIcon`, or a permissively licensed set (Fluent UI System Icons
are MIT). Start with the ribbon's Task tab.
*Code:* `openproj_ui/.../pm/graphic/images/`, `IconManager`,
`menu.properties` (icon keys).

### R4. Timeline strip above the Gantt (medium-large)
An optional, collapsible band above the Gantt showing the project span, today,
milestones, and tasks flagged "Show on timeline". This is MS Project's most
visible post-2010 feature.
*Code:* new component under `pm/graphic/views/`, reuse `CoordinatesConverter`.

### R5. Task details side pane (large)
A dockable right-hand pane with the Task Information fields (General,
Predecessors, Resources, Notes) for the selected task, updating live. Keep the
modal dialog for double-click to begin with.
*Code:* `dialog/TaskInformationDialog.java` (reuse its panels),
`DocumentFrame`.

### R6. Direct manipulation polish (medium)
Hover cards on bars (name, start, finish, duration, % complete). Show the
drag-link affordance on hover. Snap to day while dragging and show the date
tooltip.
*Code:* `GanttInteractor.java`, `GraphInteractor.java`.

### R7. Proper dark theme (medium)
Route chart backgrounds, grid, shading and label colors through
`ModernTheme`, so `-Dprojectlibre.laf=dark` darkens the Gantt as well. Add a
dark variant of the bar palette (lighter outlines).
*Code:* `GanttRenderer.paintNonWorkingDays`, `Colors`, `view.xml`.

### R8. Theme picker (small)
Add a "Theme" choice (Light / Dark / Classic / System) to the ribbon's View
tab. There is an unused `ACTION_LOOK_AND_FEEL` / `LookAndFeelAction` stub in
`GraphicManager`. It calls `LafManagerImpl.setLafChoice()` and asks the user
to restart.
*Code:* `GraphicManager.LookAndFeelAction`, `menu.properties`.

### R9. HiDPI audit (small)
Run with `-Dsun.java2d.uiScale=2`. Fix blurry PNG icons (provide @2x or SVG)
and hard-coded pixel sizes in `configuration.xml` (`rowHeight`,
`columnHeaderHeight`).

### R10. Welcome / start screen (medium)
Replace the startup dialog with a start page: recent projects, templates
(the sample `.pod` files), "Open .mpp" and a blank project.
*Code:* `StartupFactory.doStartupAction`, `GraphicManager.doWelcomeDialog`.
