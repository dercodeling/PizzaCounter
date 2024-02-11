# Todo

## Fixes

- Figure out how to change the window background (set from parent in themes.xml), in order to change
  color of splash screen (maybe implement that properly?) and to prevent white transition artefacts
  (which are caused by the fade in/out of the transition making the window background visible)

## Features

### Functionality

- Check if share works as intended

- Release 1.2.0

- Add adaptive layout for windowSizeClass.Medium with list on one side and 
  menu with bottom app bar and permanently shown add-type-dialog on the other
  (https://developer.android.com/guide/topics/large-screens/large-screen-canonical-layouts#supporting_pane)

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

- Recreate tests for new database 
- Add test for combine function in view model
- Add other tests like UI tests

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
