package hiiragi283.lib.data

import hiiragi283.lib.util.HTBuilderMarker
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.flag.FeatureFlag
import net.minecraft.world.flag.FeatureFlagSet
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.NeoForgeConditions

@HTBuilderMarker
@JvmInline
value class ConditionBuilder(private val conditions: MutableList<ICondition>) {
    operator fun ICondition.unaryPlus() {
        conditions += this
    }

    operator fun <T : Any> ResourceKey<T>.unaryPlus() {
        +NeoForgeConditions.registered(this)
    }

    operator fun String.unaryPlus() {
        +NeoForgeConditions.modLoaded(this)
    }

    operator fun <T : Any> TagKey<T>.unaryMinus() {
        +NeoForgeConditions.tagEmpty(this)
    }

    operator fun <T : Any> TagKey<T>.unaryPlus() {
        +NeoForgeConditions.not(NeoForgeConditions.tagEmpty(this))
    }

    operator fun FeatureFlag.unaryPlus() {
        +NeoForgeConditions.featureFlagsEnabled(this)
    }

    operator fun FeatureFlagSet.unaryPlus() {
        +NeoForgeConditions.featureFlagsEnabled(this)
    }
}
