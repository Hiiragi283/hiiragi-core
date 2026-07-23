@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.item.component

import com.google.common.base.Suppliers
import hiiragi283.core.api.item.ItemInstanceBuilder
import hiiragi283.core.api.util.HTBuilderMarker
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.java
import java.util.function.Supplier
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.food.FoodConstants
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack

@HTBuilderMarker
class HTFoodBuilder {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTFoodBuilder.() -> Unit): FoodProperties {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTFoodBuilder().apply(builderAction).build()
        }

        @JvmStatic
        inline fun copyOf(parent: FoodProperties, builderAction: HTFoodBuilder.() -> Unit): FoodProperties {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return create {
                nutrition = parent.nutrition
                saturation = parent.saturation / nutrition / 2f
                alwaysEat = parent.canAlwaysEat
                eatSeconds = parent.eatSeconds
                parent.usingConvertsTo().ifPresent { +it }
                +parent.effects
                builderAction()
            }
        }
    }

    @JvmField var nutrition: Int = 0

    @JvmField var saturation: Float = 0f

    @JvmField var alwaysEat: Boolean = false

    @JvmField var eatSeconds: Float = 1.6f

    @PublishedApi internal var convertTo: Option<ItemStack> by HTDelegates.optionalOnceInitialize()
    private val effects: MutableList<FoodProperties.PossibleEffect> = mutableListOf()

    operator fun ItemStack.unaryPlus() {
        convertTo = Option.some(this)
    }

    operator fun FoodProperties.PossibleEffect.unaryPlus() {
        effects += this
    }

    operator fun Iterable<FoodProperties.PossibleEffect>.unaryPlus() {
        effects += this
    }

    fun fastFood() {
        eatSeconds = 0.8f
    }

    inline fun convertTo(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +ItemInstanceBuilder.buildStack(builderAction)
    }

    fun addEffect(effect: Supplier<MobEffectInstance>, chance: Float = 1f) {
        +FoodProperties.PossibleEffect(effect, chance)
    }

    fun addEffect(effect: MobEffectInstance, chance: Float = 1f) {
        addEffect(Suppliers.ofInstance(effect), chance)
    }

    fun addEffect(effect: Holder<MobEffect>, ticks: Int, amplifier: Int, chance: Float = 1f) {
        addEffect(MobEffectInstance(effect, ticks, amplifier), chance)
    }

    fun addInfinityEffect(effect: Holder<MobEffect>, amplifier: Int, chance: Float = 1f) {
        addEffect(effect, -1, amplifier, chance)
    }

    fun build(): FoodProperties = FoodProperties(nutrition, FoodConstants.saturationByModifier(nutrition, saturation), alwaysEat, eatSeconds, convertTo.java, effects)
}
