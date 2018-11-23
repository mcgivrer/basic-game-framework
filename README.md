# Basic Game Framework

[![Build Status](https://travis-ci.org/SnapGames/basic-game-framework.svg?branch=develop)](https://travis-ci.org/SnapGames/basic-game-framework) [![buddy pipeline](https://app.buddy.works/fredericdelorme/basic-game-framework/pipelines/pipeline/158190/badge.svg?token=05a173644a4977bbce08533e358dc272005af3d915e8c36ceba53f1bd0228c50 "buddy pipeline")](https://app.buddy.works/fredericdelorme/basic-game-framework/pipelines/pipeline/158190) [![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FSnapGames%2Fbasic-game-framework.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2FSnapGames%2Fbasic-game-framework/refs/branch/develophttps://app.fossa.io/projects/git%2B${project.git.hostname}%2Fsnapgames%2Fbasic-game-framework?ref=badge_shield) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/6102158a5a724ce5a387f1436c34f71d)](https://app.codacy.com/app/mcgivrer/basic-game-framework?utm_source=github.com&utm_medium=referral&utm_content=SnapGames/basic-game-framework&utm_campaign=Badge_Grade_Dashboard) [![Known Vulnerabilities](https://snyk.io/test/github/SnapGames/basic-game-framework/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/SnapGames/basic-game-framework?targetFile=pom.xml) [![Github Releases](https://img.shields.io/github/release/SnapGames/basic-game-framework.svg)](https://github.com/SnapGames/basic-game-framework/releases/tag/0.0.2) [ ![Download](https://api.bintray.com/packages/snapgames/basic-game-framework/basic-game-framework/images/download.svg) ](https://bintray.com/snapgames/basic-game-framework/basic-game-framework/_latestVersion) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

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

| Attribute      | Name             | Description                           |
|----------------|------------------|---------------------------------------|
| `d/debug`      | debug mode       | set the level of visual debug (0->5)  |
| `s/scale`      | pixel scale      | set the piwel scale (1->4)            |
| `w/width`      | window width     | set the window Width                  |
| `h/height`     | window height    | set the window Height                 |
| `fps`          | rendered fps     | set the frame per second rate (30-60) |
| `f/fullscreen` | full screen mode | set window to fullscreen mode         |

### Run the demo

1. the default command line :

```bash
 $> java -jar basic-game-framework-0.0.1-SNAPSHOT-shaded.jar d=2 s=2 w=320 h=240
```

2. If you are a fan of the maven build tool, you can have a try with

```bash
$> mvn exec:java
```

3. On windows platform, you can also start the demo with

```cmd
C:\> "Basic Game Framework.exe"
```

> This will start the demo with debug level (d) set to `2` and in a `320x240` (w x h) pixel widow scaled (s) by `2`.

## Keymaps

During execution, you can activate some options :

| key   | Name             | Description                               |
|-------|------------------|-------------------------------------------|
| `D`   | debug mode       | switch level of visual debug (0->5)       |
| `F11` | full screen mode | switch between window and fullscreen mode |
| `F3`  | save screen      | save a screenshot of the current display  |

### This particular demo options

You can also push some keys to change visual content :

| key        | Description                          |
|------------|--------------------------------------|
| `UP`       | Move big green square up             |
| `DOWN`     | Move big green square down           |
| `LEFT`     | Move big green square left           |
| `RIGHT`    | Move big green square right          |
| `PAGEUP`   | Add 100 enemies on the playground     |
| `PAGEDOWN` | Remove 100 enemies on the playground  |
| `DELETE`   | Remove all enemies on the playground |

### Some screenshot

- *A full debug display mode for this capture*

![a small screenshot](https://raw.githubusercontent.com/SnapGames/basic-game-framework/develop/docs/images/screenshot-001.png "a Sample screenshot of the display")

- *462 small objects displayed at 100 FPS*

![a small screenshot](https://raw.githubusercontent.com/SnapGames/basic-game-framework/develop/docs/images/screenshot-002.png "a Sample screenshot of the display")

- Mode pause activated.

![a small screenshot](https://raw.githubusercontent.com/SnapGames/basic-game-framework/develop/docs/images/screenshot-003.png "a Sample screenshot of the display")


> That's all folks !

Fred from SnapGames.
