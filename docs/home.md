# Basic Game Framework

This is some pages explaining why this project exists, and what are the goals of such *framework".
In the scope of some tutorials and a book writing about Game Development on Java, I need to experiment some concepts and some patterns, that seems to be some common ones in the indie industrie.

All is around **App**, **GameState**, **GameObject**, some **Physic**'s and some internal services like **Collision** detection, **Sounds**, and later some **ParticleSystem** and a particle engine.

## Components

### App

- [Application class](core/app.md)

### InputListener

- [InputListener](core/input-listener.md)

### Game Object

- [GameObject definition](entity/gameobject.md)

### GameStateManager

*TODO Add GameState, AbstractGameState and GameStateManager to rules them all.*

- [GameStateManager](core/gsm.md)

## ECS

The **E**ntity **C**omponent **S**ystem is one of the best pattern in any game. here is how to implements and use it to bring new services to your game.


### PhysicEngine

*TODO Basic **Physic** computation to help game to be more realistic, with **Position**, **Velocity** and **Acceleration** and **Torque**.*

- [Physic System](physic/system-physic.md)
- [Position component](physic/component-position.md)
- [Velocity component](physic/component-velocity.md)
- [Acceleration component](physic/component-acceleration.md)

### Lights and Shadow

* TODO No game without lights and shadow. We will try to add ones.*

- [Light](effects/light.md),
- [Shadow](effects/shadow.md).

### Collision

*TODO Will provide a Collision detection system and response mechanism*

- [Collision System](collision/system-collision.md)
- [Collider component](collision/component-collider.md)
- [Colliding Response](collision/behavior-colliding-response.md)

### Sounds

*TODO will insure music and sound sample play with loop and volume mechanism*

- [Sound System](sounds/system-sound.md)
- [Sample Component](sounds/component-sample.md)

### Particle System

*TODO create a ParticleSystem with ParticleEffect and Particle's*.

- [Particle system](effects/particle/system-particle.md)
- [Particle effect](effects/particle/component-particle.md)
- [Particle object](effects/particle/particle.md)
