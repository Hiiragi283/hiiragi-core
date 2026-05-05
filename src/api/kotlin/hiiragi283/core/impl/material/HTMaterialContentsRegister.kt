package hiiragi283.core.impl.material

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
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toItemLike
import hiiragi283.core.api.registry.toLike
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
object HTMaterialContentsRegister {
    @JvmStatic
    private var hasInit: Boolean = false

    @JvmStatic
    internal val existingBlocks: HTTable.Mutable<HTPart, HTMaterialKey, HTMaterialContents.SimpleEntry<Block>> = mutableTableOf()

    @JvmStatic
    internal val existingItems: HTTable.Mutable<HTPart, HTMaterialKey, HTMaterialContents.ItemEntry> = mutableTableOf()

    @JvmStatic
    internal val existingTools: HTTable.Mutable<HTToolType, HTMaterialKey, HTMaterialContents.ItemEntry> = mutableTableOf()

    @JvmStatic
    internal lateinit var materialManager: HTMaterialManager
        private set

    @JvmStatic
    internal lateinit var materialBlocks: HTTable<HTPart, HTMaterialKey, HTMaterialContents.SimpleEntry<Block>>
        private set

    @JvmStatic
    internal lateinit var materialFluids: HTTable<HTFluidPart, HTMaterialKey, HTMaterialContents.FluidEntry>
        private set

    @JvmStatic
    internal lateinit var materialItems: HTTable<HTPart, HTMaterialKey, HTMaterialContents.ItemEntry>
        private set

    @JvmStatic
    internal lateinit var materialTools: HTTable<HTToolType, HTMaterialKey, HTMaterialContents.ItemEntry>
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
                        val item: Item = toolType.createTool(material)
                        helper.register(toolType.createKey(entry), item)
                        Triple(toolType, entry.asMaterialKey(), HTMaterialContents.ItemEntry(item, false))
                    }
            }
    }
}
