# EventHorizon
EventHorizon is an interactive game mode extension developed for the KSU Esports Minecraft server, aimed at boosting replayability and player engagement. It introduces random, server-wide events—categorized as positive, neutral, or negative—that challenge players to adapt and survive in both solo and team-based gameplay. These unpredictable occurrences create a dynamic and thrilling environment. EventHorizon can run as a standalone mode or integrate seamlessly with speedrun and competitive events for a fresh, ever-evolving experience.

## Installation & Setup:
The EventHorizon plugin is compatible with Minecraft version 1.21.4 and the corresponding PaperMC server.  

*EventHorizon assumes that **ALL** worlds are participating in EventHorizon, meaning that all gamerules, entities, etc. are potentially being modified on every loaded world.*  

For guidance on setting up a Paper server, refer to the official documentation:\
👉 [Getting Started with PaperMC](https://docs.papermc.io/paper/getting-started)  
Alternatively: [Setting up a local Minecraft Server](https://www.setup.md/guides/self-host/win)
\
\
<ins>Once your server is up and running:</ins>
- Download the latest EventHorizon plugin .jar file from the releases tab on the right. 
* Place the .jar file into your server's /plugins folder.
+ Start or restart your Minecraft server. The plugin should load automatically.

<ins>If you’d like to explore or modify the code:</ins>
- Clone the repository to your local machine.
* Make any desired changes.
+ Build the project using your preferred Java build tool.
- Use the generated .jar as outlined above.



## Dependencies
- FastAsyncWorldEdit
- PlaceholderAPI
- Recommended: TAB
  - Add this code to the TAB config file to use our scoreboard
```
scoreboards:
    scoreboard-1.20.3+:
      title: "<#E0B11E>EventHorizon</#FF0000>"
      display-condition: "%player-version-id%>=765;%bedrock%=false" # Only display it to players using 1.20.3+ AND NOT bedrock edition
      lines:
        - "<light_purple>Remaining Time:"
        - "<white>%eventhorizon_remainingtime_formatted%"
```

## Help?
- Check out the [Wiki](https://github.com/StrwbryPanda/EventHorizon/wiki) for usage instructions and event configurations.
