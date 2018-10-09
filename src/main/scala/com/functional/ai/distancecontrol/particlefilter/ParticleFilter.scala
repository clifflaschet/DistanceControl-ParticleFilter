package com.functional.ai.distancecontrol.particlefilter

import breeze.stats.distributions.Uniform

import scala.collection.mutable.ListBuffer

object ParticleFilter {

  val iterations = 15d
  val deltaTime = 0.1d

  /**
    * Resamples the given particle based on the performed control u (i.e. estimated velocity of the car).
    * @param particle
    * @param time
    * @return
    */
  def resampleParticle(particle: Particle, time: Double): Particle ={

    particle.copy(xt = particle.xt - Car.sampleVelocity(time) * deltaTime)
  }

  /**
    * Computes the weight of a particle (i.e. P(z | x)).
    * @param particle
    * @param time
    * @return
    */
  def weighParticle(particle: Particle, time: Double): Particle ={

    val distanceSample = Car.DistanceSensor.sampleDistance(time)
    particle.copy(weight = Car.DistanceSensor.probabilityDistanceAtParticle(distanceSample, particle))
  }

  /**
    * Normalizes the weights of all particles in the given set with the provided normalization factor.
    * @param particle
    * @param normalizationFactor
    * @return
    */
  def normalizeWeight(particle: Particle, normalizationFactor: Double): Particle ={

    particle.copy(weight = particle.weight / normalizationFactor)
  }

}

/**
  *
  * @param steps
  * @param nrParticles
  */
class ParticleFilter(steps: Int, nrParticles: Int){

  import ParticleFilter._

  def estimate(): List[Seq[Particle]] = {

    //Initialize all particles, distributed uniformly between 0 meters (i.e. hitting vehicle ahead) and the car's start distance
    var particles: Seq[Particle] = new Uniform(0, Car.startDistance).sample(nrParticles).map(Particle(_, 1))

    //This variable is only used for plotting the results and can be ignored
    val particlesOverTime: ListBuffer[Seq[Particle]] = new ListBuffer

    //Iterate for 15 seconds in small time steps
    (1d to iterations by deltaTime).foreach {time =>

      particles = iterate(particles, time)

      particlesOverTime.append(particles)
    }

    particlesOverTime.toList
  }

  /**
    * Performs one iteration of the particle filter.
    * @param particles
    * @param time
    * @return
    */
  def iterate(particles: Seq[Particle], time: Double): Seq[Particle] ={

    //Resample particles based on their importance weight
    val sampledParticles = WeightedSample.weightedSample(particles, nrParticles)

    //Resample these particles based on the control (i.e. the velocity of the car, which in itself is noisy)
    val resampledParticles = sampledParticles.map(resampleParticle(_, time))

    //Compute the new importance weights of the re-sampled particles
    val reWeightedParticles = resampledParticles.map(weighParticle(_, time))

    //Normalize the weights of the re-sampled particles
    val normalizationFactor = reWeightedParticles.map(_.weight).sum
    val normalizedParticles = reWeightedParticles.map(normalizeWeight(_, normalizationFactor))

    normalizedParticles
  }
}