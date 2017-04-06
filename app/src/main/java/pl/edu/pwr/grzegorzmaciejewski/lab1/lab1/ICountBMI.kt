package pl.edu.pwr.grzegorzmaciejewski.lab1.lab1

/**
 * Created by stud on 15.03.2017.
 */

interface ICountBMI {
    val minMass: Float
    val maxMass: Float
    val minHeigh: Float
    val maxHeigh: Float
    fun isMassValid(mass: Float): Boolean
    fun isHeighValid(heigh: Float): Boolean
    fun countBMI(mass: Float, heigh: Float): Float

}
