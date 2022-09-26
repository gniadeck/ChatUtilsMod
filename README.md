# ChatUtils Minecraft mod

## Purpose

Have you ever wanted to do some powerfull stuff on chat, but you weren't able to find mod fulfiling your trolling needs? This is the moment when ChatUtils comes in. This epic mod allow you to mess up with your friends on chat. Invoke commands for every player, random player, pay everybody some amount, privately message everyone, and so on. Have fun! ;)


## Available commands

### /allplayers 'command'
#### ex. /allplayers /pay %u 1000 - invokes /pay command for each user on the server (%u is replaced for nickname)
This command allows you to invoke any command (or write message to chat if you dont provide slash at the beggining of command argument) for every player available on the server. For example, if players "Pog","Test" and "Siemanko" are available on the server, example will call this commands:
```
/pay Pog 1000
/pay Test 1000
/pay Siemanko 1000
```

### /giveaway 'password' 'amount' 'message'
#### ex. /giveaway test 1000 Whoever writes %p on chat, wins %a $!
This commands create a 'giveaway' based on who writes given password on chat first. Author of the giveaway is excluded from potential winners. The chat would look like this:
```
Invoker: Whoever writes test on chat, wins 1000 $!
TestPlayer: test
Invoker: Congratulations, TestPlayer, you won 1000$! 
*invoking /pay TestPlayer 1000*
```
The message that is being sent after winning is configurable in properties file

### /messageall 'message'
#### equal to /allplayers /msg %u 'message'
/msg can be substituted to any command prefix you want, using properties file.

### /randomplayer 'message'
#### ex. /randomplayer I love you %u !
This epic command substitutes %u placeholder for any random player available on server, excluding the invoker.  For example, if players "Pog","Test" and "Siemanko" are available on the server, example can send this message:
`I love you Pog !`
You can also make 'message' an command, for example
`/randomplayer /pay %u 1000`
will pay a random player 1000$ !

### /tpahereall *optional* 'message'
#### ex. /tpahereall Hi, wanna see my epic house?
If you are not an admin, but you want to ask all server players to teleport to your current location, you can use /tpahereall command. If no argument is provided, the command just works like `/allplayers /tpahereall %u`, but if argument is provided, after invoking /tpahere *player* command, private message (/msg) is sent. So, invoked commands would be:
```
*/tpahere Pog*
*/msg Pog Hi, wanna see my epic house?*
```

## Configuration
All of the command prefixes, like /msg, /tpahere, /pay are fully configurable. Feel free to use the properties file and tweak mod's configuration for your favourite server. If you want, you can also open an issue with your configuration and I will be more than happy to include it in this repo!
Delays between command invocations are also fully configurable. Have fun!

Content of ChatUtilsMod.properties
```payall.delay=1000 - delay for /payall command
payall.command=/pay - command to invoke when calling /payall
allplayers.delay=7000 - delay for /allplayers command
messageall.prefix=/msg - command to invoke when calling /messageall
messageall.delay=1000 - delay for /messageall command
tpahereall.message.prefix=/msg - command to invoke when calling /tpahereall <private_message>
tpahereall.tpahere.command=/tpahere - command to invoke when calling /tpahereall, or /tpahereall <private_message>
giveaway.winmessage=Congratulations, %p you won %a$! - message that will be sent to chat after giveaway end. %p is substituted for winner's nickname, and %a for amount that the player won
```





## License
ChatUtils is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
