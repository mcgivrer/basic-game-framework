# App

This is the Main class with loop and CLI integration.

This main class implements `Runnable` and `KeyListener` interface to provide a thread instance for execution, and a Key input interface to process `KeyEvent` messages. This remains in the standard JDK with java 1.8 compatibility level.

This implements a `GameLoop` pattern with a maintained frame rate mechanism, providing `initialization`, `update` and `render`'ing features.

the App interface is :

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
  -draw2Screen()
  -clearRenderingBuffer()
  -parseArgs(args:String[])

  // Loop mechanism
  +run()
  +initialize()
  +update(long dt)
  +render(graphics2D g)

  // Key event processing
  +keyPressed(KeyEvent e)
  +keyReleased(KeyEvent e)
  +keyTyped(KeyEvent e)

  //entry Point
  +main(args:String[])
}
```
*Pseudo code of App signature*