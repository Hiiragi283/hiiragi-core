package hiiragi283.core.internal.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.Table
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.fluid.HTVirtualFluid
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTDeferredItem
import hiiragi283.core.api.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.core.api.registry.HTSimpleDeferredHolder
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.core.api.resource.SimpleBlockItemSupplierWithKey
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
data object HTMaterialContentsRegister {
    @JvmStatic
    private var hasInit: Boolean = false

    @JvmStatic
    internal lateinit var existingBlocks: Table<HTPart, HTMaterialKey, HTMaterialContents.BlockEntry>
        private set

    @JvmStatic
    internal lateinit var existingItems: Table<HTPart, HTMaterialKey, HTMaterialContents.ItemEntry>
        private set

    @JvmStatic
    internal lateinit var existingTools: Table<HTToolType, HTMaterialKey, HTMaterialContents.ItemEntry>
        private set

    @JvmStatic
    internal lateinit var materialManager: HTMaterialManager
        private set

    @JvmStatic
    internal lateinit var materialBlocks: Table<HTPart, HTMaterialKey, HTMaterialContents.BlockEntry>
        private set

    @JvmStatic
    internal lateinit var materialFluids: Table<HTFluidPart, HTMaterialKey, HTMaterialContents.FluidEntry>
        private set

    @JvmStatic
    internal lateinit var materialItems: Table<HTPart, HTMaterialKey, HTMaterialContents.ItemEntry>
        private set

    @JvmStatic
    internal lateinit var materialTools: Table<HTToolType, HTMaterialKey, HTMaterialContents.ItemEntry>
        private set

    @SubscribeEvent
    fun register(event: RegisterEvent) {
        initMaterials()

        event.register(Registries.BLOCK, ::registerMaterialBlocks.partially1(materialManager))

        event.register(Registries.ITEM, ::registerMaterialFluids.partially1(materialManager))
        event.register(Registries.ITEM, ::registerMaterialItems.partially1(materialManager))
        event.register(Registries.ITEM, ::registerMaterialTools.partially1(materialManager))
    }

    //    Existing    //

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
        val builderMap: MutableMap<HTMaterialKey, HTPropertyMap.Builder> = mutableMapOf()
        HiiragiCoreAccess.INSTANCE.forEachPlugin("Modifying Material Properties") {
            it.modifyMaterial { key: HTMaterialKey -> builderMap.computeIfAbsent(key) { HTPropertyMap.Builder() } }
        }
        materialManager = builderMap
            .mapValues { (_, builder: HTPropertyMap.Builder) -> builder.build() }
            .filterValues { !it.isEmpty }
            .let {
                object : HTMaterialManager {
                    override fun contains(material: HTMaterialLike): Boolean = material.asMaterialKey() in it

                    override fun get(material: HTMaterialLike): HTPropertyMap? = it[material.asMaterialKey()]

                    override val keys: Set<HTMaterialKey> = it.keys
                    override val entries: Set<HTMaterialManager.Entry> = it.mapTo(mutableSetOf(), ::EntryImpl)
                }
            }
    }

    private class EntryImpl(entry: Map.Entry<HTMaterialKey, HTPropertyMap>) :
        HTMaterialManager.Entry,
        HTMaterialLike by entry.key,
        HTPropertyMap by entry.value

    @JvmStatic
    private fun registerExistingBlocks() {
        existingBlocks = buildTable {
            HiiragiCoreAccess.INSTANCE.forEachPlugin("Register Existing Blocks") { plugin: HTMaterialPlugin ->
                plugin.registerExistingBlock { part: HTPartLike, material: HTMaterialKey, block: SimpleBlockItemSupplierWithKey ->
                    put(part.asPart(), material, HTMaterialContents.BlockEntry(block, true))
                }
            }
        }
    }

    @JvmStatic
    private fun registerExistingItems() {
        existingItems = buildTable {
            HiiragiCoreAccess.INSTANCE.forEachPlugin("Register Existing Items") { plugin: HTMaterialPlugin ->
                plugin.registerExistingItem { part: HTPartLike, material: HTMaterialKey, item: SimpleSupplierWithKey<Item> ->
                    put(part.asPart(), material, HTMaterialContents.ItemEntry(item, true))
                }
            }
        }
    }

    @JvmStatic
    private fun registerExistingTools() {
        existingTools = buildTable {
            HiiragiCoreAccess.INSTANCE.forEachPlugin("Register Existing Items") { plugin: HTMaterialPlugin ->
                plugin.registerExistingTool { toolType: HTToolType, key: HTMaterialKey, item: SimpleSupplierWithKey<Item> ->
                    put(toolType, key, HTMaterialContents.ItemEntry(item, true))
                }
            }
        }
    }

    //    Register    //

    @JvmStatic
    private fun registerMaterialBlocks(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Block>) {
        // 素材ブロックを生成する
        materialBlocks = buildTable {
            for (entry: HTMaterialManager.Entry in manager) {
                entry
                    .getOrDefault(HTMaterialPropertyKeys.BLOCK_PREFIXES)
                    .forEach { part: HTPartLike ->
                        val properties: BlockBehaviour.Properties = part[HTPartPropertyKeys.BLOCK_PROP] ?: return@forEach
                        val id: ResourceLocation = part.createId(entry)
                        helper.register(id, Block(properties))
                        put(part.asPart(), entry.asMaterialKey(), HTMaterialContents.BlockEntry(HTSimpleDeferredBlockAndItem(id), false))
                    }
            }
        }
    }

    @JvmStatic
    private fun registerMaterialFluids(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材液体を追加する
        materialFluids = buildTable {
            for (entry: HTMaterialManager.Entry in manager) {
                entry
                    .getOrDefault(HTMaterialPropertyKeys.FLUID_PREFIXES)
                    .forEach { part: HTFluidPart ->
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
                            HTVirtualFluid(typeHolder, HTDeferredItem(bucketId)),
                        )
                        helper.register(
                            bucketId,
                            BucketItem(fluid, Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)),
                        )

                        put(part, entry.asMaterialKey(), HTMaterialContents.FluidEntry(HTSimpleDeferredHolder(Registries.FLUID, id), false))
                    }
            }
        }
    }

    @JvmStatic
    private fun registerMaterialItems(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材ブロックのアイテムを生成する
        materialBlocks.forEach { (_, _, block: HTMaterialContents.BlockEntry) ->
            helper.register(block.getId(), HTBlockItem(block.get(), Item.Properties()))
        }
        // 素材アイテムを生成する
        materialItems = buildTable {
            for (entry: HTMaterialManager.Entry in manager) {
                entry
                    .getOrDefault(HTMaterialPropertyKeys.ITEM_PREFIXES)
                    .forEach { part: HTPartLike ->
                        val id: ResourceLocation = part.createId(entry)
                        helper.register(id, Item(Item.Properties()))
                        put(part.asPart(), entry.asMaterialKey(), HTMaterialContents.ItemEntry(HTSimpleDeferredItem(id), false))
                    }
            }
        }
    }

    @JvmStatic
    private fun registerMaterialTools(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材ツールを生成する
        materialTools = buildTable {
            for (entry: HTMaterialManager.Entry in manager) {
                val material: HTToolMaterial = entry[HTMaterialPropertyKeys.TOOL_MATERIAL] ?: continue
                entry
                    .getOrDefault(HTMaterialPropertyKeys.TOOL_PREFIXES)
                    .forEach { toolType: HTToolType ->
                        val id: ResourceLocation = toolType.createId(entry)
                        helper.register(id, toolType.createTool(material))
                        put(toolType, entry.asMaterialKey(), HTMaterialContents.ItemEntry(HTSimpleDeferredItem(id), false))
                    }
            }
        }
    }
}
