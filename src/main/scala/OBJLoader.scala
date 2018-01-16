import org.joml.Vector3f

import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex

object OBJLoader {

    val VERTICES_REGEX: Regex = """v\s([+-]?\d*\.?\d*)\s([+-]?\d*\.?\d*)\s([+-]?\d*\.?\d*)""".r
    val NORMAL_REGEX: Regex = """vn\s([+-]?\d*\.?\d*)\s([+-]?\d*\.?\d*)\s([+-]?\d*\.?\d*)""".r
    val FACE_REGEX: Regex = """f\s(\d+)\/\/(\d+)\s(\d+)\/\/(\d+)\s(\d+)\/\/(\d+)""".r

    def loadObjModel(filePath: String): Model = {
        var vertices: ArrayBuffer[Vector3f] = ArrayBuffer[Vector3f]()
        var normals: ArrayBuffer[Vector3f] = ArrayBuffer[Vector3f]()
        val objStr: String = FileUtils.readFromFile(filePath)
        VERTICES_REGEX.findAllMatchIn(objStr).foreach(vertLine =>
            vertices += new Vector3f(vertLine.subgroups.head.toFloat, vertLine.subgroups(1).toFloat, vertLine.subgroups(2).toFloat)
        )
        NORMAL_REGEX.findAllMatchIn(objStr).foreach(normalLine =>
            normals += new Vector3f(normalLine.subgroups.head.toFloat, normalLine.subgroups(1).toFloat, normalLine.subgroups(2).toFloat)
        )
        var modelData: ArrayBuffer[Float] = ArrayBuffer[Float]()
        FACE_REGEX.findAllMatchIn(objStr).foreach(faceLine =>
            (0 to 2).foreach(i => {
                val vertData: Vector3f = vertices(faceLine.subgroups(i * 2).toInt - 1)
                val normalData: Vector3f = normals(faceLine.subgroups(i * 2 + 1).toInt - 1)
                modelData += vertData.x
                modelData += vertData.y
                modelData += vertData.z
                modelData += normalData.x
                modelData += normalData.y
                modelData += normalData.z
            })
        )
        modelData.grouped(6).foreach(println)
        new Model(modelData.toArray)
    }

}
