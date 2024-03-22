package fr.pegasus.papermc.games.instances.enums;

public enum InstanceStates {
    CREATED, // When the instance is created and the schematic is pasted
    READY, // When the instance is ready to be started after the players are affected and teleported
    PRE_STARTED, // When the start countdown is running
    STARTED, // When the instance is started
    ROUND_PRE_STARTED, // When the round start countdown is running
    ROUND_STARTED, // When the round is started
    ROUND_ENDED, // When the round is ended
    ENDED, // When the instance is ended
    CLOSED, // When the instance is closed, players are unaffected
}
