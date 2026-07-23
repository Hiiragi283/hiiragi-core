package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.util.HTDelegates
import net.minecraft.world.item.crafting.Recipe

/**
 * [HTProgressData]を使用するレシピ向けの，[HTRecipeBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
abstract class HTProgressRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String) : HTRecipeBuilder<RECIPE>(prefix) {
    protected var progressData: HTProgressData = HTProgressData.time(20 * 10)
        private set

    var energy: Int by HTDelegates.onceInitialize()
    var time: Int by HTDelegates.onceInitialize { 20 * 10 }
}
