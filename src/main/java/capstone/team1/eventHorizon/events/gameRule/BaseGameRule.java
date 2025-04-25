package capstone.team1.eventHorizon.events.gameRule;

import capstone.team1.eventHorizon.EventHorizon;
import capstone.team1.eventHorizon.events.BaseEvent;
import capstone.team1.eventHorizon.events.EventClassification;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BaseGameRule<T> extends BaseEvent
{

    public BaseGameRule(EventClassification classification, String eventName)
    {
        super(classification, eventName);
    }

    @Override
    public void execute() {

    }

    @Override
    public void terminate() {

    }

    protected void applyGameRuleToWorld(World world, GameRule<T> gameRule, T value) {
        //initialValue = world.getGameRuleValue(gameRule);
        world.setGameRule(gameRule, value);
    }
    protected void applyGameRuleToAllWorlds(GameRule<T> gameRule, T value) {
        for (World world : EventHorizon.getPlugin().getServer().getWorlds()) {
            applyGameRuleToWorld(world, gameRule, value);
        }
    }

    protected void restoreGameRuleToWorld(World world, GameRule<T> gameRule) {
        //world.setGameRule(gameRule, initialValue);
    }

}
