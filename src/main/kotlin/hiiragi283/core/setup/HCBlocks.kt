package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.common.registry.register.HTDeferredBlockRegister
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus

object HCBlocks {
    @JvmField
    val REGISTER = HTDeferredBlockRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTMaterialPrefix, HTSimpleDeferredBlock> = buildTable {
        // Storage Blocks
        for (material: HCMaterial in HCMaterial.entries) {
            val properties: BlockBehaviour.Properties = getStorageProp(material) ?: continue
            val prefix: HTMaterialPrefix = HCMaterialPrefixes.STORAGE_BLOCK
            this.put(
                prefix,
                material.asMaterialKey(),
                REGISTER.registerSimple(prefix.createPath(material), properties),
            )
        }
    }.let(::HTMaterialTable)

    /**
     * @see mekanism.common.resource.BlockResourceInfo.modifyProperties
     */
    @JvmStatic
    private fun getStorageProp(material: HCMaterial): BlockBehaviour.Properties? {
        when (material) {
            is HCMaterial.Fuels -> {
                return BlockBehaviour.Properties
                    .of()
                    .strength(5f, 6f)
                    .sound(SoundType.TUFF)
                    .mapColor(
                        when (material) {
                            HCMaterial.Fuels.COAL -> return null
                            HCMaterial.Fuels.CHARCOAL -> MapColor.COLOR_BLACK
                            HCMaterial.Fuels.COAL_COKE -> MapColor.COLOR_GRAY
                            HCMaterial.Fuels.CARBIDE -> MapColor.DEEPSLATE
                        },
                    )
            }
            is HCMaterial.Gems -> {
                return BlockBehaviour.Properties
                    .of()
                    .strength(5f, 9f)
                    .sound(SoundType.AMETHYST)
                    .mapColor(
                        when (material) {
                            HCMaterial.Gems.ECHO -> MapColor.COLOR_BLACK
                            HCMaterial.Gems.CINNABAR -> MapColor.TERRACOTTA_RED
                            HCMaterial.Gems.SALTPETER -> MapColor.TERRACOTTA_WHITE
                            HCMaterial.Gems.SULFUR -> MapColor.TERRACOTTA_YELLOW
                            HCMaterial.Gems.AZURE -> MapColor.TERRACOTTA_BLUE
                            HCMaterial.Gems.CRIMSON_CRYSTAL -> MapColor.CRIMSON_STEM
                            HCMaterial.Gems.WARPED_CRYSTAL -> MapColor.WARPED_STEM
                            else -> return null
                        },
                    )
            }
            is HCMaterial.Pearls -> {
                return BlockBehaviour.Properties
                    .of()
                    .strength(5f, 9f)
                    .sound(SoundType.SHROOMLIGHT)
                    .mapColor(
                        when (material) {
                            HCMaterial.Pearls.ENDER -> MapColor.TERRACOTTA_GREEN
                            HCMaterial.Pearls.ELDRITCH -> MapColor.TERRACOTTA_PURPLE
                        },
                    )
            }
            is HCMaterial.Metals -> return null
            is HCMaterial.Alloys -> {
                return BlockBehaviour.Properties
                    .of()
                    .strength(5f, 9f)
                    .sound(SoundType.COPPER)
                    .mapColor(
                        when (material) {
                            HCMaterial.Alloys.NETHERITE -> return null
                            HCMaterial.Alloys.STEEL -> MapColor.COLOR_GRAY
                            HCMaterial.Alloys.AZURE_STEEL -> MapColor.COLOR_BLUE
                            HCMaterial.Alloys.DEEP_STEEL -> MapColor.TERRACOTTA_LIGHT_GREEN
                            HCMaterial.Alloys.NIGHT_METAL -> MapColor.TERRACOTTA_BLACK
                        },
                    )
            }
            is HCMaterial.Dusts -> return null
            is HCMaterial.Plates -> {
                return BlockBehaviour.Properties
                    .of()
                    .strength(5f, 6f)
                    .mapColor(
                        when (material) {
                            HCMaterial.Plates.PLASTIC -> MapColor.TERRACOTTA_WHITE
                            HCMaterial.Plates.RUBBER -> MapColor.TERRACOTTA_BLACK
                        },
                    )
            }
        }
    }
}
