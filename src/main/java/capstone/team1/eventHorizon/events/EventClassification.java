package capstone.team1.eventHorizon.events;

/**
 * Represents the classification types for events in the EventHorizon plugin.
 * Events are categorized based on their impact on gameplay experience.
 */
public enum EventClassification {
    /**
     * Represents beneficial events that provide advantages to players,
     * such as buffs, resource gains, or helpful mob spawns.
     */
    POSITIVE,

    /**
     * Represents challenging events that create obstacles or difficulties for players,
     * such as debuffs, hostile mob spawns, or item losses.
     */
    NEGATIVE,

    /**
     * Represents events that neither distinctly benefit nor hinder players,
     * but may alter gameplay in interesting ways.
     */
    NEUTRAL
}