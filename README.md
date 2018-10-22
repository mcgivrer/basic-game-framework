# Basic Game Framework

[![Build Status](https://travis-ci.org/SnapGames/basic-game-framework.svg?branch=develop)](https://travis-ci.org/SnapGames/basic-game-framework) [![buddy pipeline](https://app.buddy.works/snapgames/basic-game-framework/pipelines/pipeline/155470/badge.svg?token=4df155595479af7dea413ea0c1d1f90219d384a3e89cf1ddd82b06e12b88a19d "buddy pipeline")](https://app.buddy.works/snapgames/basic-game-framework/pipelines/pipeline/155470) [![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FSnapGames%2Fbasic-game-framework.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2FSnapGames%2Fbasic-game-framework/refs/branch/develophttps://app.fossa.io/projects/git%2B${project.git.hostname}%2Fsnapgames%2Fbasic-game-framework?ref=badge_shield) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/6102158a5a724ce5a387f1436c34f71d)](https://app.codacy.com/app/mcgivrer/basic-game-framework?utm_source=github.com&utm_medium=referral&utm_content=SnapGames/basic-game-framework&utm_campaign=Badge_Grade_Dashboard) [![Github Releases](https://img.shields.io/github/release/snapgames/basic-game-framework.svg)](https://github.com/snapgames/basic-game-framework/releases/tag/1.0.0)

## Context

This Basic Game Framework on the Java platform is all you need to start coding a game on java with the JDK only.

## Compilation

Simply mavenized project with a :

```bash
 $> mvn clean install
```

## Execution

Run the app with :

```bash
 $> mvn exec:java
```

## Some options

On the command line, you can add some options:

| Attribute      | Name             | Description                          |
|----------------|------------------|--------------------------------------|
| `d/debug`      | debug mode       | set the level of visual debug (0->5) |
| `s/scale`      | pixel scale      | set the piwel scale (1->4)           |
| `w/width`      | window width     | set the window Width                 |
| `h/height`     | window height    | set the window Height                |
| `f/fps`        | rendered fps     | set the frame per second rate (30-60)|
| `k/fullscreen` | full screen mode | set window to fullscreen mode        |

Sample command line :

```bash
 $> java -jar basic-game-framework-0.0.1-SNAPSHOT.jar d=2 s=2 w=320 h=240
```

> This will start the demo with debug level (d) set to `2` and in a `320x240` (w x h) pixel widow scaled (s) by `2`.


During execution, you can activate some options :

| key   | Name             | Description                               |
|-------|------------------|-------------------------------------------|
| `D`   | debug mode       | switch level of visual debug (0->5)       |
| `F11` | full screen mode | switch between window and fullscreen mode |
| `F3`  | save screen      | save a screenshot of the current display  |

## This particular demo options

You can also push some keys to change visual content :

| key       | Description                          |
|-----------|--------------------------------------|
| `UP`      | Move big green square up             |
| `DOWN`    | Move big green square down           |
| `LEFT`    | Move big green square left           |
| `RIGHT`   | Move big green square right          |
| `PAGEUP`  | Add 10 enemies on the playground     |
| `PAGEDOWN`| Remove 10 enemies on the playground  |
| `DELETE`  | Remove all enemies on the playground |

## Some screenshot

![a small screenshot](./raw/docs/images/screenshot-001.png "a Sample screenshot of the display")

*figure 1 - a Sample screenshot of the current demo*

## To be continued ...

> That's all folks !

Fred from SnapGames.
