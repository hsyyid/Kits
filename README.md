Kits
=============
**Currently still under development!**  
A Minecraft Sponge plugin for Kits!

* [Developer Website]
* [Source]
* [Issues]
* [Sponge Website Posting]

## Downloads

As this plugin is still in development, the development build may have issues! However, if you'd still like to download the plugin, you can download the latest development build [here]. Please report any issues by responding to this thread.

## How to Install

Simply drop the downloaded JAR into your mods folder! If you don't know how to make a Sponge server, check out the Sponge Documentation!

## How to Use/Configure Kits?
To configure Kits, first run the server. A Kits.conf file will be created in your config folder.  If you go onto your server, and do /kit default you will get a Diamond Axe.

## Current Commands

* /kits [pgNo] - Shows a PaginatedList of all the available kits, and enables you to spawn them in by click on text.
* /kit [name] - Spawns in a Kit with the specified name.
* /kit reload - Reloads Kits Config.
* /kit add [kit name] [item name] - Adds an Item to a Kit or can Add a Kit with a Specified Item.
* /kit delete [kit name] - Deletes specified Kit.
* /kit interval [kit name] [interval] - Change the interval of the specified kit in MILLISECONDS.

## Adding Items to a Kit

If you'd like to add an item to the kit, there are two ways. The supported way, is to simply use /kit add [kit name] [item name], and the item appears in the kit the next time you use the command. No need to even restart your server.

For example, if you'd like to add an apple to kit default, you'd simply do

/kit add default apple
It's as simple as that!

If you'd prefer to do it manually, there are instructions of the Official Sponge Powered Thread.

## Adding Kits

Once again, there are two ways to do this. The first of which is to do /kit add [kit name] [item name] this is the easiest way. For example, if you'd like to add kit owner with a diamond sword, you can just do

/kit add owner diamond_sword
Then, all you have to do is reboot your server, and poof! Your kit is ready to go!

## How do you Spawn a Kit?

Simply do:
/kit (name)
Where name and the parenthesis would be replaced with the kit name. An example of spawning the default kit would be as follows:

/kit default

## Permissions

* kits.use - Access to the /kit command.
* kits.reload - Access to the /kit reload command.
* kits.list - Access to the /kits command.
* kits.add - Access to the /kit add command
* kits.interval - Access to the /kit interval command
* kits.delete - Access to the /kit delete command.

## Kit Specific Permissions

kits.use.[kitname] - Access to the /kit [kitname] command.
For example, if you wanted to give permission to use kit default, the permission would be

kits.use.default

[Developer Website]: http://negafinity.com
[Issues]: https://github.com/hsyyid/Kits/issues
[Sponge Website Posting]: https://forums.spongepowered.org/t/kits-v0-1/7099
[Source]: https://github.com/hsyyid/Kits/tree/master/src/main/java/io/github/hsyyid
[here]: https://github.com/hsyyid/Kits/releases