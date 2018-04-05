package com.functional.ai.particlefilter

import breeze.stats.distributions.Gaussian

object Car {

  val startDistance = 100 //Meters
  val minDistance = 5 //Meters

  //Velocity distribution
  val velocityMean = 13.88 //Meters/second
  val velocitySigma = 1.66 //Meters/second
  val velocityDistribution = new Gaussian(velocityMean, velocitySigma)

  /**
    * Sample the velocity at the given time step, with noise.
    * @param time
    * @return
    */
  def sampleVelocity(time: Double): Double ={
    if(trueDistanceToVehicleAtTime(time) <= minDistance) 0d else velocityDistribution.sample()
  }

  /**
    * Computes the true distance to the vehicle ahead (which is what we are trying to estimate with the particle filter)
    * @param time
    * @return
    */
  def trueDistanceToVehicleAtTime(time: Double) ={
    //The car stops abruptly when three meters from the vehicle ahead, hence the minimum is bound at 3.
    Math.max(startDistance - (velocityMean * time), 3)
  }

  /**
    * The car has a sensor that estimates the distance to the vehicle ahead. The sensor's distance estimates are noisy.
    */
  object DistanceSensor{

    //The noise of of the car's distance sensor
    val distanceSigma = 8 //Meters

    /**
      * Using the sensor, sample the distance to the vehicle ahead at the given time step, with noise.
      * @param time
      * @return
      */
    def sampleDistance(time: Double): Double ={
      new Gaussian(trueDistanceToVehicleAtTime(time), distanceSigma).sample()
    }

    def probabilityDistanceAtParticle(sensorDistance: Double, particle: Particle) ={

      val g = Gaussian(particle.xt, distanceSigma)
      g.pdf(sensorDistance)
    }
  }

}
