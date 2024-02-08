# Todo

## Fixes

## Features

### Functionality

- Add adaptive layout for windowSizeClass.Medium with list on one side and 
  menu with bottom app bar and permanently shown add-type-dialog on the other
  (https://developer.android.com/guide/topics/large-screens/large-screen-canonical-layouts#supporting_pane)

- Settings:
  - create new database table + handle database migration:
    - table should have only columns key and value and a row for each settings can then be added flexibly;
  - use database table via changes in view model, dao, etc. (new view model and state?)
  - apply default types, warnings, theme and language (in-app setting + system per-app settings https://developer.android.com/guide/topics/resources/localization https://developer.android.com/guide/topics/resources/app-languages#auto-localeconfig)

### UI

- Improve AddTypeBottomSheet's animations
  - Opening: deal with lacking smoothness of animation (keyboard overlaps bottom sheet for a
    moment ) in LaunchEffect towards the end of AddTypeBottomSheet
  - Closing: Somehow hide keyboard in closeAndAddPizzaType for smoother animation without it
    causing a transparent screen to remain after closing of bottom sheet (this screen can be
    removed via back gesture)

### Down the road

- Improve smoothness of keyboard opening and closing animations → if smooth enough: automatically
  open keyboard too (code exists near the end of AddTypeBottomSheet as a LaunchedEffect)

- Bottom navigation bar is not borderless on Samsung Galaxy Tab S7 FE like it is on OnePlus 8 Pro (https://developer.android.com/jetpack/compose/layouts/insets)

(- Update app icon to reflect (hopefully :)) stable status)

## Implementation

- Organize and extract Padding values (and other style values?) into respective dedicated files in .ui.theme

- Add modifier arguments for reusable components where applicable

- Check .ui.screens for potentially needed refactoring

- Recreate tests for new database and add other tests like UI tests

# Notes

## Git

### Commit messages

Format:

```markdown
<type>[optional scope]: <description>
```

with type being on of the
following: `feat:, fix:, build:, chore:, ci:, docs:, style:, refactor:, perf:, test:`

For details see [https://www.conventionalcommits.org/en/v1.0.0/#summary]