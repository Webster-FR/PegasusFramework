# Pegasus Framework

## Getting started

### Creating instance class

Start by creating an instance class **with this constructor**, and implementing all lifecycle methods.

All the instance logic goes on this class !

```java
public class SampleInstance extends Instance {
    public SampleInstance(int id, JavaPlugin plugin, CommonOptions commonOptions, InstanceOptions instanceOptions, ScoreManager scoreManager) {
        super(id, plugin, commonOptions, instanceOptions, scoreManager);
    }
}
```

### Setting up instance behavior

#### Main plugin class

Make sure that your plugin extends of PegasusPlugin.

```java
public class GameSamplePlugin extends PegasusPlugin{
    @Override
    public void onEnable(){
        super.onEnable();
        // The initialization code goes here
    }
}
```
#### Creating the game world

To create the game world, just create a new WorldBuilder, and set on it all the world options that you want !

After that, just call the ServerManager#addGameWorld(WorldBuilder) to get the newly created game world.

_Note : If the game world already exists, the given settings will overwrite the older._

```java
public class GameSamplePlugin extends PegasusPlugin{
    @Override
    public void onEnable(){
        super.onEnable();
        WorldBuilder gameWorldBuilder = new WorldBuilder("pegasus_sample");
        gameWorldBuilder.setGameMode(GameMode.CREATIVE)
                .setDifficulty(Difficulty.PEACEFUL)
                .addGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
                .addGameRule(GameRule.DO_WEATHER_CYCLE, false)
                .setWorldTime(6000)
                .addPrevention(WorldPreventions.ALL)
                .addPrevention(WorldPreventions.PREVENT_PVP);
        PegasusWorld gameWorld = this.getServerManager().addGameWorld(gameWorldBuilder);
    }
}
```

#### Creating the game settings

Start by creating a new OptionsBuilder, and set all the settings you want.

_Note : Some settings are mandatory to the game be runnable!_

After that, you can get the GameManager by calling the ServerManager#createGameManager(DataManager, OptionsBuilder, ScoreManager) method.

```java
public class GameSamplePlugin extends PegasusPlugin{
    @Override
    public void onEnable(){
        super.onEnable();
        // Game world creation code...
        OptionsBuilder optionsBuilder = new OptionsBuilder()
                .setGameType(GameType.SOLO)
                .setWorld(gameWorld)
                .setInstanceClass(SampleInstance.class)
                .setRoundDurations(List.of(20))
                .setSpawnPoints(List.of(new RelativeLocation(0.5, 0, 0.5, 90, 0)))
                .setSchematic(new Schematic(this, "instances_test", SchematicFlags.COPY_BIOMES))
                .setPreAllocatedInstances(1);
        gameManager = this.getServerManager().createGameManager(new SampleDataManager(), optionsBuilder, new SampleScoreManager());
    }
}
```

#### Starting the game

To start the game, just call the GameManager#start() method.

## Lifecycles

### Instance lifecycle and behavior

| Instance state    | Player game mode | Is player frozen |
|-------------------|------------------|------------------|
| CREATED           | Not on instance  | Not on instance  |
| READY             | SPECTATOR        | NO               |
| PRE_STARTED       | SPECTATOR        | NO               |
| STARTED           | SPECTATOR        | NO               |
| ROUND_PRE_STARTED | ADVENTURE        | YES              |
| ROUND_STARTED     | SURVIVAL         | NO               |
| ROUND_ENDED       | SPECTATOR        | NO               |
| ENDED             | SPECTATOR        | NO               |
| CLOSED            | Not on instance  | Not on instance  |

### Instances Manager lifecycle

| Instance Manager state | Changed when all instances are in state |
|------------------------|-----------------------------------------|
| READY                  | READY                                   |
| STARTED                | STARTED                                 |
| ENDED                  | CLOSED                                  |

### Game Manager lifecycle

| Game Manager state | Changed when...                                          |
|--------------------|----------------------------------------------------------|
| CREATED            | when new instance created                                |
| STARTED            | when start method is called                              |
| ENDED              | when stop method is called or Instances Manager is ENDED |

## How to get which data ?

### Instance
- Teams and players : Instance#getPlayerManager()
- Score : Instance#getScoreManager()
