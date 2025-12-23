package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.math.toFraction
import org.apache.commons.lang3.math.Fraction

abstract class HTProcessingRecipeBuilder<BUILDER : HTProcessingRecipeBuilder<BUILDER>>(prefix: String) : HTRecipeBuilder<BUILDER>(prefix) {
    protected var time: Int = 200
        private set
    protected var exp: Fraction = Fraction.ZERO
        private set

    fun setTime(time: Int): BUILDER {
        this.time = maxOf(0, time)
        return self()
    }

    fun setExp(exp: Float): BUILDER = setExp(exp.toFraction())

    fun setExp(exp: Fraction): BUILDER {
        this.exp = maxOf(Fraction.ZERO, exp)
        return self()
    }
}
