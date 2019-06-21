// See LICENSE.sbt for license details.
// See LICENSE.SiFive for license details.

package sbt.internal.inc

import java.io.File

import sbt.io.IO
import sbt.io.syntax._
import sbt.librarymanagement._
import sbt.librarymanagement.ivy._
import sbt.util.Logger
import xsbti.compile.CompilerBridgeProvider

import lmcoursier.{CoursierConfiguration, CoursierDependencyResolution}

// This was adapted from https://github.com/scalacenter/zinc/blob/2425db167867cd34a191391142a7c111b6d73330/internal/zinc-ivy-integration/src/test/scala/sbt/internal/inc/BridgeProviderSpecification.scala
object BridgeProvider {
  def currentBase: File = new File(".")
  def currentTarget: File = currentBase / "target" / "ivyhome"
  def currentManaged: File = currentBase / "target" / "lib_managed"

  val resolvers = Vector(ZincComponentCompiler.LocalResolver, Resolver.mavenCentral)

  def coursierConfig(log: Logger): CoursierConfiguration =
    CoursierConfiguration()
    .withLog(log)
    .withResolvers(resolvers)
  def coursierResolution(log: Logger): DependencyResolution = CoursierDependencyResolution(coursierConfig(log))

  // Place where we store the compiled and installed bridges for every Scala version
  def secondaryCacheDirectory: File = file("target").getAbsoluteFile./("zinc-components")

  def getZincProvider(targetDir: File, log: Logger): CompilerBridgeProvider = {
    val lock = ZincComponentCompiler.getDefaultLock
    val secondaryCache = Some(secondaryCacheDirectory)
    val componentProvider = ZincComponentCompiler.getDefaultComponentProvider(targetDir)
    val manager = new ZincComponentManager(lock, componentProvider, secondaryCache, log)
    val dependencyResolution = coursierResolution(log)
    ZincComponentCompiler.interfaceProvider(manager, dependencyResolution, currentManaged)
  }

  def getCompilerBridge(targetDir: File, log: Logger, scalaVersion: String): File = {
    val provider = getZincProvider(targetDir, log)
    val scalaInstance = provider.fetchScalaInstance(scalaVersion, log)
    val bridge = provider.fetchCompiledBridge(scalaInstance, log)
    bridge
  }

  def scalaInstance(scalaVersion: String,
                    targetDir: File,
                    logger: Logger): xsbti.compile.ScalaInstance = {
    val provider = getZincProvider(targetDir, logger)
    provider.fetchScalaInstance(scalaVersion, logger)
  }
}
