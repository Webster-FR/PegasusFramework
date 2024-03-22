package game_manager;

import com.google.common.collect.Sets;
import fr.pegasus.papermc.games.GameManager;
import fr.pegasus.papermc.games.instances.GameType;
import fr.pegasus.papermc.teams.Team;
import fr.pegasus.papermc.utils.PegasusPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTests {

    private GameManager gameManager;
    private Method isEmptyTeamMethod;
    private Method checkMinimumTeamsMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        gameManager = new GameManager();
        isEmptyTeamMethod = GameManager.class.getDeclaredMethod(
                "isEmptyTeam", Set.class);
        isEmptyTeamMethod.setAccessible(true);
        checkMinimumTeamsMethod = GameManager.class.getDeclaredMethod(
                "checkMinimumTeams", Set.class, GameType.class);
        checkMinimumTeamsMethod.setAccessible(true);
    }

    @Nested
    class IsEmptyTests{

        private Set<Team> teams;

        @BeforeEach
        public void setUp() {
            teams = new HashSet<>();
            teams.add(new Team("T1", "Team 1", Sets.newHashSet(new PegasusPlayer("Player 1"), new PegasusPlayer("Player 2"))));
            teams.add(new Team("T2", "Team 2", Sets.newHashSet(new PegasusPlayer("Player 3"), new PegasusPlayer("Player 4"))));
        }

        @Test
        public void testWithoutEmptyTeams() throws InvocationTargetException, IllegalAccessException {
            boolean value = (boolean) isEmptyTeamMethod.invoke(gameManager, teams);
            assertFalse(value);
        }

        @Test
        public void testWithEmptyTeam() throws InvocationTargetException, IllegalAccessException {
            teams.add(new Team("T3", "Team 3", new HashSet<>()));
            boolean value = (boolean) isEmptyTeamMethod.invoke(gameManager, teams);
            assertTrue(value);
        }

        @Test
        public void testWithOnePlayerTeam() throws InvocationTargetException, IllegalAccessException {
            teams.add(new Team("T3", "Team 3", Sets.newHashSet(new PegasusPlayer("Player 5"))));
            boolean value = (boolean) isEmptyTeamMethod.invoke(gameManager, teams);
            assertFalse(value);
        }
    }

    @Nested
    public class CheckMinimumTeamsTests{

        @Nested
        public class Empty{

            private Set<Team> teams;

            @BeforeEach
            public void setUp() {
                teams = new HashSet<>();
            }

            @Test
            public void testWithSolo() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.SOLO);
                assertFalse(value);
            }

            @Test
            public void testWithTeamOnly() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.TEAM_ONLY);
                assertFalse(value);
            }

            @Test
            public void testWithTeamVsTeam() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.TEAM_VS_TEAM);
                assertFalse(value);
            }

            @Test
            public void testWithFFA() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.FFA);
                assertFalse(value);
            }
        }

        @Nested
        public class One{

            private Set<Team> teams;

            @BeforeEach
            public void setUp() {
                teams = new HashSet<>();
                teams.add(new Team("T1", "Team 1", Sets.newHashSet(new PegasusPlayer("Player 1"), new PegasusPlayer("Player 2"))));
            }

            @Test
            public void testWithSolo() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.SOLO);
                assertTrue(value);
            }

            @Test
            public void testWithTeamOnly() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.TEAM_ONLY);
                assertTrue(value);
            }

            @Test
            public void testWithTeamVsTeam() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.TEAM_VS_TEAM);
                assertFalse(value);
            }

            @Test
            public void testWithFFA() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.FFA);
                assertTrue(value);
            }
        }

        @Nested
        public class Two{

            private Set<Team> teams;

            @BeforeEach
            public void setUp() {
                teams = new HashSet<>();
                teams.add(new Team("T1", "Team 1", Sets.newHashSet(new PegasusPlayer("Player 1"), new PegasusPlayer("Player 2"))));
                teams.add(new Team("T2", "Team 2", Sets.newHashSet(new PegasusPlayer("Player 3"), new PegasusPlayer("Player 4"))));
            }

            @Test
            public void testWithSolo() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.SOLO);
                assertTrue(value);
            }

            @Test
            public void testWithTeamOnly() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.TEAM_ONLY);
                assertTrue(value);
            }

            @Test
            public void testWithTeamVsTeam() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.TEAM_VS_TEAM);
                assertTrue(value);
            }

            @Test
            public void testWithFFA() throws InvocationTargetException, IllegalAccessException {
                boolean value = (boolean) checkMinimumTeamsMethod.invoke(gameManager, teams, GameType.FFA);
                assertTrue(value);
            }
        }
    }
}
