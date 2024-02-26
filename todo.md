<style>
  priority {
    color: red;
    font-weight: Bold;
  }
  optional {
    color: gray;
  }
</style>

# Todo

## Fixes

- Figure out why Material You generates wrong colorScheme on Samsung Tablet 
  (dynamicLightColorScheme() already wrong in Theme.kt → seems not to be caused by this module)

- Figure out why predictive back gestures aren't working between settings and main screen

- Save button of DefaultTypesBottomSheet is covered by keyboard on Tablet

## Features

### Up next

- <priority>Add banner informing of new app version using GitHub API to see if version with current version number ("Get a release by tag name") is older ("created_at") than newest version ("Get the latest release")
  (https://docs.github.com/en/rest/releases/releases?apiVersion=2022-11-28#get-the-latest-release)

- Release 1.2.1

- Add setting for default quantity (e.g. 1 instead of 0 on add - implement with slider or textField)

- Add adaptive layout for WindowWidthSizeClass.Expanded with list on one side and 
  menu with bottom app bar and permanently shown add-type-dialog on the other
  using Accompanist's TwoPane composable
  (https://developer.android.com/guide/topics/large-screens/large-screen-canonical-layouts#supporting_pane)
  \+ maybe do the same for settings with selection bottom sheet opening on the right

- Improve AddTypeBottomSheet's animations
  - Opening: deal with lacking smoothness of animation (keyboard overlaps bottom sheet for a
    moment) in LaunchEffect towards the end of AddTypeBottomSheet
  - Closing: Somehow hide keyboard in closeAndAddPizzaType for smoother animation without it
    causing a transparent screen to remain after closing of bottom sheet (this screen can be
    removed via back gesture)

### Down the road

- Learn how to use backup_rules.xml and data_extraction_rules.xml

- Improve smoothness of keyboard opening and closing animations → if smooth enough: automatically
  open keyboard too (code exists near the end of AddTypeBottomSheet as a LaunchedEffect)

- Bottom navigation bar is not borderless on Samsung Galaxy Tab S7 FE like it is on OnePlus 8 Pro (https://developer.android.com/jetpack/compose/layouts/insets)

- Maybe add isExpanded to pizzaType, so that types (notes) stay expanded after relaunch

- <optional>Update app icon to reflect (hopefully :)) stable status)</optional>

## Implementation

- Add multi-previews to screen level composables once Android Studio bug has been fixed 
  (https://issuetracker.google.com/issues/324732800)

- Organize and extract Padding values (and other style values?) into respective dedicated files in .ui.theme

- Add modifier arguments for reusable components where applicable

- Check .ui.screens for potentially needed refactoring

- <priority>Recreate tests for new database
- <priority>Add test for combine function in view model
- <priority>Add other tests like UI tests

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
