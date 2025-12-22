package hiiragi283.core.data.server.loot

import hiiragi283.core.api.data.loot.HTBlockLootTableProvider
import hiiragi283.core.api.function.partially2
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

class HCBlockLootTableProvider(registries: HolderLookup.Provider) : HTBlockLootTableProvider(registries) {
    override fun generate() {
        HCBlocks.REGISTER.asBlockSequence().forEach(::dropSelf)

        // Ores
        val ores: Array<HTMaterialPrefix> = arrayOf(
            HCMaterialPrefixes.ORE,
            HCMaterialPrefixes.ORE_DEEPSLATE,
            HCMaterialPrefixes.ORE_NETHER,
            HCMaterialPrefixes.ORE_END,
        )
        val materials: Map<HCMaterial, UniformGenerator?> = mapOf(
            HCMaterial.Minerals.CINNABAR to UniformGenerator.between(2f, 5f),
            HCMaterial.Minerals.SULFUR to UniformGenerator.between(4f, 5f),
            HCMaterial.Metals.SILVER to null,
        )

        for ((material: HCMaterial, range: UniformGenerator?) in materials) {
            val basePrefix: HTMaterialPrefix = material.basePrefix
            for (prefix: HTMaterialPrefix in ores) {
                val ore: HTSimpleDeferredBlock = HCBlocks.MATERIALS[prefix, material] ?: continue
                val drop: ItemLike = HCItems.MATERIALS[basePrefix, material] ?: continue
                add(ore, ::createOreDrops.partially2(drop, range))
            }
        }
    }
}
