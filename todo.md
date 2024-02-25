<style>
  priority {
    color: red;
    font-weight: Bold;
  }
</style>

# Todo

## Fixes

- <priority>Figure out why Material You generates wrong colorScheme on Samsung Tablet (colorScheme already wrong in Theme.kt)</priority>

- Figure out why predictive back gestures aren't working between settings and main screen

## Features

### Up next

- Release 1.2.0

- Add adaptive layout for WindowWidthSizeClass.Expanded with list on one side and 
  menu with bottom app bar and permanently shown add-type-dialog on the other
  using Accompanist's TwoPane composable
  (https://developer.android.com/guide/topics/large-screens/large-screen-canonical-layouts#supporting_pane)

- Improve AddTypeBottomSheet's animations
  - Opening: deal with lacking smoothness of animation (keyboard overlaps bottom sheet for a
    moment) in LaunchEffect towards the end of AddTypeBottomSheet
  - Closing: Somehow hide keyboard in closeAndAddPizzaType for smoother animation without it
    causing a transparent screen to remain after closing of bottom sheet (this screen can be
    removed via back gesture)

### Down the road

- Improve smoothness of keyboard opening and closing animations → if smooth enough: automatically
  open keyboard too (code exists near the end of AddTypeBottomSheet as a LaunchedEffect)

- Bottom navigation bar is not borderless on Samsung Galaxy Tab S7 FE like it is on OnePlus 8 Pro (https://developer.android.com/jetpack/compose/layouts/insets)

- Maybe add isExpanded to pizzaType, so that types (notes) stay expanded after relaunch

- Learn how to use backup_rules.xml and data_extraction_rules.xml

(- Update app icon to reflect (hopefully :)) stable status)

## Implementation

- Add multi-previews to screen level composables once Android Studio bug has been fixed 
  (https://issuetracker.google.com/issues/324732800)

- Organize and extract Padding values (and other style values?) into respective dedicated files in .ui.theme

- Add modifier arguments for reusable components where applicable

- Check .ui.screens for potentially needed refactoring

- <priority>Recreate tests for new database</priority>
- <priority>Add test for combine function in view model</priority>
- <priority>Add other tests like UI tests</priority>

# Notes

## Git

### Commit messages

Format:

```
<type>[optional scope]: <description>
```

with type being `feat`, `fix`, `build`, `chore`, `ci`, `docs`, `style`, `refactor`, `perf` or ` test`

For details see [https://www.conventionalcommits.org/en/v1.0.0/#summary]

## Release checklist

1. [ ] Update version number and code
2. [ ] Build APK
3. [ ] Rename APK (`pizza_counter-vx.y.z.apk`)
4. [ ] Create new release on GitHub
   - [ ] Name release ("Version x.y.z - _[core changes]_")
   - [ ] Summarize changes
   - [ ] Attach APK