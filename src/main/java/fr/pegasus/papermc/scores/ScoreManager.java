package fr.pegasus.papermc.scores;

import fr.pegasus.papermc.teams.Team;

import java.util.Map;

public interface ScoreManager {
    Map<Team, Integer> getScores();
}
