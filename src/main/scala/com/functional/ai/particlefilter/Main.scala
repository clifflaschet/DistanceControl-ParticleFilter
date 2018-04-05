package com.functional.ai.particlefilter

object Main extends App{

  val particlesOverTime = new ParticleFilter(15, 500).estimate()

  val filterResults = for(
    p <- particlesOverTime
  ) yield p.map(_.xt).mkString(",")

  //Write results to file for plotting
  writeCSV("filterResults.txt", filterResults)

  //Write actual car distance to file
  val carPosition = for{
    t <- 1d to ParticleFilter.iterations by ParticleFilter.deltaTime
  }yield Car.trueDistanceToVehicleAtTime(t)

  writeCSV("carPosition.txt", List(carPosition.mkString(",")))

  def writeCSV(fileName: String, data: List[String]): Unit ={

    import java.io._
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))
    for (x <- data) {
      writer.write(x + "\n")
    }
    writer.close()
  }
}
