# ProjectLibre design system (modern theme)

Every color and size introduced by the UI refresh is listed here. Change
them in the source of truth, not in individual components.

## Where tokens live

| Layer | File | Holds |
|---|---|---|
| Swing widgets | `openproj_ui/src/com/projity/pm/graphic/laf/ModernTheme.java` | Accent, selection, grid, header colors; FlatLaf settings (corner radius, scrollbars) |
| Gantt bars | `openproj_core/src/com/projity/configuration/view.xml` (`<bar>`) | Fill and outline per bar type, link and label colors, non-working shading |
| Gantt chrome | `openproj_core/src/com/projity/graphic/configuration/shape/Colors.java` (`GANTT_*`) | Progress bar, today line, timescale separators |
| Metrics | `openproj_core/src/com/projity/configuration/configuration.xml` (`<graphic>`) | Row height, bar height and offsets |
| Fonts | `openproj_core/src/com/projity/util/Environment.java` (`fonts`) | Gantt label and network fonts |

## Color tokens

### UI (ModernTheme)

| Token | Hex | Use |
|---|---|---|
| `ACCENT` | `#2F6DB5` | Focus rings, default buttons, focused cell border |
| `GRID` | `#E3E6EA` | Sheet gridlines |
| `SELECTION_BACKGROUND` | `#CFE2F7` | Selected rows |
| `SELECTION_FOREGROUND` | `#1B1B1B` | Text on selected rows |
| `HEADER_BACKGROUND` | `#F3F4F6` | Column and row headers |
| `HEADER_FOREGROUND` | `#3A3F45` | Header text |

### Gantt bars (view.xml)

| Bar | Outline (`colorName`) | Fill (`fillColorName`) |
|---|---|---|
| Task | `#3E75B8` | `#8FB8E6` |
| Critical task | `#C0392B` | `#F2A39B` |
| Late task | `#7E57C2` | `#CDB9EC` |
| Assignment | `#C77A12` | `#F6C987` |
| Summary (bracket) | `#3B3F45` | (solid) |
| Milestone (diamond) | `#2E3338` | (solid) |
| Deadline (diamond) | `#2E9E5B` | (solid) |
| Baseline | `#9AA3AD` | (solid) |
| Total slack | `#A9C1DE` | diagonal hatch |
| Links | `#4A5560` | |
| Labels | `#2B2F33` | |
| Non-working time | `#F0F1F3` | |
| Row lines (Grid.showRows) | `#ECEEF1` | |

### Gantt chrome (Colors.java)

| Token | Hex | Use |
|---|---|---|
| `GANTT_PROGRESS` | `#1F3F66` | % complete bar inside a task |
| `GANTT_TODAY` | `#E07B00` | Dashed current-date line |
| `GANTT_SCALE_SEPARATOR` | `#D5D9DE` | Vertical lines at major timescale units |

**Rules of thumb.** Fills are light enough that black text and the dark
progress bar stay readable on top. Outlines are dark enough (≥3:1 against
white) to define the bar. Red is reserved for the critical path, and the
today line uses orange so it never reads as "critical".

## Metrics

| Setting | Old | New |
|---|---|---|
| `rowHeight` | 19 | 22 |
| `ganttBarHeight` | 11 | 12 |
| `ganttBarYOffset` | 4 | 5 (centers the bar) |
| Name column | 150 | 250 |
| Indicators column | 50 | 36 |
| Main window | 1024×768 | 85% of the screen |

## view.xml syntax

```xml
<middle shapeName="FULL_HEIGHT"
        colorName="#3E75B8"        <!-- outline; also used for links/labels of this format -->
        fillColorName="#8FB8E6"    <!-- optional; new in the refresh -->
        paintName="SOLID"          <!-- SOLID | DEFAULT (checker) | DIAGONAL | ... -->
        strokeName="FRAMED"/>      <!-- FRAMED draws the outline; SOLID = no outline -->
```

Colors can be a name from `Colors.java` (e.g. `DARK_GRAY`) or `#rrggbb`.
