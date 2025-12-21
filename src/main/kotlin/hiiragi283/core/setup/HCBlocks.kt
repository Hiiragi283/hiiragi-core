package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.common.block.HTDryingLackBlock
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.registry.HTBasicDeferredBlock
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.common.registry.register.HTDeferredBlockRegister
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
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

    //    Materials    //

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTMaterialPrefix, HTSimpleDeferredBlock> = buildTable {
        fun register(prefix: HTPrefixLike, material: HTMaterialLike, properties: BlockBehaviour.Properties) {
            this.put(prefix.asMaterialPrefix(), material.asMaterialKey(), REGISTER.registerSimple(prefix.createPath(material), properties))
        }

        // Ores
        register(HCMaterialPrefixes.ORE_NETHER, HCMaterial.Minerals.CINNABAR, copyOf(Blocks.NETHER_QUARTZ_ORE))
        register(HCMaterialPrefixes.ORE_DEEPSLATE, HCMaterial.Minerals.SULFUR, copyOf(Blocks.DEEPSLATE_LAPIS_ORE))
        register(HCMaterialPrefixes.ORE_NETHER, HCMaterial.Minerals.SULFUR, copyOf(Blocks.NETHER_QUARTZ_ORE))

        register(HCMaterialPrefixes.ORE, HCMaterial.Metals.SILVER, copyOf(Blocks.GOLD_ORE))
        register(HCMaterialPrefixes.ORE_DEEPSLATE, HCMaterial.Metals.SILVER, copyOf(Blocks.DEEPSLATE_GOLD_ORE))
        // Storage Blocks
        for (material: HCMaterial in HCMaterial.entries) {
            val properties: BlockBehaviour.Properties = getStorageProp(material) ?: continue
            val prefix: HTMaterialPrefix = HCMaterialPrefixes.STORAGE_BLOCK
            register(prefix, material, properties)
        }
    }.let(::HTMaterialTable)

    //    Utilities    //

    @JvmField
    val DRYING_LACK: HTBasicDeferredBlock<HTDryingLackBlock> = REGISTER.registerSimple(
        "drying_lack",
        copyOf(Blocks.OAK_PLANKS),
        ::HTDryingLackBlock,
    )

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

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
            is HCMaterial.Minerals -> {
                return BlockBehaviour.Properties
                    .of()
                    .strength(5f, 9f)
                    .sound(SoundType.AMETHYST)
                    .mapColor(
                        when (material) {
                            HCMaterial.Minerals.REDSTONE -> return null
                            HCMaterial.Minerals.GLOWSTONE -> return null
                            HCMaterial.Minerals.CINNABAR -> MapColor.TERRACOTTA_RED
                            HCMaterial.Minerals.SALTPETER -> MapColor.TERRACOTTA_WHITE
                            HCMaterial.Minerals.SULFUR -> MapColor.TERRACOTTA_YELLOW
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
            is HCMaterial.Metals -> {
                return BlockBehaviour.Properties
                    .of()
                    .strength(5f, 9f)
                    .sound(SoundType.COPPER)
                    .mapColor(
                        when (material) {
                            HCMaterial.Metals.SILVER -> MapColor.TERRACOTTA_LIGHT_BLUE
                            HCMaterial.Metals.NIGHT_METAL -> MapColor.TERRACOTTA_BLACK
                            else -> return null
                        },
                    )
            }
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
                        },
                    )
            }
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
            else -> return null
        }
    }
}
