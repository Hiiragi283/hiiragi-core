package hiiragi283.core.setup

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.block.HTChoppingBoardBlock
import hiiragi283.core.common.block.HTCopperBasinBlock
import hiiragi283.core.common.block.HTForgingAnvilBlock
import hiiragi283.core.common.block.HTWarpedWartBlock
import hiiragi283.core.common.block.HTWeatheringCopperBasinBlock
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.color.HTColoredCollection
import hiiragi283.lib.color.VanillaColoredCollections
import hiiragi283.lib.copper.HTCopperPhase
import hiiragi283.lib.copper.HTWeatheringCoppers
import hiiragi283.lib.item.component.consumables
import hiiragi283.lib.material.CommonMaterialKeys
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.material.name
import hiiragi283.lib.registry.HTBasicDeferredBlockAndItem
import hiiragi283.lib.registry.HTDeferredBlockAndItemRegister
import hiiragi283.lib.registry.HTDeferredBlockRegister
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.resource.SimpleBlockItemSupplierWithKey
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import hiiragi283.lib.util.printError
import hiiragi283.lib.util.toTextResult
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
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
        fun registerOre(material: HTMaterialKey) {
            val name: String = material.name
            this[CommonPartKeys.ORE, material] = REGISTER.registerSimple("${name}_ore", copyOf(Blocks.IRON_ORE))
            this[CommonPartKeys.ORE_DEEPSLATE, material] = REGISTER.registerSimple("deepslate_${name}_ore", copyOf(Blocks.DEEPSLATE_IRON_ORE))
            this[CommonPartKeys.ORE_NETHER, material] = REGISTER.registerSimple("nether_${name}_ore", copyOf(Blocks.NETHER_QUARTZ_ORE))
            this[CommonPartKeys.ORE_END, material] = REGISTER.registerSimple("end_${name}_ore", copyOf(Blocks.END_STONE))
        }

        fun registerBlock(material: HTMaterialKey, blockProp: BlockBehaviour.Properties, itemProp: Identity<Item.Properties> = identity()) {
            this[CommonPartKeys.STORAGE_BLOCK, material] = REGISTER.registerSimple("${material.name}_block", blockProp, itemProp)
        }

        fun registerRawBlock(material: HTMaterialKey, blockProp: BlockBehaviour.Properties, itemProp: Identity<Item.Properties> = identity()) {
            this[CommonPartKeys.RAW_BLOCK, material] = REGISTER.registerSimple("raw_${material.name}_block", blockProp.sound(SoundType.STONE), itemProp)
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
        val result: HTTextResult<HTSimpleDeferredBlockAndItem> = get(part, material).toTextResult { "Unregistered part $part for ${material.identifier()}" }
        return result.printError(LOGGER)
    }

    //    Buildings    //

    @JvmField
    val CONCRETE_SLABS: HTColoredCollection<HTBasicDeferredBlockAndItem<SlabBlock>> = VanillaColoredCollections.CONCRETE.map { base: SimpleBlockItemSupplierWithKey -> REGISTER.registerSimple("${base.path}_slab", copyOf(base.get()), ::SlabBlock) }

    @JvmField
    val CONCRETE_STAIRS: HTColoredCollection<HTBasicDeferredBlockAndItem<StairBlock>> = VanillaColoredCollections.CONCRETE.map { base: SimpleBlockItemSupplierWithKey ->
        REGISTER.registerSimple("${base.path}_stairs", copyOf(base.get()), blockFactory = { StairBlock(base.get().defaultBlockState(), it) })
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
    val CHOPPING_BOARD: HTBasicDeferredBlockAndItem<HTChoppingBoardBlock> = REGISTER.registerSimple("chopping_board", copyOf(Blocks.OAK_WOOD).noOcclusion(), ::HTChoppingBoardBlock)

    @JvmField
    val FORGING_ANVIL: HTBasicDeferredBlockAndItem<HTForgingAnvilBlock> = REGISTER.registerSimple("forging_anvil", copyOf(Blocks.SMOOTH_STONE).noOcclusion(), ::HTForgingAnvilBlock)

    @JvmField
    val COPPER_BASIN: HTWeatheringCoppers<HTBasicDeferredBlockAndItem<HTCopperBasinBlock>> = run {
        val name = "copper_basin"
        val prop: BlockBehaviour.Properties = properties(2f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
            .sound(SoundType.COPPER)
        HTWeatheringCoppers(
            { phase: HTCopperPhase -> REGISTER.registerSimple(phase.createPath(name), prop, blockFactory = { HTWeatheringCopperBasinBlock(phase.toState(), it) }) },
            { phase: HTCopperPhase -> REGISTER.registerSimple(phase.createWaxedPath(name), prop, ::HTCopperBasinBlock) },
        )
    }

    //    Extensions    //

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun properties(hardness: Float, resistance: Float = hardness): BlockBehaviour.Properties = BlockBehaviour.Properties.of().strength(hardness, resistance)
}
