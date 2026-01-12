package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.math.toFraction
import hiiragi283.core.api.recipe.HTProcessingRecipe
import org.apache.commons.lang3.math.Fraction
import java.util.function.IntUnaryOperator
import java.util.function.UnaryOperator

/**
 * [HTProcessingRecipe]向けの[HTRecipeBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
abstract class HTProcessingRecipeBuilder<BUILDER : HTProcessingRecipeBuilder<BUILDER>>(prefix: String) : HTRecipeBuilder<BUILDER>(prefix) {
    protected var time: Int = getDefaultTime()
        private set
    protected var exp: Fraction = Fraction.ZERO
        private set

    /**
     * @author Hiiragi Tsubasa
     * @since 0.7.0
     */
    protected open fun getDefaultTime(): Int = 200

    /**
     * @author Hiiragi Tsubasa
     * @since 0.7.0
     */
    fun modifyTime(operator: IntUnaryOperator): BUILDER = setTime(operator.applyAsInt(time))

    fun setTime(time: Int): BUILDER {
        this.time = maxOf(0, time)
        return self()
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 0.7.0
     */
    fun modifyExp(operator: UnaryOperator<Fraction>): BUILDER = setExp(operator.apply(exp))

    /**
     * @author Hiiragi Tsubasa
     * @since 0.7.0
     */
    fun setExp(exp: Float): BUILDER = setExp(exp.toFraction())

    fun setExp(exp: Fraction): BUILDER {
        this.exp = maxOf(Fraction.ZERO, exp)
        return self()
    }
}
