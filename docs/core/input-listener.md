# InputListener

*TODO propose a standard implementation of the KeyListener and then the MouseListener, MouseMoveListener and MouseWheelListener*

## Keyboard events

A first implementation will satisfy only the Java `KeyListener` interface to manage keyboards events.

An internal buffer of all keys will be maintained through the JDK `KeyEvent` flow.

- a new `getKey()` method will be added to check the buffer state for a particular key.
- a `action(App app)` method is added to delegate action management to the `App` class. 

```java
class InputListener(){
  // internal key buffer states
  - keys:int[65536]

  // Key event processing
  +keyPressed(KeyEvent e)  {}
  +keyReleased(KeyEvent e) {}
  +keyTyped(KeyEvent e)    {}
  +getKey(int keyCode) {}
  +action(App app){}
}
```

## Mouse events and moves

*TODO*
