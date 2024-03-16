package fr.pegasus.papermc.teams.loaders;

import fr.pegasus.papermc.teams.Team;

import java.util.Map;
import java.util.Set;

public interface DataManager {
    Set<Team> loadTeams();
    void uploadScores(Map<Team, Integer> scores);
}
