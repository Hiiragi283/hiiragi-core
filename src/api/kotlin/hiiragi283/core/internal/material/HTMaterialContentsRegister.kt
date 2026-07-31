package hiiragi283.core.internal.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.Table
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartKey
import hiiragi283.core.api.material.part.HTPartManager
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyManager
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.core.api.resource.SimpleBlockItemSupplierWithKey
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.internal.item.HTMaterialBlockItem
import hiiragi283.core.internal.item.HTMaterialItem
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.RegisterEvent

/**
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
data object HTMaterialContentsRegister {
    @JvmStatic
    private var hasInit: Boolean = false

    @JvmStatic
    internal lateinit var partManager: HTPartManager
        private set

    @JvmStatic
    internal lateinit var existingContents: HTMaterialAccess
        private set

    @JvmStatic
    internal lateinit var materialManager: HTMaterialManager
        private set

    @JvmStatic
    private lateinit var materialBlocks: Table<HTPartKey, HTMaterialKey, HTMaterialContents.BlockEntry>

    @JvmStatic
    private lateinit var materialItems: Table<HTPartKey, HTMaterialKey, HTMaterialContents.ItemEntry>

    @JvmStatic
    private lateinit var materialTools: Table<HTToolType, HTMaterialKey, HTMaterialContents.ItemEntry>

    @JvmStatic
    internal val registeredContents: HTMaterialAccess by lazy {
        HTMaterialAccess(
            HTMaterialContentsImpl(materialBlocks) { part: HTPartKey, key: HTMaterialKey -> "Unregistered $part block for $key" },
            HTMaterialContentsImpl(materialItems) { part: HTPartKey, key: HTMaterialKey -> "Unregistered $part item for $key" },
            HTMaterialContentsImpl(materialTools) { toolType: HTToolType, key: HTMaterialKey -> "Unregistered ${toolType.name} item for $key" },
        )
    }

    @SubscribeEvent
    fun register(event: RegisterEvent) {
        initMaterials()

        event.register(Registries.BLOCK) { registerMaterialBlocks(materialManager, it) }

        // event.register(Registries.ITEM) { registerMaterialFluids(materialManager, it) }
        event.register(Registries.ITEM) { registerMaterialItems(materialManager, it) }
        event.register(Registries.ITEM) { registerMaterialTools(materialManager, it) }
    }

    //    Existing    //

    @JvmStatic
    private fun initMaterials() {
        if (!hasInit) {
            // 部品のプロパティを定義する
            gatherPartProperties()
            // 既存の素材コンテンツを登録する
            registerExistingContents()
            // 素材のプロパティを定義する
            gatherMaterialProperties()
            hasInit = true
        }
    }

    @JvmStatic
    private fun gatherPartProperties() {
        val partMap: Map<HTPartKey, HTPart> = buildMap {
            HiiragiCoreAccess.INSTANCE.forEachPlugin("Register Part") { plugin: HTMaterialPlugin ->
                plugin.registerPart { key: HTPartKey, idPattern: String, getter: HTPropertyGetter ->
                    val entry = HTPart(key, idPattern, getter)
                    check(this.put(key, entry) == null) { "Duplicated part registration: $key" }
                }
            }
        }
        partManager = HTPropertyManager(partMap)
    }

    @JvmStatic
    private fun registerExistingContents() {
        val existingBlocks: Table<HTPartKey, HTMaterialKey, HTMaterialContents.BlockEntry> = buildTable {
            HiiragiCoreAccess.INSTANCE.forEachPlugin("Register Existing Blocks") { plugin: HTMaterialPlugin ->
                plugin.registerExistingBlock { part: HTPartKey, key: HTMaterialKey, block: SimpleBlockItemSupplierWithKey ->
                    put(part, key, HTMaterialContents.BlockEntry(block, true))
                }
            }
        }
        val existingItems: Table<HTPartKey, HTMaterialKey, HTMaterialContents.ItemEntry> = buildTable {
            HiiragiCoreAccess.INSTANCE.forEachPlugin("Register Existing Items") { plugin: HTMaterialPlugin ->
                plugin.registerExistingItem { part: HTPartKey, key: HTMaterialKey, item: SimpleSupplierWithKey<Item> ->
                    put(part, key, HTMaterialContents.ItemEntry(item, true))
                }
            }
        }
        val existingTools: Table<HTToolType, HTMaterialKey, HTMaterialContents.ItemEntry> = buildTable {
            HiiragiCoreAccess.INSTANCE.forEachPlugin("Register Existing Items") { plugin: HTMaterialPlugin ->
                plugin.registerExistingTool { toolType: HTToolType, key: HTMaterialKey, item: SimpleSupplierWithKey<Item> ->
                    put(toolType, key, HTMaterialContents.ItemEntry(item, true))
                }
            }
        }
        existingContents = HTMaterialAccess(
            HTMaterialContentsImpl(existingBlocks) { part: HTPartKey, key: HTMaterialKey -> "Unknown $part block for $key" },
            HTMaterialContentsImpl(existingItems) { part: HTPartKey, key: HTMaterialKey -> "Unknown $part item for $key" },
            HTMaterialContentsImpl(existingTools) { toolType: HTToolType, key: HTMaterialKey -> "Unknown ${toolType.name} item for $key" },
        )
    }

    @JvmStatic
    private fun gatherMaterialProperties() {
        val builderMap: MutableMap<HTMaterialKey, HTPropertyMap.Builder> = mutableMapOf()
        HiiragiCoreAccess.INSTANCE.forEachPlugin("Modifying Material Properties") {
            it.modifyMaterial { key: HTMaterialKey -> builderMap.computeIfAbsent(key) { HTPropertyMap.Builder() } }
        }
        val materialMap: MutableMap<HTMaterialKey, HTMaterial> = mutableMapOf()
        for ((key: HTMaterialKey, builder: HTPropertyMap.Builder) in builderMap) {
            val propertyMap: HTPropertyMap = builder.build()
            if (!propertyMap.isEmpty) {
                materialMap[key] = HTMaterial(key, propertyMap)
            }
        }
        materialManager = HTPropertyManager(materialMap)
    }

    //    Register    //

    @JvmStatic
    private fun registerMaterialBlocks(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Block>) {
        // 素材ブロックを生成する
        materialBlocks = buildTable {
            for (material: HTMaterial in manager) {
                val key: HTMaterialKey = material.key
                material
                    .getOrDefault(HTMaterialPropertyKeys.BLOCK_PREFIXES)
                    .forEach { partKey: HTPartKey ->
                        val part: HTPart = partManager[partKey] ?: return@forEach
                        val properties: BlockBehaviour.Properties = part[HTPartPropertyKeys.BLOCK_PROP] ?: return@forEach
                        val id: ResourceLocation = part.createId(key)
                        helper.register(id, Block(properties))
                        put(partKey, key, HTMaterialContents.BlockEntry(HTSimpleDeferredBlockAndItem(id), false))
                    }
            }
        }
    }

    /*private fun registerMaterialFluids(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材液体を追加する
        materialFluids = buildTable {
            for ((key: HTMaterialKey, map: HTPropertyMap) in manager) {
                map
                    .getOrDefault(HTMaterialPropertyKeys.FLUID_PREFIXES)
                    .forEach { part: HTFluidPart ->
                        val id: ResourceLocation = part.createId(key)

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
                            HTMaterialBucketItem(map, fluid, Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)),
                        )

                        put(part, key, HTMaterialContents.FluidEntry(HTSimpleDeferredHolder(Registries.FLUID, id), false))
                    }
            }
        }
    }*/

    @JvmStatic
    private fun registerMaterialItems(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材ブロックのアイテムを生成する
        materialBlocks.forEach { (_, key: HTMaterialKey, block: HTMaterialContents.BlockEntry) ->
            helper.register(block.getId(), HTMaterialBlockItem(manager.getOrThrow(key), block.get(), Item.Properties()))
        }
        // 素材アイテムを生成する
        materialItems = buildTable {
            for (material: HTMaterial in manager) {
                val key: HTMaterialKey = material.key
                material
                    .getOrDefault(HTMaterialPropertyKeys.ITEM_PREFIXES)
                    .forEach { partKey: HTPartKey ->
                        val part: HTPart = partManager[partKey] ?: return@forEach
                        val id: ResourceLocation = part.createId(key)
                        helper.register(id, HTMaterialItem(material, Item.Properties()))
                        put(partKey, key, HTMaterialContents.ItemEntry(HTSimpleDeferredItem(id), false))
                    }
            }
        }
    }

    @JvmStatic
    private fun registerMaterialTools(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材ツールを生成する
        materialTools = buildTable {
            for (material: HTMaterial in manager) {
                val key: HTMaterialKey = material.key
                val toolMaterial: HTToolMaterial = material[HTMaterialPropertyKeys.TOOL_MATERIAL] ?: continue
                material
                    .getOrDefault(HTMaterialPropertyKeys.TOOL_PREFIXES)
                    .forEach { toolType: HTToolType ->
                        val id: ResourceLocation = toolType.createId(key)
                        helper.register(id, toolType.createTool(toolMaterial))
                        put(toolType, key, HTMaterialContents.ItemEntry(HTSimpleDeferredItem(id), false))
                    }
            }
        }
    }
}
