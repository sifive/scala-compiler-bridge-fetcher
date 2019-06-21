// See LICENSE.SiFive for license details.

package sifive

import java.io.File
import sbt.internal.util.ConsoleLogger

// This code is in this repo
import sbt.internal.inc.BridgeProvider

/** Scala Compiler Bridge Fetcher for Bloop
  *
  * This is intended to fetch the Scala compiler bridge for use by Bloop
  * This is not a good way to do this and not suggested as an example
  */
object ScalaCompilerBridgeFetcher {
  def main(args: Array[String]): Unit = {
    require(args.size == 1, "Usage: ScalaCompilerBridgeFetcher <Scala Version>")

    // Create destination directory
    val destDir = new File(bloop.io.Paths.bloopCacheDir.toFile, "components")
    require(!destDir.isFile, s"Destination directory '$destDir' must not be a file!")
    destDir.mkdirs()

    val log = ConsoleLogger()
    val version = args(0)
    BridgeProvider.getCompilerBridge(destDir, log, version)
  }
}
