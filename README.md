# Pegasus Framework

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
