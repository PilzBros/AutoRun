# Autorun Setup Guide
## General Information
- Due to its extremely powerful nature, only OP's are given permissions to Autorun by default. You can, by your own discretion, give other users Autorun permission nodes. Consult Autorun's Perms
- If an error results in executing a command, the error message will be displayed (to the user or possibly everyone, based off execution level) 

## All-Player Commands
**Notice**: Please consult Autorun [Commands](commands.md) for correct and up-to-date command syntax 

### Join Broadcast
Let's say for example, we want a message to broadcast every time a user logs into the server.

`/autorun add all Console broadcast Someone joined the server ;)`

### Check Money
Let's say for example, we want the server to display the users current balance when they log into the server.

`/autorun add all money`

## Player Commands
### Auto-Kick
Let's say that we want the user yourm0m to be auto kicked once they join the server. We don't want to ban them for whatever reason, we want to mess with their heads, but only 4 times.

`/autorun add yourm0m Console 4 kick yourm0m`