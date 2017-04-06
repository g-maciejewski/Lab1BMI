package pl.edu.pwr.grzegorzmaciejewski.lab1.lab1

/**
 * Created by stud on 15.03.2017.
 */

class CountBMIForKgM : ICountBMI {


    override fun isHeighValid(heigh: Float): Boolean {
        return heigh > minHeigh && heigh < maxHeigh
    }

    override fun countBMI(mass: Float, heigh: Float): Float {
        if (isMassValid(mass) && isHeighValid(heigh))
            return mass / (heigh * heigh)
        else
            throw IllegalArgumentException()
    }

    override fun isMassValid(mass: Float): Boolean {
        return mass > minMass && mass < maxMass
    }


    override val minMass = 10f
    override val maxMass = 250f
    override val maxHeigh = 2.5f
    override val minHeigh = 0.5f


}
