# Todo

## Fixes

## Features

### Functionality

- Add adaptive layout for windowSizeClass.Medium with list on one side and 
  menu with bottom app bar and permanently shown add-type-dialog on the other
  using Jetpack WindowManager library

- Create reusable Snackbar composable

- Settings:
  - make long pressing the version number copy the version (and maybe show a snackbar)
  - create new database table, handle database migration and changes in view model, dao, etc. (new view model and state?)
  - apply default types, warnings, theme and language (in-app setting + system per-app settings https://developer.android.com/guide/topics/resources/localization https://developer.android.com/guide/topics/resources/app-languages#auto-localeconfig)

- Create UI to remove types individually
  (long press → increase/decrease-buttons replaced by delete button with icon and label to make it same size
  → scrim appears behind pizzaListItem → on button press type is delete, on scrim press everything reverses)

- Improve settings page navigation transition

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

(- Update app icon to reflect (hopefully) stable status)

## Implementation

- Organize and extract Padding values (and other style values?) into respective dedicated files in .ui.theme

- Add modifier arguments for reusable components where applicable

- Check .ui.screens for potentially needed refactoring

- Recreate tests for new database

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