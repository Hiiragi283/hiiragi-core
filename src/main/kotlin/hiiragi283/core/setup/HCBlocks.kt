package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTOreVariant
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.block.HTTestBlock
import hiiragi283.core.common.block.HTWarpedWartBlock
import hiiragi283.core.common.item.block.HTWarpedWartItem
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.registry.HTBasicDeferredBlock
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
    val ORES: HTMaterialTable<HTOreVariant, HTSimpleDeferredBlock> = buildTable {
        fun register(variant: HTOreVariant, material: HTMaterialLike, properties: BlockBehaviour.Properties) {
            this[variant, material.asMaterialKey()] = REGISTER.registerSimple(variant.createPath(material), properties)
        }

        register(HTOreVariant.STONE, CommonMaterialKeys.ZINC, copyOf(Blocks.IRON_ORE))
        register(HTOreVariant.DEEPSLATE, CommonMaterialKeys.ZINC, copyOf(Blocks.DEEPSLATE_IRON_ORE))
    }.let(::HTMaterialTable)

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTTagPrefix, HTSimpleDeferredBlock> = buildTable {
        fun register(prefix: HTTagPrefix, material: HTMaterialLike, properties: BlockBehaviour.Properties) {
            this[prefix, material.asMaterialKey()] = REGISTER.registerSimple(prefix.createPath(material), properties)
        }

        fun registerBlock(
            material: HTMaterialLike,
            hardness: Float,
            resistance: Float,
            color: MapColor,
        ) {
            register(CommonTagPrefixes.BLOCK, material, properties(hardness, resistance).mapColor(color))
        }

        fun registerBlock(
            material: HTMaterialLike,
            hardness: Float,
            resistance: Float,
            color: MapColor,
            soundType: SoundType,
        ) {
            register(CommonTagPrefixes.BLOCK, material, properties(hardness, resistance).mapColor(color).sound(soundType))
        }

        registerBlock(VanillaMaterialKeys.CHARCOAL, 5f, 6f, MapColor.COLOR_BLACK)
        registerBlock(VanillaMaterialKeys.ECHO, 5f, 9f, MapColor.COLOR_BLACK, SoundType.AMETHYST)
        registerBlock(VanillaMaterialKeys.ENDER, 5f, 9f, MapColor.TERRACOTTA_GREEN, SoundType.SHROOMLIGHT)

        // Common
        registerBlock(CommonMaterialKeys.COAL_COKE, 5f, 6f, MapColor.COLOR_GRAY)
        registerBlock(CommonMaterialKeys.CARBIDE, 5f, 6f, MapColor.DEEPSLATE)

        registerBlock(CommonMaterialKeys.CINNABAR, 5f, 9f, MapColor.TERRACOTTA_RED)
        registerBlock(CommonMaterialKeys.SALT, 5f, 9f, MapColor.TERRACOTTA_WHITE)
        registerBlock(CommonMaterialKeys.SALTPETER, 5f, 9f, MapColor.TERRACOTTA_WHITE)
        registerBlock(CommonMaterialKeys.SULFUR, 5f, 9f, MapColor.TERRACOTTA_YELLOW)

        register(
            CommonTagPrefixes.RAW_BLOCK,
            CommonMaterialKeys.ZINC,
            properties(5f, 6f).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN),
        )
        registerBlock(CommonMaterialKeys.ZINC, 5f, 9f, MapColor.TERRACOTTA_LIGHT_GREEN, SoundType.METAL)

        registerBlock(CommonMaterialKeys.STEEL, 5f, 9f, MapColor.COLOR_GRAY, SoundType.METAL)
        registerBlock(CommonMaterialKeys.BRASS, 5f, 9f, MapColor.TERRACOTTA_YELLOW, SoundType.METAL)

        registerBlock(CommonMaterialKeys.PLASTIC, 5f, 6f, MapColor.TERRACOTTA_WHITE, SoundType.WOOD)
        registerBlock(CommonMaterialKeys.RUBBER, 5f, 6f, MapColor.TERRACOTTA_BLACK, SoundType.WOOD)
        // Hiiragi Core
        registerBlock(HCMaterialKeys.AZURE, 5f, 9f, MapColor.TERRACOTTA_BLUE, SoundType.AMETHYST)
        registerBlock(HCMaterialKeys.CRIMSON_CRYSTAL, 5f, 9f, MapColor.CRIMSON_STEM, SoundType.AMETHYST)
        registerBlock(HCMaterialKeys.WARPED_CRYSTAL, 5f, 9f, MapColor.WARPED_STEM, SoundType.AMETHYST)

        registerBlock(HCMaterialKeys.AZURE_STEEL, 5f, 9f, MapColor.COLOR_BLUE, SoundType.METAL)
        registerBlock(HCMaterialKeys.DEEP_STEEL, 5f, 9f, MapColor.TERRACOTTA_LIGHT_GREEN, SoundType.METAL)

        registerBlock(HCMaterialKeys.ELDRITCH, 5f, 9f, MapColor.TERRACOTTA_PURPLE, SoundType.SHROOMLIGHT)
    }.let(::HTMaterialTable)

    //    Crops    //

    @JvmField
    val WARPED_WART: HTDeferredBlock<HTWarpedWartBlock, HTWarpedWartItem> = REGISTER.register(
        "warped_wart",
        copyOf(Blocks.NETHER_WART),
        ::HTWarpedWartBlock,
        ::HTWarpedWartItem,
    )

    //    Misc    //

    @JvmField
    val TEST: HTBasicDeferredBlock<HTTestBlock> = REGISTER.registerSimple(
        "test",
        properties(5f, 6f),
        ::HTTestBlock,
    )

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties =
        BlockBehaviour.Properties.of().strength(hardness, resistance)
}
