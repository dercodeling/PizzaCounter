# Todo

## Fixes

- Bottom navigation bar is not borderless on Samsung Galaxy Tab S7 FE as it is on OnePlus 8 Pro

## Features

### Functionality

- Make it possible to remove single types

- Persistence
    - Implement database
    - Auto-save list on change

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

- Settings:
    - make list of types added in init() changeable
    - dark theme settings
    - language settings
        - Add in-app language setting (default, de, en)
        - Enable the system to enact per-app language settings
    - Warning dialogs for resetting quantities/types

## Implementation

- Refactor MainActivity.kt for better readability → split up into multiple files (e.g. PizzaList, PizzaListItem, ...)

- Rework state management with a separate State data class, events etc. (like in Database Tutorial: https://youtu.be/bOd3wO0uFr8 ~ 18:10)

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