# The Gauntlet

This plugin is written for my personal Minecraft server.
The server is run on Paper 1.20.1 build 196; therefore this plugin uses the Spigot and Bukkit APIs.
To develop locally, Java 21 and Gradle 8.5 are required to build and export to jar.
Please feel free to use this plugin for your own server!
In addition to the releases in this repo, I have a resource pack repo here: [Gauntlet Resources](https://github.com/jaymac00/GauntletResources).

**Implemented Commands:**
- none

**Implemented Events:**
- Grave Events (for player death)
- Head Events (for player death when killed by another player)
- Tunneling Events (for Redstone Pickaxe)

**Implemented Items:**
- Gravestone (block drop)
- Player Head Trophy (entity drop)
- Redstone Pickaxe (recipe)

## Collaborator Need-To-Know

**Branches:**
- master - main branch exported to .jar to use on server; don't worry about this branch
- version* - version branches to stage new implementations and bug fixes; create feature branches out of these
- *-feature-\* - collaborator feature branches to develop new features

All branches require a Pull Request with one approval, I will handle merges from there.

### Third-Party Contributors Need-To-Know

I am not particularly looking for any outside contributions;
but if you truly have a better/more efficient way to script something,
I am willing to look at Pull Requests.
A declined PR may not necessarily mean your feedback is bad,
simply that it is not something I would like to use.
