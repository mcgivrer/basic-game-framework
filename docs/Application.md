# App

This is the Main class with loop and CLI integration.

This main class implements `Runnable` and `KeyListener` interface to provide a thread instance for execution, and a Key input interface to process `KeyEvent` messages. This remains in the standard JDK with java 1.8 compatibility level.

This implements a `GameLoop` pattern with a maintained frame rate mechanism, providing `initialization`, `update` and `render`'ing features.

```java
void run(){
  initialize();
  while(!exit){
    input();
    update();
    render();
    drawToScreen();
  }
  dispose();
}
```



the App class will provide the following structure :

```Java
class App{
  // internals
  -title:String
  -thread:Thread

  // Constructor
  +App(title:String)

  // Graphics
  -buffer:BufferedImage
  -viewport:Rectangle
  -drawToScreen()
  -clearRenderingBuffer()
  -parseArgs(args:String[])

  // Loop mechanism
  +run()
  +initialize()
  +update(long dt)
  +render(graphics2D g)

  //entry Point
  +main(args:String[])
}
```
*Pseudo code of App signature*