package fr.pegasus.papermc.samples.game;

import com.google.common.collect.Sets;
import fr.pegasus.papermc.teams.Team;
import fr.pegasus.papermc.teams.loaders.DataManager;
import fr.pegasus.papermc.utils.PegasusPlayer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SampleDataManager implements DataManager {
    @Override
    public Set<Team> loadTeams() {
        Set<Team> teams = new HashSet<>();
        teams.add(new Team("T1", "Team 1", Sets.newHashSet(new PegasusPlayer("Xen0Xys"))));
        return teams;
    }

    @Override
    public void uploadScores(Map<Team, Integer> scores) {

    }
}
