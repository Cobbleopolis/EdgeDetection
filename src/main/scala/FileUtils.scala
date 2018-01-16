import ShaderUtils.getClass

import scala.io.Source

object FileUtils {

  def readFromFile(name: String): String = Source.fromInputStream(getClass.getResourceAsStream(name)).mkString

  def readFileByLine(name: String, f: String => Unit): Unit = Source.fromURL(getClass.getResource(name)).getLines.foreach(f)

}
