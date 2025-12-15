package hiiragi283.core.api.data

import hiiragi283.core.api.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.registries.NeoForgeRegistries

data class HTWorldGenData(
    val configuredKey: ResourceKey<ConfiguredFeature<*, *>>,
    val placedKey: ResourceKey<PlacedFeature>,
    val modifierKey: ResourceKey<BiomeModifier>,
) {
    constructor(id: ResourceLocation) : this(
        Registries.CONFIGURED_FEATURE.createKey(id),
        Registries.PLACED_FEATURE.createKey(id),
        NeoForgeRegistries.Keys.BIOME_MODIFIERS.createKey(id),
    )

    constructor(configuredData: HTWorldGenData, id: ResourceLocation) : this(
        configuredData.configuredKey,
        Registries.PLACED_FEATURE.createKey(id),
        NeoForgeRegistries.Keys.BIOME_MODIFIERS.createKey(id),
    )
}
