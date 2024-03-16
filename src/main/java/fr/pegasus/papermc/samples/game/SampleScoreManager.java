package fr.pegasus.papermc.samples.game;

import fr.pegasus.papermc.scores.ScoreManager;
import fr.pegasus.papermc.teams.Team;

import java.util.HashMap;
import java.util.Map;

public class SampleScoreManager implements ScoreManager {
    @Override
    public Map<Team, Integer> getScores() {
        return new HashMap<>();
    }
}
