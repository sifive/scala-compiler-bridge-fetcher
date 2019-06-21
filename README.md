# scala-compiler-bridge-fetcher

This is a very light project for fetching the Scala Compiler Bridge for [Bloop](https://github.com/scalacenter/bloop).
The use case is doing this fetch without asking Bloop to compile something
(we are separating dependency fetching from compilation).
It is intended for use with [Wit](https://github.com/sifive/wit) as a workaround since
Bloop will fetch the compiler bridge to a user's home directory only upon first compile.

This code is not recommended to be used as an example or for any purpose other than as a piece of Wit.

## Basic Use

### Compilation

```bash
sbt package
```

### Fetching Compiler Bridge

This assumes you have some coursier cache

First, you need the classpath of this repositories dependencies.
Note that these dependencies must be in sync with what's in built.sbt

```bash
CP=$(<coursier> fetch --cache <cache dir> --classpath ch.epfl.scala::bloop-backend:1.2.5 ch.epfl.scala::bsp4s:2.0.0-M3 io.get-coursier::lm-coursier:1.1.0-M14-4)
```

Then you can take the packaged jar and the above classpath to fetch a particular version of the compiler bridge.
You should set `coursier.cache` to match the above, as well as `user.home` to create a fake home directory where
the compiler bridge will be located.

```bash
java -Dcoursier.cache=<cache dir> -Duser.home=<desired location> -cp $CP:<path to packaged jar> sifive.ScalaCompilerBridgeFetcher <Scala Version>
```
