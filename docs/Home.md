# Basic Game Framework

This is some pages explaining why this project exists, and what are the goals of such *framework".
In the scope of some tutorials and a book writing about Game Development on Java, I need to experiment some concepts and some patterns, that seems to be some common ones in the indie industrie.

All is around **App**, **GameState**, **GameObject**, some **Physic**'s and some internal services like **Collision** detection, **Sounds**, and later some **ParticleSystem** and a particle engine.

## Components

### App

- [Application class](Application)

### InputListener

- [InputListener](input-listener)

### Game Object

- [GameObject definition](gameobject)

### GameStateManager

*TODO Add GameState, AbstractGameState and GameStateManager to rules them all.*

- [GameStateManager](gsm)

## ECS

The **E**ntity **C**omponent **S**ystem is one of the best pattern in any game. here is how to implements and use it to bring new services to your game.


### PhysicEngine

*TODO Basic **Physic** computation to help game to be more realistic, with **Position**, **Velocity** and **Acceleration** and **Torque**.*

- [Physic System](system-physic)
- [Position component](component-position)
- [Velocity component](component-velocity)
- [Acceleration component](component-acceleration)

### Lights and Shadow

* TODO No game without lights and shadow. We will try to add ones.*

- [Light](light),
- [Shadow](shadow).

### Collision

*TODO Will provide a Collision detection system and response mechanism*

- [Collision System](system-collision)
- [Collider component](component-collider)
- [Colliding Response](behavior-colliding-response)

### Sounds

*TODO will insure music and sound sample play with loop and volume mechanism*

- [Sound System](system-sound)
- [Sample Component](component-sample)

### Particle System

*TODO create a ParticleSystem with ParticleEffect and Particle's*.

- [Particle system](system-particle)
- [Particle effect](component-particle)
- [Particle object](particle)
