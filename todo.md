# Todo

## Features

### Functionality
- Persistence 
  - Implement database
  - Auto-save list on change

- Clear count functionality
- Clear types functionality

### UI
- Improve AddTypeBottomSheet's animations
  - Opening: deal with lacking smoothness of animation (keyboard overlaps bottom sheet for a moment ) in LaunchEffect towards the end of AddTypeBottomSheet
  - Closing: Somehow hide keyboard in closeAndAddPizzaType for smoother animation without it causing a transparent screen to remain after closing of bottom sheet (this screen can be removed via back gesture)

### Down the road
- Improve smoothness of keyboard opening and closing animations → if smooth enough: automatically open keyboard too (code exists near the end of AddTypeBottomSheet as a LaunchedEffect)

- Setting: make list of types added in init() changeable
 
- Add dark theme settings

- Add language settings
  - Add in-app language setting (default, de, en)
  - Enable the system to enact per-app language settings