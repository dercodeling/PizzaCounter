# Todo

## Fixes

- Bottom navigation bar is not borderless on Samsung Galaxy Tab S7 FE as it is on OnePlus 8 Pro

- **Name text is to narrow on some devices → smaller buttons? smaller padding? two rows?**

## Features

### Functionality

- Improve settings page navigation transition

- Don't navigate back to Settings page on back gesture from MainScreen

- Settings: → new database table → see tutorials on database migration
  -> initial types, theme, language (in-app setting + system per-app settings), reset warnings, about-dialog (version, GitHub, license?)
  - create ui for all settings
  - create new database table, handle database migration and changes in view model, dao, etc. (new view model and state?)

- Create UI to remove types individually

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

(- Update app icon to reflect (hopefully) stable status)

## Implementation

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