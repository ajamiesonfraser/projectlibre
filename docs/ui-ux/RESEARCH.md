# UI/UX research: what ProjectLibre can learn from other planners

ProjectLibre's users mostly come from Microsoft Project, so **MS Project
desktop is the reference model**. Being familiar matters more than being
novel. Web planners (Project for the web / Planner, Smartsheet, TeamGantt,
OpenProject) show where expectations have moved since OpenProj's 2007 design.

> Sources: Microsoft support documentation and third-party guides (linked at
> the bottom), plus hands-on familiarity with these products. Exact menu names
> differ between MS Project versions. Treat specifics as "how it generally
> works", not pixel specs.

## 1. Microsoft Project (desktop): the reference

**Layout.** The Gantt Chart view is a split view: a task sheet on the left
and a bar chart on the right, with a shared row grid. The timescale sits above
the chart and shows **two tiers** by default (for example weeks over days).
An optional **Timeline** strip can be shown above the Gantt (View → Timeline
checkbox) as a one-glance summary of key tasks and milestones.

**Bars.** Each bar runs from Start to Finish. The defaults are:

* Normal tasks: soft blue fill, darker outline.
* Summary tasks: thin dark bar with downward bracket ends.
* Milestones (zero duration): black diamond with the date next to it.
* Progress: narrower, darker bar inside the task bar.
* Critical path (Format → Critical Tasks): red bars.
* Baseline: thin gray bar under the task.
* Links: thin lines with arrowheads routed between bars.

Bar styles are table-driven (Format → Bar Styles), with one row each for
tasks, summaries and milestones. ProjectLibre's `view.xml` already has the
same model.

**Chart chrome.** Non-working time is shaded with a light, solid color
(Timescale → Non-working time). A **Current Date** gridline marks today,
alongside optional status-date and project-start lines. Gridlines are light
and unobtrusive.

**Sheet.** Rows are compact (~20-22 px), with light gridlines and a strong
selection color. Summary rows are bold with outline +/- toggles. The
**Indicators** column (ⓘ) shows icons for notes, constraints and overallocation.
The **Task Mode** column shows manual vs. automatic scheduling. Columns can be
added inline with an "Add New Column" header.

**Status bar and zoom.** A status bar at the bottom shows the scheduling mode,
view shortcuts and a **zoom slider** for the timescale. That's quicker than
digging through the ribbon.

**Ribbon.** Tabs are File / Task / Resource / Report / Project / View /
Format. Contextual tabs (for example *Gantt Chart Format*) appear only for the
active view. The **Task tab** holds the high-frequency actions: indent,
outdent, link, auto/manual schedule, % complete buttons (0/25/50/75/100),
Inspect.

## 2. Project for the web / Planner (Microsoft's newer design)

* Three switchable views of the same plan: **Grid**, **Board** (Kanban),
  **Timeline** (Gantt). The tabs sit at the top of the plan.
* A former Microsoft designer describes **deliberately separating the grid
  from the timeline** as "a big bet coming from Project Desktop". The timeline
  still shows a task's name, so users don't need the sheet beside it.
* Task bars use a **pastel fill with a dark outline**, chosen so bars meet
  accessibility contrast guidelines. ProjectLibre's new palette follows this.
* A **task details side pane** opens on click instead of a modal dialog, so
  you can edit while still seeing the plan.

## 3. Other tools

| Tool | Pattern worth borrowing |
|---|---|
| **Smartsheet** | Spreadsheet-first editing. Gantt bars and dependencies are created and edited directly in the grid with drag and drop. Reviews highlight baselines and critical path in one Gantt. |
| **TeamGantt** | Praised for simple, friendly Gantt UX. Big hit targets, drag to create or reschedule, drag between bars to link, inline % complete. |
| **OpenProject** | Rebuilt its Gantt as a cleaner, more visual chart with a dedicated UX team, and added inline task creation directly in the list. |
| **GanttProject** | Closest open-source desktop peer (also Swing). Reviewers call its UI dated and say it struggles on large projects. A useful warning: looking modern and staying fast both matter. |
| **Asana / Monday / ClickUp timelines** | Today marker, weekend shading, avatars on bars, hover cards with key dates, zoom presets (Days / Weeks / Months / Quarters). |

Reviewers comparing Gantt tools mostly score **drag-and-drop editing,
dependencies, milestones, baselines, critical path** and **views that stay in
sync**. ProjectLibre already has all of these features. Its gap is
presentation and directness of manipulation.

## 4. Gap analysis for ProjectLibre 1.6.2

Based on running the app; see `screenshots/before-gantt.png`.

| # | Observation (before) | Why it hurts | Fix |
|---|---|---|---|
| 1 | Metal look-and-feel on Linux, dated widgets, small default fonts | Looks abandoned, inconsistent across OSes | **Done:** FlatLaf light theme on every OS, with classic as an option |
| 2 | Gantt bars filled with a checkerboard texture in saturated pure blue/red | Hard to read, "1990s" feel, poor print | **Done:** soft fill and darker outline (MS Project palette) |
| 3 | No antialiasing: jagged diamonds, arrows and diagonal links | Looks low quality, especially on HiDPI | **Done:** antialiased bars, links and labels |
| 4 | Bar labels in bold Times Roman (serif) | Inconsistent with the rest of the UI, noisy | **Done:** regular sans-serif |
| 5 | Link lines strike through bar labels | Labels unreadable in dense plans | **Done:** labels painted last with a light backing |
| 6 | Window opens at 1024×768 on any screen | Wastes space on modern displays | **Done:** 85% of the screen, centered |
| 7 | Name column 150 px, task names cut off | Most-used column is unreadable | **Done:** 250 px; indicators column narrowed |
| 8 | Dark-gray selection with white text | Heavy and unlike MS Project | **Done:** light-blue selection, accent focus border |
| 9 | No today line | Can't see "where are we" at a glance | **Done:** dashed orange current-date line |
| 10 | Non-working days drawn with a dot pattern | Visual noise | **Done:** flat light-gray shading |
| 11 | NullPointerException on every startup | Unstable; may skip ribbon state updates | **Done:** guarded |
| 12 | Timescale header: cramped two-tier text, heavy separators | Hard to scan | Roadmap R1 |
| 13 | No zoom slider; zoom only via the ribbon | Slow navigation | Roadmap R2 |
| 14 | Task Information is a big modal dialog | Interrupts flow | Roadmap R5 |
| 15 | No Timeline summary strip | Hard to communicate the plan to others | Roadmap R4 |
| 16 | Glossy 2008-era ribbon icons, mixed styles | Dated next to the flat theme | Roadmap R3 |
| 17 | Dark theme only re-skins widgets; chart stays white | Half-dark UI | Roadmap R7 |

## 5. Principles for further work

1. **MS Project parity first.** If an MS Project user expects it somewhere,
   put it there with the same name.
2. **Data-driven styling.** Keep colors in `view.xml`, `ModernTheme`, and
   `Colors` (see [DESIGN-SYSTEM.md](DESIGN-SYSTEM.md)), never inline in
   renderers.
3. **Don't break files or habits.** Visual changes only. Don't change the
   `.pod` format or scheduling. Keep `-Dprojectlibre.laf=classic` working.
4. **Keep the OpenProj logo** (license requirement; see DEVELOPMENT.md).
5. **Verify with screenshots** (`scripts/screenshot.sh`) before and after.

## Sources

* [Work with the Gantt Chart view (Microsoft Support)](https://support.microsoft.com/en-us/project/work-with-the-gantt-chart-view)
* [Format the bar chart of a Gantt Chart view (Microsoft Support)](https://support.microsoft.com/en-us/project/format-the-bar-chart-of-a-gantt-chart-view)
* [Change the appearance of nonworking time in timescale views (Microsoft Support)](https://support.microsoft.com/en-US/project/change-the-appearance-of-nonworking-time-in-timescale-views)
* [Overview of Project views (Microsoft Support)](https://support.microsoft.com/en-gb/project/overview-of-project-views)
* [What can you do with Project for the web? (Microsoft Support)](https://support.microsoft.com/en-us/project/what-can-you-do-with-project-for-the-web)
* [Project timeline case study (former Microsoft product designer)](https://jessicaperrin.squarespace.com/project-timeline)
* [Project for the web becoming Planner (The Project Group)](https://www.theprojectgroup.com/blog/en/project-for-the-web-becoming-planner/)
* [MS Project Gantt chart formatting tips (ONES)](https://ones.com/blog/ms-project-gantt-chart-formatting-tips-to-customize-bars-styles/)
* [The Best Gantt Chart Software in 2026 (Smartsheet)](https://www.smartsheet.com/content/best-gantt-chart-software)
* [Best Gantt chart software (The Digital Project Manager)](https://thedigitalprojectmanager.com/tools/best-gantt-chart-software/)
* [OpenProject: separate Gantt charts module](https://www.openproject.org/blog/new-gantt-charts-module/)
* [GanttProject reviews (Capterra)](https://www.capterra.com/p/136586/GanttProject/reviews/)
* [What is GanttProject? (ProjectManager)](https://www.projectmanager.com/blog/ganttproject)
* [FlatLaf - Flat Look and Feel (FormDev)](https://www.formdev.com/flatlaf/)
