# GameEntity

The game entity is a simple interface to easily manage the `GameObject`, even if their are not `GameObject` but some inheritant of this main object.

## Contract

The default interface signature is as following :

```java
interface GameEntity {
    + update(long dt):void;
    + render(Graphics2D g):void;
    + renderDebugInfo(Graphics2D g):void;
    + getId():int;
    + getName():String;
    + getBoundingBox():BoundingBox;
    + getLayer():int;
    + getPriority():int;
}
```

As you can see, the 2 of the 3 main game loop operations are represented:

### Update

The update operation must contains all operation to update the entity according to elapsed time since previous call.

```java
+ update(long dt):void;
```

### render

The render operation consists in drawing everything for the entity.

```java
+ render(Graphics2D g):void;
```
