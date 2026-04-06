package hiiragi283.core.setup

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.collection.mutableTableOf
import hiiragi283.core.api.collection.toFlatTable
import hiiragi283.core.api.fluid.HTVirtualFluid
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.property.isNotEmpty
import hiiragi283.core.api.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.data.tank.HTSimpleTankInteraction
import hiiragi283.core.common.gui.sync.HTBoolSyncPayload
import hiiragi283.core.common.gui.sync.HTFluidSyncPayload
import hiiragi283.core.common.gui.sync.HTFractionSyncPayload
import hiiragi283.core.common.gui.sync.HTIntSyncPayload
import hiiragi283.core.common.gui.sync.HTItemSyncPayload
import hiiragi283.core.common.gui.sync.HTLongSyncPayload
import hiiragi283.core.impl.HiiragiCoreAccessImpl
import hiiragi283.core.impl.material.HTMaterialManagerImpl
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.TieredItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

internal object HCMiscRegister {
    @JvmStatic
    private var hasInit: Boolean = false

    @JvmStatic
    val existingBlocks: HTTable.Mutable<HTPart, HTMaterialKey, HTMaterialContents.SimpleEntry<Block>> = mutableTableOf()

    @JvmStatic
    val existingItems: HTTable.Mutable<HTPart, HTMaterialKey, HTMaterialContents.ItemEntry> = mutableTableOf()

    @JvmStatic
    val existingTools: HTTable.Mutable<HTToolType, HTMaterialKey, HTMaterialContents.ItemEntry> = mutableTableOf()

    @JvmStatic
    lateinit var materialBlocks: HTTable<HTPart, HTMaterialKey, HTMaterialContents.SimpleEntry<Block>>
        private set

    @JvmStatic
    lateinit var materialFluids: HTTable<HTFluidPart, HTMaterialKey, HTMaterialContents.FluidEntry>
        private set

    @JvmStatic
    lateinit var materialItems: HTTable<HTPart, HTMaterialKey, HTMaterialContents.ItemEntry>
        private set

    @JvmStatic
    lateinit var materialTools: HTTable<HTToolType, HTMaterialKey, HTMaterialContents.ItemEntry>
        private set

    @JvmStatic
    fun register(event: RegisterEvent) {
        initMaterials()

        val manager: HTMaterialManager = HiiragiCoreAccess.INSTANCE.materialManager
        event.register(Registries.BLOCK, ::registerMaterialBlocks.partially1(manager))

        event.register(Registries.ITEM, ::registerMaterialFluids.partially1(manager))
        event.register(Registries.ITEM, ::registerMaterialItems.partially1(manager))
        event.register(Registries.ITEM, ::registerMaterialTools.partially1(manager))

        // Fluid Ingredient Type
        event.register(NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("potion"), HTPotionFluidIngredient.TYPE)
        }

        // Slot Sync Type
        event.register(HCRegistries.Keys.SLOT_TYPE) { helper ->
            helper.register(HTConst.COMMON.toId("boolean"), HTBoolSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("fraction"), HTFractionSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("integer"), HTIntSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("long"), HTLongSyncPayload.TYPE)

            helper.register(HTConst.MINECRAFT.toId("fluid"), HTFluidSyncPayload.TYPE)
            helper.register(HTConst.MINECRAFT.toId("item"), HTItemSyncPayload.TYPE)
        }
        // Tank Interaction Type
        event.register(HCRegistries.Keys.TANK_INTERACTION_TYPE) { helper ->
            helper.register(HTConst.MINECRAFT.toId("simple"), HTSimpleTankInteraction.CODEC)
        }
    }

    //    Initialization    //

    @JvmStatic
    private fun initMaterials() {
        if (!hasInit) {
            HiiragiCoreAccess.INSTANCE.partManager
            // 既存の素材ブロックを登録する
            registerExistingBlocks()
            // 既存の素材アイテムを登録する
            registerExistingItems()
            // 既存の素材ツールを登録する
            registerExistingTools()
            // 素材のプロパティを定義する
            gatherProperties()
            hasInit = true
        }
    }

    @JvmStatic
    private fun gatherProperties() {
        val builderMap: MutableMap<HTMaterialKey, HTPropertyMap.Mutable> = mutableMapOf()
        HiiragiCoreAccess.INSTANCE.forEachPlugin("Modifying Material Properties") {
            it.modifyMaterial { key: HTMaterialKey -> builderMap.computeIfAbsent(key) { HTBasicPropertyMap.Mutable() } }
        }
        HiiragiCoreAccessImpl.materialManagerCache = builderMap.filterValues(HTPropertyMap::isNotEmpty).let(::HTMaterialManagerImpl)
    }

    @JvmStatic
    private fun registerExistingBlocks() {
        HiiragiCoreAccess.INSTANCE.forEachPlugin("Register Existing Blocks") { plugin: HTMaterialPlugin ->
            plugin.registerExistingBlock { part: HTPartLike, material: HTMaterialKey, block: HTBlockHolderLike<*> ->
                existingBlocks.put(part.asPart(), material, HTMaterialContents.SimpleEntry(block, true))
            }
        }
    }

    @JvmStatic
    private fun registerExistingItems() {
        HiiragiCoreAccess.INSTANCE.forEachPlugin("Register Existing Items") { plugin: HTMaterialPlugin ->
            plugin.registerExistingItem { part: HTPartLike, material: HTMaterialKey, item: HTItemHolderLike<*> ->
                existingItems.put(part.asPart(), material, HTMaterialContents.ItemEntry(item, true))
            }
        }
    }

    @JvmStatic
    private fun registerExistingTools() {
        HiiragiCoreAccess.INSTANCE.forEachPlugin("Register Existing Items") { plugin: HTMaterialPlugin ->
            plugin.registerExistingTool { toolType: HTToolType, key: HTMaterialKey, item: HTItemHolderLike<*> ->
                existingTools.put(toolType, key, HTMaterialContents.ItemEntry(item, true))
            }
        }
    }

    //    Register    //

    @JvmStatic
    private fun registerMaterialBlocks(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Block>) {
        // 素材ブロックを生成する
        materialBlocks = manager
            .toFlatTable { entry: HTMaterialManager.Entry ->
                entry
                    .getOrDefault(HTMaterialPropertyKeys.BLOCK_PREFIXES)
                    .mapNotNull { part: HTPartLike ->
                        val properties: BlockBehaviour.Properties = part[HTPartPropertyKeys.BLOCK_PROP] ?: return@mapNotNull null
                        val block = Block(properties)
                        helper.register(part.createId(entry), block)
                        Triple(part.asPart(), entry.asMaterialKey(), HTMaterialContents.SimpleEntry(block.toLike(), false))
                    }
            }
    }

    @JvmStatic
    private fun registerMaterialFluids(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材液体を追加する
        materialFluids = manager
            .toFlatTable { entry: HTMaterialManager.Entry ->
                entry
                    .getOrDefault(HTMaterialPropertyKeys.FLUID_PREFIXES)
                    .map { part: HTFluidPart ->
                        val id: ResourceLocation = part.createId(entry)

                        val typeHolder: DeferredHolder<FluidType, FluidType> =
                            DeferredHolder.create(NeoForgeRegistries.Keys.FLUID_TYPES, id)
                        Registry.register(
                            NeoForgeRegistries.FLUID_TYPES,
                            id,
                            FluidType(
                                FluidType.Properties
                                    .create()
                                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA),
                            ),
                        )

                        val bucketId: ResourceLocation = id.withSuffix("_bucket")
                        val fluid: HTVirtualFluid = Registry.register(
                            BuiltInRegistries.FLUID,
                            id,
                            HTVirtualFluid(typeHolder, bucketId.toItemLike()),
                        )
                        helper.register(
                            bucketId,
                            BucketItem(fluid, Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)),
                        )

                        Triple(part, entry.asMaterialKey(), HTMaterialContents.FluidEntry(fluid, false))
                    }
            }
    }

    @JvmStatic
    private fun registerMaterialItems(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材ブロックのアイテムを生成する
        materialBlocks.forEach { (_, _, block: HTMaterialContents.SimpleEntry<Block>) ->
            val id: ResourceLocation = block.getId()
            helper.register(id, HTBlockItem(block.get(), Item.Properties()))
        }
        // 素材アイテムを生成する
        materialItems = manager
            .toFlatTable { entry: HTMaterialManager.Entry ->
                entry
                    .getOrDefault(HTMaterialPropertyKeys.ITEM_PREFIXES)
                    .map { part: HTPartLike ->
                        val item = Item(Item.Properties())
                        helper.register(part.createId(entry), item)
                        Triple(part.asPart(), entry.asMaterialKey(), HTMaterialContents.ItemEntry(item, false))
                    }
            }
    }

    @JvmStatic
    private fun registerMaterialTools(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材ツールを生成する
        materialTools = manager
            .toFlatTable { entry: HTMaterialManager.Entry ->
                val material: HTToolMaterial =
                    entry[HTMaterialPropertyKeys.TOOL_MATERIAL] ?: return@toFlatTable emptySet()
                entry
                    .getOrDefault(HTMaterialPropertyKeys.TOOL_PREFIXES)
                    .map { toolType: HTToolType ->
                        val item: TieredItem = toolType.createTool(material)
                        helper.register(toolType.createKey(entry), item)
                        Triple(toolType, entry.asMaterialKey(), HTMaterialContents.ItemEntry(item, false))
                    }
            }
    }
}
