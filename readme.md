# SMPE
## What it does
The plugin randomly starts events, in which players can compete against each other if they want to. Some events are free to play while others require an entry cost to join. The winner will then recieve the combined entry cost of all players as a price.

## Events
To win, a player has to complete a randomly picked challenge as fast as possible:
- **Advancement:** Players have to get X advancement
- **Effect:** Players have to get X
- **Item:** Players have to get X item
- **Mob:** Players have to kill X mob
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
# The time it takes between the first join-message and the start of an event
start_time: 120

# The amount of players an event requires to start
min_players: 2

# The chance of an event randomly starting every second is 1 in VALUE
# 0 will disable random events
start_chance: 1800

# The percent of the online players that have to be in the event for skipping the time
time_skip_percent: 100


# The percent of players that are required for skipping an event
voteSkip_percent: 80

# The time to the start the time is skipped to when all players have joined the event
skip_time: 5

Events:
  Advancement:
    probability: 100 # The events are added to a list VALUE amount of times, and then drawn randomly from this list; set to 0 to disable the event
    skill: 80 # Skill Ranking out of 100; Higher ranking will lead to higher entry costs
  Effect:
    probability: 100
    skill: 70
  Item:
    probability: 250
    skill: 80
  Mob:
    probability: 120
    skill: 90
  Biome:
    probability: 120
    skill: 40
  PlaceBlockAt:
    probability: 50
    skill: 30
  PlaceBlockAtIn:
    probability: 50
    skill: 40
  WinRaid:
    probability: 20
    skill: 80
  KillPet:
    probability: 1
    skill: 50


# Items that can be won
Items:
  #If you want all events to be free, just remove all items from this list
  NETHERITE_INGOT: 400   #ITEM: VALUE
  NETHERITE_SCRAP: 250
  DIAMOND: 70
  IRON_INGOT: 20
  WIND_CHARGE: 29
  GOLD_INGOT: 18
  REDSTONE: 9
  COPPER_INGOT: 5
  LAPIS_LAZULI: 6
  DIRT: 3

```

</details>

## Links
- Modrinth: [modrinth.com/plugin/smpe](https://modrinth.com/plugin/smpe)
- Source: [github.com/KEL0002/SMPE](https://github.com/KEL0002/SMPE)
- Report Issues: [github.com/KEL0002/SMPE/issues](https://github.com/KEL0002/SMPE/issues)

