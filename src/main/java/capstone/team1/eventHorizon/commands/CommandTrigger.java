package capstone.team1.eventHorizon.commands;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.utility.MsgUtility;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CommandTrigger {


    public static void run(CommandSender sender, String[] args) {
        MsgUtility.warning("Triggering event..." + Arrays.toString(args));
        EventHorizon.getEventManager().triggerEventByName(args[0]);
    }

    public static Collection<String> suggest(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Map<String, BaseEvent> registeredEvents = EventHorizon.getEventInitializer().getRegisteredEvents();
            Collection<String> eventNames = registeredEvents.keySet();
            return StringUtil.copyPartialMatches(args[0], eventNames, new ArrayList<>());
        }
        return new ArrayList<>();
    }
}