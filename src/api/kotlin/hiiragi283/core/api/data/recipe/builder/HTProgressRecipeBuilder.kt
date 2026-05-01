package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * [HTProgressRecipe]向けの[HTRecipeBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
abstract class HTProgressRecipeBuilder(prefix: String) : HTRecipeBuilder(prefix) {
    protected var progressData: HTProgressData = HTProgressData.time(20 * 10)

    var energy: Int by object : ReadWriteProperty<Any?, Int> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int = progressData.energy.orElseThrow()

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            progressData = HTProgressData.energy(value)
        }
    }
    var time: Int by object : ReadWriteProperty<Any?, Int> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Int = progressData.time.orElseGet { 20 * 10 }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            progressData = HTProgressData.time(value)
        }
    }
}
