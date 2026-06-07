package hiiragi283.core.setup

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.block.HTChoppingBoardBlock
import hiiragi283.core.common.block.HTCopperBasinBlock
import hiiragi283.core.common.block.HTWarpedWartBlock
import hiiragi283.core.common.block.HTWeatheringCopperBasinBlock
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.item.HTBlockItem
import hiiragi283.lib.item.component.consumables
import hiiragi283.lib.material.CommonMaterialKeys
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.registry.HTBasicDeferredBlockAndItem
import hiiragi283.lib.registry.HTDeferredBlockAndItemRegister
import hiiragi283.lib.registry.HTDeferredBlockRegister
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTWeatheringCopperBlocks
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import hiiragi283.lib.util.printError
import hiiragi283.lib.util.right
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus
import org.slf4j.Logger

data object HCBlocks {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER_ONLY_BLOCK = HTDeferredBlockRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val REGISTER = HTDeferredBlockAndItemRegister(REGISTER_ONLY_BLOCK)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Resources    //

    @JvmField
    val RESOURCES: Table<HTMaterialPartKey, HTMaterialKey, HTSimpleDeferredBlockAndItem> = buildTable {
        fun registerBlock(material: HTMaterialKey, blockProp: BlockBehaviour.Properties, itemProp: Identity<Item.Properties> = identity()) {
            this[CommonPartKeys.STORAGE_BLOCK, material] = REGISTER.registerSimple("${material.identifier().path}_block", blockProp, itemProp)
        }

        fun registerRawBlock(material: HTMaterialKey, blockProp: BlockBehaviour.Properties, itemProp: Identity<Item.Properties> = identity()) {
            this[CommonPartKeys.RAW_BLOCK, material] = REGISTER.registerSimple("raw_${material.identifier().path}_block", blockProp.sound(SoundType.STONE), itemProp)
        }

        // Vanilla
        registerBlock(VanillaMaterialKeys.CHARCOAL, copyOf(Blocks.COAL_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN))

        registerBlock(VanillaMaterialKeys.ECHO, copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_CYAN))
        // Common
        registerRawBlock(CommonMaterialKeys.TIN, copyOf(Blocks.COPPER_BLOCK).mapColor(MapColor.WARPED_WART_BLOCK))
        registerRawBlock(CommonMaterialKeys.IRIDIUM, copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.NONE))
        registerRawBlock(CommonMaterialKeys.PLATINUM, copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_WHITE))

        registerBlock(CommonMaterialKeys.TIN, copyOf(Blocks.COPPER_BLOCK).mapColor(MapColor.WARPED_WART_BLOCK))
        registerBlock(CommonMaterialKeys.IRIDIUM, copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.NONE))
        registerBlock(CommonMaterialKeys.PLATINUM, copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_WHITE))
        registerBlock(CommonMaterialKeys.LEAD, copyOf(Blocks.COPPER_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE))
        // Hiiragi Core
    }

    @JvmStatic
    operator fun get(part: HTMaterialPartKey, material: HTMaterialKey): HTSimpleDeferredBlockAndItem? = RESOURCES[part, material]

    @JvmStatic
    fun getResult(part: HTMaterialPartKey, material: HTMaterialKey): HTTextResult<HTSimpleDeferredBlockAndItem> {
        val result: HTTextResult<HTSimpleDeferredBlockAndItem> = get(part, material)?.right() ?: HTTextResult("Unregistered part $part for ${material.identifier()}")
        return result.printError(LOGGER)
    }

    //    Crops    //

    @JvmField
    val WARPED_WART: HTBasicDeferredBlockAndItem<HTWarpedWartBlock> = REGISTER.registerSimple(
        "warped_wart",
        copyOf(Blocks.NETHER_WART).mapColor(MapColor.WARPED_WART_BLOCK),
        ::HTWarpedWartBlock,
    ) { prop: Item.Properties -> prop.consumables(HCConsumables.WARPED_WART) }

    //    Misc    //

    @JvmField
    val CHOPPING_BOARD: HTBasicDeferredBlockAndItem<HTChoppingBoardBlock> = REGISTER.registerSimple("chopping_board", copyOf(Blocks.OAK_WOOD), ::HTChoppingBoardBlock)

    @JvmField
    val COPPER_BASIN: HTWeatheringCopperBlocks<HTCopperBasinBlock, HTWeatheringCopperBasinBlock, HTBlockItem<Block>> = HTWeatheringCopperBlocks.createSimple(
        REGISTER,
        "copper_basin",
        {
            properties(2f)
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .sound(SoundType.COPPER)
        },
        ::HTCopperBasinBlock,
        ::HTWeatheringCopperBasinBlock,
    )

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties = BlockBehaviour.Properties.of().strength(hardness, resistance)
}
