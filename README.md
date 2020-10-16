# mc-hunting

Plugin for hunter/hunted game mode, intended to be used with a `spigot` server.

## Objective
Players choose to be a `hunter` or `hunted`, where the former team tries to kill all members of the latter team before they can reach a certain point in the game. The ending state, whether it be getting to the _Nether_, _End_, etc. is arbitrary and more so up to the players themselves.

Note that once a `hunted` player dies, they will be put in _spectator_ ode until the game is reset.

## Installation
Build a `.jar`, and drop it into the `/plugins` folder from the root of the server installation directory

## Commands

`/role`
- Once a player has joined, pull up a GUI to allow them to choose their role
- The role choices are `hunter` or `hunted`

`/start`
- Once all players have chosen a role, respawn all players and apply debufs to `hunter` players for 60 seconds

`/reset`
- Respawn all players, clear inventory/armor, and quit the current game
