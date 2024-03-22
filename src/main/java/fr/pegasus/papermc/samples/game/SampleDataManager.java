package fr.pegasus.papermc.samples.game;

import com.google.common.collect.Sets;
import fr.pegasus.papermc.teams.Team;
import fr.pegasus.papermc.teams.loaders.DataManager;
import fr.pegasus.papermc.tools.PegasusRandom;
import fr.pegasus.papermc.utils.PegasusPlayer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SampleDataManager implements DataManager {

    private final int fakeTeamsCount;
    private final int fakeTeamsPlayerCount;

    public SampleDataManager(int fakeTeamsCount, int fakeTeamsPlayerCount) {
        this.fakeTeamsCount = fakeTeamsCount;
        this.fakeTeamsPlayerCount = fakeTeamsPlayerCount;
    }

    @Override
    public Set<Team> loadTeams() {
        Set<Team> teams = new HashSet<>();
        teams.add(new Team("T1", "Team 1", Sets.newHashSet(new PegasusPlayer("Xen0Xys"))));
        PegasusRandom pRandom = new PegasusRandom();
        for(int i = 0; i < this.fakeTeamsCount; i++){
            Set<PegasusPlayer> pPlayers = new HashSet<>();
            for(int j = 0; j < this.fakeTeamsPlayerCount; j++)
                pPlayers.add(new PegasusPlayer(pRandom.randomString(20)));
            teams.add(new Team("T" + (i + 2), "Team " + (i + 2), pPlayers));
        }
        return teams;
    }

    @Override
    public void uploadScores(Map<Team, Integer> scores) {

    }
}
