# The Game State and its Manager

## Introduction

The **GameStateManager** is a concept used to add multiple gameplay to a game without changing all the core process, but integrate a state machine to switch between those states.

All start by a contract where is defined call structure of a state. We are going to call this a **GameState**.

The **GameState** must drive any implementation to provide the necessary steps to create, initialize, run, and destroy a gameplay. 

A default implementation will implements all the internal mechanics of State switching, this will be the **AbstractGameState**. 

But any gameplay processing will be delegated to a specific **GameState** implementation.

## GameState

If you remember the game loop operations, the main method of the **App** class, the steps of the game execution are the following:

```java
void run(){
  initialize();
  while(!exit){
    input();
    update();
    render();
    drawToBuffer();
  }
  dispose();
}
```

If we extract the `run()` method from the main operation, we could delegate the loop steps to another class. This will provide the contract to apply in our game state to be implemented in a new gameplay.

```
  GameState
    + initialize()
    + input()
    + update()
    + render()
    + dispose()
```

To identify a specific state, we will need an internal ID and to be human readable, a name. So we could add some new information :
```
  GameState
    + id
    + name
    + initialize()
    + input()
    + update()
    + render()
    + dispose
```



This core interface will provide the following methods signatures:

```java
public interface GameState{
  // return name of this state
  String getName();
  // return id of the instance.
  long getId();
  // initialization of this gameplay
  void initialize(App app);
  // manage user input
  void input(App app, InputListener il);
  // update gameplay mechanic
  void update(App app, long dt);
  // render this gameplay
  void render(App app, Graphics2D g);
  // release all resources for this gameplay
  void dispose(App app);
}
```

## AbstractGameState

The abstract game state will implements all the core processing of any **GameState**.

TODO

## GameStateManager

The **GameStateManager** is an internal service providing the manager initialize, load, and switch between existing game states.

This service will provide:

- an **initialize()** method, to define all existing game states, and which one is the entry point,
- a **switch()** method to switch from the current state to a new one,
- a **create()** method to load all needed resources for a state,
- an **input()** method to delegate input management from game loop to the current active state,
- an **update()** method to delegate update process from game loop to the current active state,
- a **render()** method to delegate render operation from game loop to the current active state,
- a **dispose()** method to release resource at game exit time.
- a **load()** method will be added in a following chapter to load all GameState classes dynamically from a `states.xml` file.

The corresponding class signature would be:

```java
public class GameStateManager{
  // initialization of the GameStateManager instance
  public void initialize(App app) {}
  // load states implementation classes from a states.xml file
  public void load(App app) {}
  // switch to state named name.
  public void switch(String name) {}
  // create the state previously switched to
  public void create(App app) {}
  // Delegate input management to active state
  public void input(App app, InputListener il) {}
  // Delegate update processing to active state
  public void update(App app, long dt) {}
  // Delegate rendering operations to active state
  public void render(App app, Graphics2D g) {}
  // Dispose all resources for the GameStateManager.
  public void dispose(App app){}
}
```

