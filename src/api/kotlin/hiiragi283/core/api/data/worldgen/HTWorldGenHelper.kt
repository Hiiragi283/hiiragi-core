package hiiragi283.core.api.data.worldgen

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers

data object HTWorldGenHelper {
    //    ConfiguredFeature    //

    @JvmStatic
    fun <FC : FeatureConfiguration, F : Feature<FC>> register(context: BootstrapContext<ConfiguredFeature<*, *>>, data: HTWorldGenData, feature: F, config: FC) {
        register(context, data, ConfiguredFeature(feature, config))
    }

    @JvmStatic
    fun register(context: BootstrapContext<ConfiguredFeature<*, *>>, data: HTWorldGenData, feature: ConfiguredFeature<*, *>) {
        context.register(data.configuredKey, feature)
    }

    //    ConfiguredFeature    //

    @JvmStatic
    fun register(context: BootstrapContext<PlacedFeature>, data: HTWorldGenData, placement: List<PlacementModifier>) {
        val holder: Holder.Reference<ConfiguredFeature<*, *>> = context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(data.configuredKey)
        context.register(data.placedKey, PlacedFeature(holder, placement))
    }

    //    BiomeModifier    //

    @JvmStatic
    fun register(context: BootstrapContext<BiomeModifier>, data: HTWorldGenData, modifier: BiomeModifier) {
        context.register(data.modifierKey, modifier)
    }

    @JvmStatic
    fun register(context: BootstrapContext<BiomeModifier>, data: HTWorldGenData, biome: TagKey<Biome>, decoration: GenerationStep.Decoration) {
        val biomes: HolderSet<Biome> = context.lookup(Registries.BIOME).getOrThrow(biome)
        val placement: HolderSet<PlacedFeature> = context.lookup(Registries.PLACED_FEATURE).getOrThrow(data.placedKey).let { HolderSet.direct(it) }
        context.register(data.modifierKey, BiomeModifiers.AddFeaturesBiomeModifier(biomes, placement, decoration))
    }
}
