package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.common.block.HTWarpedWartBlock
import hiiragi283.core.common.item.block.HTWarpedWartItem
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.registry.HTDeferredBlock
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
            this[prefix.asMaterialPrefix(), material.asMaterialKey()] = REGISTER.registerSimple(prefix.createPath(material), properties)
        }

        // Ores
        register(HCMaterialPrefixes.ORE_NETHER, CommonMaterialKeys.CINNABAR, copyOf(Blocks.NETHER_QUARTZ_ORE))
        register(HCMaterialPrefixes.ORE_DEEPSLATE, CommonMaterialKeys.SULFUR, copyOf(Blocks.DEEPSLATE_LAPIS_ORE))
        register(HCMaterialPrefixes.ORE_NETHER, CommonMaterialKeys.SULFUR, copyOf(Blocks.NETHER_QUARTZ_ORE))

        // Storage Blocks
        fun registerBlock(
            material: HTMaterialLike,
            hardness: Float,
            resistance: Float,
            color: MapColor,
        ) {
            register(HCMaterialPrefixes.STORAGE_BLOCK, material, properties(hardness, resistance).mapColor(color))
        }

        fun registerBlock(
            material: HTMaterialLike,
            hardness: Float,
            resistance: Float,
            color: MapColor,
            soundType: SoundType,
        ) {
            register(HCMaterialPrefixes.STORAGE_BLOCK, material, properties(hardness, resistance).mapColor(color).sound(soundType))
        }

        registerBlock(VanillaMaterialKeys.CHARCOAL, 5f, 6f, MapColor.COLOR_BLACK)
        registerBlock(CommonMaterialKeys.COAL_COKE, 5f, 6f, MapColor.COLOR_GRAY)
        registerBlock(CommonMaterialKeys.CARBIDE, 5f, 6f, MapColor.DEEPSLATE)

        registerBlock(CommonMaterialKeys.CINNABAR, 5f, 9f, MapColor.TERRACOTTA_RED)
        registerBlock(CommonMaterialKeys.SALT, 5f, 9f, MapColor.TERRACOTTA_WHITE)
        registerBlock(CommonMaterialKeys.SALTPETER, 5f, 9f, MapColor.TERRACOTTA_WHITE)
        registerBlock(CommonMaterialKeys.SULFUR, 5f, 9f, MapColor.TERRACOTTA_YELLOW)

        registerBlock(VanillaMaterialKeys.ECHO, 5f, 9f, MapColor.COLOR_BLACK, SoundType.AMETHYST)
        registerBlock(HCMaterialKeys.AZURE, 5f, 9f, MapColor.TERRACOTTA_BLUE, SoundType.AMETHYST)
        registerBlock(HCMaterialKeys.CRIMSON_CRYSTAL, 5f, 9f, MapColor.CRIMSON_STEM, SoundType.AMETHYST)
        registerBlock(HCMaterialKeys.WARPED_CRYSTAL, 5f, 9f, MapColor.WARPED_STEM, SoundType.AMETHYST)

        registerBlock(VanillaMaterialKeys.ENDER, 5f, 9f, MapColor.TERRACOTTA_GREEN, SoundType.SHROOMLIGHT)
        registerBlock(HCMaterialKeys.ELDRITCH, 5f, 9f, MapColor.TERRACOTTA_PURPLE, SoundType.SHROOMLIGHT)

        registerBlock(HCMaterialKeys.NIGHT_METAL, 5f, 9f, MapColor.TERRACOTTA_BLACK, SoundType.METAL)

        registerBlock(CommonMaterialKeys.STEEL, 5f, 9f, MapColor.COLOR_GRAY, SoundType.METAL)
        registerBlock(HCMaterialKeys.AZURE_STEEL, 5f, 9f, MapColor.COLOR_BLUE, SoundType.METAL)
        registerBlock(HCMaterialKeys.DEEP_STEEL, 5f, 9f, MapColor.TERRACOTTA_LIGHT_GREEN, SoundType.METAL)

        registerBlock(CommonMaterialKeys.PLASTIC, 5f, 6f, MapColor.TERRACOTTA_WHITE, SoundType.WOOD)
        registerBlock(CommonMaterialKeys.RUBBER, 5f, 6f, MapColor.TERRACOTTA_BLACK, SoundType.WOOD)
    }.let(::HTMaterialTable)

    //    Crops    //

    @JvmField
    val WARPED_WART: HTDeferredBlock<HTWarpedWartBlock, HTWarpedWartItem> = REGISTER.register(
        "warped_wart",
        copyOf(Blocks.NETHER_WART),
        ::HTWarpedWartBlock,
        ::HTWarpedWartItem,
    )

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties =
        BlockBehaviour.Properties.of().strength(hardness, resistance)
}
