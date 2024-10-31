# Autorun Commands
Typical command prefix is `/autorun`, however the shortcut `/ar` can be substituted for any command.
When executing commands, replace any information in brackets (ex. [PLAYER] -> herobrine) without the brackets.

## Basics
- `/autorun` - Displays plugin name and version
- `/autorun ?` - Displays all plugin commands
- `/autorun commands` - List of all commands for all users
- `/autorun players` - List of all players with pending auto run commands
- `/autorun player [PLAYER]` - List all commands for provided player username
- `/autorun update` - Displays Autorun update information
- `/autorun reload` - Reloads Autorun 

## Adding Commands
- `/autorun add [PLAYER] [LEVEL] [TIMES] [COMMAND]` - Adds command for provided player
- `/autorun add all [LEVEL] [COMMAND]` - Adds command for all players 

Level Options: Console, Op, Player 

## Deleting Commands
- `/autorun remove [PLAYER] [COMMAND]` - Removes command for provided player
- `/autorun remove all [COMMAND]` - Removes command for all players 

## Clearing Data
- `/autorun purge` - Clears all stored data