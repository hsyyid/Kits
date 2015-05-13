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
To configure Kits, first run the server. A Kits.conf file will be created in your config folder. The starting config will look something like the following:

`kits{`
  `default{`
    `items="diamond_axe,"`
  `}`
`kits=default,`
`}`
If you go onto your server, and do /kit default you will get a Diamond Axe.

## Current Commands

* /kits [pgNo] - Shows a PaginatedList of all the avaliable kits, and enables you to spawn them in by click on text.
* /kit [name] - Spawns in a Kit with the speicified name.
* /kit reload - Reloads Kits Config
* /kit add [kit name] [item name] - Adds an Item to a Kit or can Add a Kit with a Specified Item.

## Adding Items to a Kit

If you'd like to add an item to the kit, there are two ways. The supported way, is to simply use /kit add [kit name] [item name], and the item appears in the kit the next time you use the command. No need to even restart your server.

For example, if you'd like to add an apple to kit default, you'd simply do

`/kit add default apple`
It's as simple as that!

If you'd prefer to do it manually, follow the instructions below:

Open the config. Then, simply write the name of another item such as diamond_hoe and put another comma. If you'd like to specify the quantity of the item, simply put the number after the item id. Refer to the following example:

`kits{`
      `default{`
        `items="diamond_axe 10,diamond_hoe 4,"`
      `}`
    `kits=default`
`}`
__DO NOT FORGET TO PUT A COMMA AFTER THE LAST ITEM!__

## Adding Kits

Once again, there are two ways to do this. The first of which is to do /kit add [kit name] [item name] this is the easiest way. For example, if you'd like to add kit owner with a diamond sword, you can just do

/kit add owner diamond_sword
Then, all you have to do is reboot your server, and poof! Your kit is ready to go!

If you'd prefer to do it manually:

Put double quotes around default, and apply the same concept, for example if you wanted to add a kit, owner you'd change it from kits=default, to kits="default,owner," Refer to the following example:

 kits{
         default{
            items="diamond_axe,diamond_hoe,"
          }
        kits="default,owner,"
    }
DO NOT FORGET TO PUT A COMMA AT THE END!

Then, reboot your server, and a new config will be generated, similar to the one below.

kits {
    default {
        item="diamond_axe"
    }
    kits="default,owner,"
    owner {
        item="diamond_axe"
    }
}
From here, simply modify the items using the method provided above. NOTE THIS ONLY WORKS IF YOU'RE ADDING A KIT. IF YOU'VE ADDED ONE AND WANT TO DELETE IT, YOU MUST ERASE IT, OR REINSTALL THE PLUGIN! IF YOU DELETE IT TO ADD A KIT IN PLACE OF IT YOU MUST WRITE IT OUT YOURSELF!

## How do you Spawn a Kit?

Simply do:
`/kit (name)`
Where name and the parenthesis would be replaced with the kit name. An example of spawning the default kit would be as follows:

`/kit default`

## Permissions

kits.use - Access to the /kit command.
kits.reload - Access to the /kit reload command.
kits.list - Access to the /kits command.

## Kit Specific Permissions

kits.use.[kitname] - Access to the /kit [kitname] command.
For example, if you wanted to give permission to use kit default, the permission would be

kits.use.default

## Clone
The following steps will ensure your project is cloned properly.  
1. `git clone git@github.com:SpongePowered/SpongeAPI.git`  
2. `cd SpongeAPI`  
3. `cp scripts/pre-commit .git/hooks`

## Building
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for Windows systems in place of any 'gradle' command.

In order to build SpongeAPI you simply need to run the `gradle` command. You can find the compiled JAR file in `./build/libs` labeled similarly to 'spongeapi-x.x.x-SNAPSHOT.jar'.

## Contributing
Are you a talented programmer looking to contribute some code? We'd love the help!
* Open a pull request with your changes, following our [guidelines](CONTRIBUTING.md).
* Please follow the above guidelines and requirements for your pull request(s) to be accepted.

[Developer Website]: http://negafinity.com
[Issues]: https://github.com/hsyyid/Kits/issues
[Sponge Website Posting]: https://forums.spongepowered.org/t/kits-v0-1/7099
[Source]: https://github.com/hsyyid/Kits/tree/master/src/main/java/io/github/hsyyid
[here]: http://www.mediafire.com/download/lvum35nnsebi8l0/Kits-0.1.jar