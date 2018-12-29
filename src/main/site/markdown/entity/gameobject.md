# GameObject

The Object class managed by Main class App. support both simple rectangle and images.

See below the UML implementation like :

```Java
class GameObject extends GameEntity {

  // identification
  +id:long
  +name:string

  // geometry
  +x:int
  +y:int
  +width:int
  +height:int
  +boundingBox:Rectangle // will be a specific object later.

  // physic attributes
  +dx:float
  +dy:float
  +friction:float
  +elasticity:float

  //loop actions implementation
  +update(dt:long)
  +render(g:Graphics2D)

  // Builder pattern
  +builder(name:String)

  // add accessors here ...
}
```
*Pseudo code of GameObject signature*
