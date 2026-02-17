# SMPE
## What it does
The plugin randomly starts 'Events', in which players can compete against each other to complete a certain challenge. By default, most events are free to play, but some require an entry cost to join. The winner will then receive the combined entry cost of all players as a price.

## Events
To win, a player has to complete a randomly picked challenge as fast as possible:
- **Advancement:** Players have to get X advancement
- **Biome:** Players have to enter X biome
- **Effect:** Players have to get X effect
- **Item:** Players have to get X item
- **KillMob:** Players have to kill X mob
- **KillPet:** Players have to kill a pet
- **PlaceBlockAt:** Players have to place a block at X coordinates
- **PlaceBlockAtIn:** Players have to place a block at X coordinates in X dimension
- **StructureEvent:** Players have to enter X structure
- **WinRaid:** Players have win a raid

## Other Features
- Admin command allowing for starting and stopping events with `/smpe start/stop [EVENTNUMBER]`
- Voteskipping events with `/smpe voteskip`
- Leaving events with `/smpe leave`
- Listing all running events with `/smpe list`
- Very overcomplicated entry cost selection system
- Basically 100% customization over every message the plugin sends
- A powerful config

<details>
<summary>Config</summary>

```YAML
#  ███████╗███╗   ███╗██████╗ ███████╗     ██████╗ ██████╗ ███╗   ██╗███████╗██╗ ██████╗
#  ██╔════╝████╗ ████║██╔══██╗██╔════╝    ██╔════╝██╔═══██╗████╗  ██║██╔════╝██║██╔════╝
#  ███████╗██╔████╔██║██████╔╝█████╗      ██║     ██║   ██║██╔██╗ ██║█████╗  ██║██║  ███╗
#  ╚════██║██║╚██╔╝██║██╔═══╝ ██╔══╝      ██║     ██║   ██║██║╚██╗██║██╔══╝  ██║██║   ██║
#  ███████║██║ ╚═╝ ██║██║     ███████╗    ╚██████╗╚██████╔╝██║ ╚████║██║     ██║╚██████╔╝
#  ╚══════╝╚═╝     ╚═╝╚═╝     ╚══════╝     ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝     ╚═╝ ╚═════╝

# Info: Setting values that don't make sense might break the plugins' functionality.
#  If this happens, it's not a huge problem. You can either reset the config by deleting its file, or find the default value in the
#  'config' section of this plugins GitHub & Modrinth websites.
# I recommend leaving most stuff as is, but you may consider taking a look at the 'entrycost' section, as you might
#  consider changing it to fit your players play-style

start_chance_denominator: 24000 # Fancy wording for: in each tick, there is a 1 in ... chance of a random event starting
# Default is 24000, so an event will on average start every 20 minutes
random_start_multible_events: false # Allows for multiple events to be overlapping with random starting.

start_time: 121 # Time it takes for an event to start.
notify_times: [1,2,3,4,5,10,20,30] # List of specific times when to send a join message
multiples_of: 30 # Always sends a notification if the time is a multiple of this number; 0=none

timeskip_percent: 80 # % of online players that have to be in the event for it's time to skip to 5s

min_players: 2
voteskip_percent: 75

console_logging: false
autoupdater: true # Only notifíes about updates, doesn't perform them


# If you want all events to be free, set all weights except 'none' to 0
entrycost:
  none:
    weight: 10
  item:
    weight: 6
    material_values:
      # How it works: The player progress on the server gets rated from 0-1024 (±30% random variation)(/smpe sprogress). The progress is based on type of armor and toolset
      # Then, an item gets randomly chosen from this list. The progress gets divided by the item value, leading to the amount of items chosen
      # Example: Everyone has netherite armor and tools (progress = 1024 ±30% -> progress = 870). Wind charge with value of 16 gets chosen -> 870/16 = 54 [Wind charges]
      - diamond: 60
      - iron_ingot: 30
      - wind_charge: 16
      - gold_ingot: 16
      - redstone: 12
      - copper_ingot: 10
      - lapis_lazuli: 10
      - dirt: 2
  xp:
    weight: 1
    # How it works: In the end, the average (average with small bias to the poor people) is divided by a random value between those two numbers
    divisor_min: 2
    divisor_max: 10



events:
  advancementEvent:
    weight: 7
    fireworks_disabled: false
  biomeEvent:
    weight: 7
    fireworks_disabled: false
  effectEvent:
    weight: 5
    fireworks_disabled: false
  itemEvent:
    weight: 12
    fireworks_disabled: false
  killMobEvent:
    weight: 6
    fireworks_disabled: false
  killPetEvent:
    weight: 1
    fireworks_disabled: false
  placeBlockAtEvent:
    radius: 3000 # Max random radius around the center position of all players
    weight: 6
    fireworks_disabled: true
  placeBlockAtInEvent:
    radius: 3000
    weight: 6
    fireworks_disabled: true
  structureEvent:
    weight: 6
    fireworks_disabled: false
  winRaidEvent:
    weight: 1
    fireworks_disabled: false
```

</details>

<details>
<summary>Language Config</summary>

```YAML
# This file mostly supports the MiniMessage format
#   https://webui.advntr.dev for online creator, https://docs.papermc.io/adventure/minimessage/format/ for doc
#   Use /smpe echo TEXT to test in game
# Variables starting or ending with '_' will have their values have added a ' ' at the start/end if the value is not empty
# Variables are almost always available in a logical context
# Define custom variables at the bottom
command_feedback:
  stop_all: "%p%aAll Events were stopped."
  stop_single: "%p%aEvent %EVENT_NUMBER% was stopped."
error:
  usage: "%p%NO%%eUsage: /smpe [join | leave | voteskip | list] "
  usage_join: "%p%NO%%eUsage: /smpe join [EVENTNUMBER] %EventListInteraction%"
  usage_stop: "%p%NO%%eUsage: /smpe stop [EVENTNUMBER] %EventListInteraction%"
  not_a_player: "%p%eYou have to be a player to perform this command."
  no_permission: "%p%NO%%eYou do not have the permission to perform this command"
  not_in_event: "%p%NO%%eYou are not in an event."
  event_out_of_range: "%p%NO%%eThe event does not exist"
  event_already_running: "%p%NO%%eThe event you are trying to join is already running"
  fireworks_disabled: "%p%NO%<sprite:items:item/firework_rocket>%eFireworks are disabled during the current event"
info:
  list: "%pThere are currently %AMOUNT% Event(s) running:\n%LIST%"
pregame:
  not_joined: "%pAn Event is starting in %CLOCK%%TIME%! %JoinInteraction%<hover:show_text:'This Event requires %ENTRYCOST_RAW% to join. The winner will receive the combined cost of all players. If the event is cancelled, you will get it back.'>%_ENTRYCOST_FORMATTED%</hover>"
  joined: "%pThe Event is starting in %CLOCK%%TIME%! %LeaveInteraction%"
  join: "%p%YES%%PH% %PLAYER% joined Event %EVENT_NUMBER%!"
  join_already_joined: "%p%NO%%eYou are already in this Event!"
  join_already_joined_other: "%p%NO%%eYou are already in Event %OTHER_EVENT_NUMBER%. Leave it to join Event %EVENT_NUMBER%"
  join_no_entry_cost: "%p%NO%%eThis Event requires %ENTRYCOST_RAW% to join. <hover:show_text:'This Event requires %ENTRYCOST_RAW% to join. The winner will receive the combined cost of all players. If the event is cancelled, you will get it back.'>🛈</hover>"
  actionbar: "%p%YES%You are in the Event. Starting in %CLOCK%%TIME% (%STEVE%ₓ%PLAYERCOUNT_LOWER%)"
start:
  not_enough_players: "%p%NO%There were not enough players to start the event"
  started_not_participating: "%pThe Event has started"
  started_participating: "%p%YES%The Event has started! Your goal is to %EVENT_GOAL% as fast as possible. %VoteskipInteraction%%_NOTICE%"
  goals:
    # Special variables:
    #   %SOMEATTRIBUTE% - Formatted version of the goals attribute e.g. %ITEM%
    #   %SOMEATTRIBUTE_RAW% - Raw version of the goals attribute
    #   %TR_KEY% - Translation key for the event goals attribute. common usage: <lang:%TR_KEY%>
    # Note that translation keys change based on the players selected language, but that %SOMEATTRIBUTE% also may not be what the attribute is called in English
    advancementEvent: "get the advancement <hover:show_text:'%ADVANCEMENT%:\n<lang:%DESCRIPTION%>'><lang:%TR_KEY%></hover>"
    biomeEvent: "enter the %BOOTS%biome <hover:show_text:'%BIOME%'><lang:%TR_KEY%></hover>"
    effectEvent: "obtain the potion effect %EFFECT_ICON%<hover:show_text:'%EFFECT%'><lang:%TR_KEY%></hover>"
    itemEvent: "get the item %ITEM_ICON%<hover:show_text:'%ITEM%'><lang:%TR_KEY%></hover>"
    killMobEvent: "%SWORD%kill the mob <hover:show_text:'%MOB%'><sprite:items:item/%MOB_RAW%_spawn_egg><lang:%TR_KEY%></hover>"
    killPetEvent: "%SWORD%kill any <sprite:items:item/cat_spawn_egg>pet"
    placeBlockAtEvent: "place a block at <sprite:gui:hud/locator_bar_arrow_down>%LOCATION%"
    placeBlockAtInEvent: "place a block at <sprite:gui:hud/locator_bar_arrow_down>%LOCATION% in %WORLD_ICON%%WORLD%"
    structureEvent: "enter the %STRUCTURE_BLOCK% structure %STRUCTURE%"
    winRaidEvent: "win a <sprite:gui:mob_effect/raid_omen>raid"
  notice:
    advancementEvent: "<hover:show_text:'The advancement has been temporarily revoked from you if you already had it. You will get it back after this event'>🛈</hover>"
    biomeEvent: ""
    effectEvent: ""
    itemEvent: ""
    killMobEvent: ""
    killPetEvent: "<hover:show_text:'A pet is considered a mob that was tamed by any player'>🛈</hover>"
    placeBlockAtEvent: ""
    placeBlockAtInEvent: ""
    structureEvent: ""
    winRaidEvent: ""
main:
  leave: "%p%NO%%PH% %PLAYER% left the event!"
  voteskip_already_done: "%p%NO%%eYou have already voted for skipping this event"
  voteskip_before_start: "%p%NO%%eYou cannot vote to skip before the start"
  voteskip: "%p<sprite:items:item/firework_rocket>%PH% %PLAYER% voted for skipping this event (%VOTESKIPPER_AMOUNT%/%VOTESKIPPERS_REQUIRED%)"

  actionbar: "%CLOCK%%TIME% - %ACTIONBAR_GOAL%"
  actionbar_goal:
    advancementEvent: "Get the advancement: <lang:%TR_KEY%>"
    biomeEvent: "%BOOTS%Enter the biome: <lang:%TR_KEY%>"
    effectEvent: "Obtain the effect: %EFFECT_ICON%<lang:%TR_KEY%>"
    itemEvent: "Get the item: %ITEM_ICON%<lang:%TR_KEY%>"
    killMobEvent: "%SWORD%Kill the mob: <sprite:items:item/%MOB_RAW%_spawn_egg><lang:%TR_KEY%>"
    killPetEvent: "%SWORD%Kill a <sprite:items:item/cat_spawn_egg>pet"
    placeBlockAtEvent: "Place a block at: <sprite:gui:hud/locator_bar_arrow_down>%LOCATION%"
    placeBlockAtInEvent: "Place a block at: <sprite:gui:hud/locator_bar_arrow_down>%LOCATION% in %WORLD_ICON%%WORLD%"
    structureEvent: "%STRUCTURE_BLOCK% Enter the structure: %STRUCTURE%"
    winRaidEvent: "Win a <sprite:gui:mob_effect/raid_omen>raid"

end:
  winner: "%p%YES%%PH% %PLAYER% won the event!"
  voteskipped: "%p%NO%The event was voteskipped!"
  cancel: "%p%NO%The event was cancelled!"
  actionbar: ""
  title_winner: "%p%PH% %PLAYER% won the event!"
console: # MiniMessage not supported in the console
  join: "%p%PLAYER% has joined event %EVENT_NUMBER%."
  leave: "%p%PLAYER% has left event %EVENT_NUMBER%."
  start: "%pEvent %EVENT_NUMBER% has started."
  end: "%pEvent %EVENT_NUMBER% has ended for reason: %MESSAGE%"


updater:
  updateCheckFailed: "%pFailed to check fetch updates for SMPE"
  console_updateAvailable: "%pThere is a new update for SMPE available! (%CURRENT% -> %NEW%) Download: https://modrinth.com/plugin/smpe/version/%NEW%"
  player_updateAvailable: "%p%aThere is a new update available for SMPE! (%CURRENT% -> %NEW%) <click:open_url:https://modrinth.com/plugin/smpe/version/%NEW%><color:#1bd96a>[DOWNLOAD]</click>"

# Map custom Variables to Strings
# Fixed variables never contain non-capitalized characters
custom:
  "%p": "" #Preset for all chat messages; empty by default
  "%PH%": "<head:%PLAYER%>" #Display the player head icon
  "%CLOCK%": "<sprite:items:item/clock_20>"
  "%STEVE%": "<head:entity/player/wide/steve>"
  "%SWORD%": "<sprite:items:item/diamond_sword>"
  "%BOOTS%": "<sprite:items:item/diamond_boots>"
  "%STRUCTURE_BLOCK%": "<sprite:blocks:block/structure_block>"
  "%YES%": "<sprite:gui:pending_invite/accept>"
  "%NO%": "<sprite:gui:pending_invite/reject>"

  "%EventListInteraction%": "<color:#ffffff><hover:show_text:'Click to show a list of all running events'><click:run_command:'smpe list'><sprite:items:item/paper>ₗᵢₛₜ ₑᵥₑₙₜₛ</click>"
  "%LeaveInteraction%": "<hover:show_text:'Click to leave the event'><click:run_command:'smpe leave'><sprite:items:item/spider_eye>ₗₑₐᵥₑ</click></hover>"
  "%JoinInteraction%": "<hover:show_text:'Click to join the event'><click:run_command:'smpe join %EVENT_NUMBER%'><sprite:items:item/slime_ball>ⱼₒᵢₙ</click></hover>"
  "%VoteskipInteraction%": "<hover:show_text:'Click to voteskip the event. You can always use /smpe voteskip to do this'><click:run_command:'smpe voteskip'><sprite:items:item/firework_rocket>ᵥₒₜₑₛₖᵢₚ</click></hover>"

  #=============================================
  #========= Customise the colors here =========
  #=============================================

  "%a": "<color:#b805ff>" # Feedback for admin commands
  "%e": "<color:#7e0b01>" # Color for errors
```

</details>

## Links
- Modrinth: [modrinth.com/plugin/smpe](https://modrinth.com/plugin/smpe)
- Source: [github.com/KEL0002/SMPE](https://github.com/KEL0002/SMPE)
- Report Issues: [github.com/KEL0002/SMPE/issues](https://github.com/KEL0002/SMPE/issues) ← Please do that!
- View Metrics: [bstats.org/plugin/bukkit/SMPE/25137](https://bstats.org/plugin/bukkit/SMPE/25137)
