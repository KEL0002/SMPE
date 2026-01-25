# SMPE
## What it does
The plugin randomly starts events, in which players can compete against each other if they want to. Some events are free to play while others require an entry cost to join. The winner will then recieve the combined entry cost of all players as a price.

## Events
To win, a player has to complete a randomly picked challenge as fast as possible:
- **Advancement:** Players have to get X advancement
- **Effect:** Players have to get X
- **Item:** Players have to get X item
- **KillMob:** Players have to kill X mob
- **Biome:** Players have to enter X biome
- **PlaceBlockAt:** Players have to place a block at X coordinates
- **PlaceBlockAtIn:** Players have to place a block at X coordinates in X dimension
- **WinRaid:** Players have win a raid
- **KillPet:** Players have to kill a pet

## Other Features
- Admin command allowing for starting and stopping events (`/smpe [start | stop]`)
- Voteskipping events
- Leaving events
- Very overcomplicated entry cost/price selection system

<details>
<summary>Config</summary>

```YAML
#  ███████╗███╗   ███╗██████╗ ███████╗     ██████╗ ██████╗ ███╗   ██╗███████╗██╗ ██████╗
#  ██╔════╝████╗ ████║██╔══██╗██╔════╝    ██╔════╝██╔═══██╗████╗  ██║██╔════╝██║██╔════╝
#  ███████╗██╔████╔██║██████╔╝█████╗      ██║     ██║   ██║██╔██╗ ██║█████╗  ██║██║  ███╗
#  ╚════██║██║╚██╔╝██║██╔═══╝ ██╔══╝      ██║     ██║   ██║██║╚██╗██║██╔══╝  ██║██║   ██║
#  ███████║██║ ╚═╝ ██║██║     ███████╗    ╚██████╗╚██████╔╝██║ ╚████║██║     ██║╚██████╔╝
#  ╚══════╝╚═╝     ╚═╝╚═╝     ╚══════╝     ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝     ╚═╝ ╚═════╝

# Info: Setting values that don't make sense might break the plugins functionality.
#  If this happens, its not bad. You can either reset the config by deleting its file, or find the default value in the
#  'config' section of this plugins GitHub & Modrinth websites.
# I recommend leaving most stuff as is, but you may consider taking a look at the 'entrycost' section, as you might
#  consider changing it to fit your players play-style

start_chance_denominator: 24000 # Fancy wording for: in each tick, there is a 1 in ... chance of a random event starting
# Default is 24000, so an event will on average start every 20 minutes
random_start_multible_events: false # Allows for multiple events to be overlapping with random starting.

start_time: 121 # Time it takes for an event to start.
notify_times: [1,2,3,4,5,10,20,30] # List of specific times when to send a join message
multiples_of: 30 # Always sends a notification if the time is a multiple of this number; 0=none

min_players: 2
voteskip_percent: 75

console_logging: false
autoupdater: false # Only notifíes about updates, doesn't perform them


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
  winRaidEvent:
    weight: 1
    fireworks_disabled: false
```

</details>

## Links
- Modrinth: [modrinth.com/plugin/smpe](https://modrinth.com/plugin/smpe)
- Source: [github.com/KEL0002/SMPE](https://github.com/KEL0002/SMPE)
- Report Issues: [github.com/KEL0002/SMPE/issues](https://github.com/KEL0002/SMPE/issues)
