package com.functional.ai.particlefilter

import scala.annotation.tailrec
import scala.util.Random

object WeightedSample {

  /**
    * Takes a weighted random sample of n particles from the provided set of particles, according to the weights of the particles.
    * @param particles
    * @param n
    * @return
    */
  def weightedSample(particles: Seq[Particle], n: Int): Seq[Particle] ={

    val random = new Random()
    val weightSum = particles.map(_.weight).sum
    val cumulativeWeightedParticles = WeightedSample.cumulativeWeights(particles)

    (1 to n).map(_ => WeightedSample.search(random.nextDouble() * weightSum, cumulativeWeightedParticles))
  }

  /**
    * Selects the first particle in the sequence that matches the randomly chosen cumulative weight.
    * @param randomCumulative
    * @param cdf
    * @return
    */
  @tailrec
  def search(randomCumulative: Double, cdf: Seq[(Particle, Double)]): Particle ={

    if(randomCumulative <= cdf.head._2)
      cdf.head._1
    else
      search(randomCumulative, cdf.tail)
  }

  /**
    * Transforms the weights of a set of particles to be cumulative.
    * @param particles
    * @return
    */
  def cumulativeWeights(particles: Seq[Particle]): Seq[(Particle, Double)] ={
    particles.map{
      var cumulative = 0d
      p => cumulative += p.weight; (p, cumulative)
    }
  }
}
