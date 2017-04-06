package pl.edu.pwr.grzegorzmaciejewski.lab1.lab1

/**
 * Created by PanG on 28.03.2017.
 */

class CountBMIForLbIn : ICountBMI {
    override val minMass = 22.05f
    override val maxMass = 551.16f
    override val minHeigh = 19.69f
    override val maxHeigh = 98.43f


    override fun isMassValid(mass: Float): Boolean {
        return mass > minMass && mass < maxMass
    }

    override fun isHeighValid(heigh: Float): Boolean {
        return heigh > minHeigh && heigh < maxHeigh
    }

    override fun countBMI(mass: Float, heigh: Float): Float {
        if (isMassValid(mass) && isHeighValid(heigh))
            return mass / (heigh * heigh) * 703
        else
            throw IllegalArgumentException()
    }
}

