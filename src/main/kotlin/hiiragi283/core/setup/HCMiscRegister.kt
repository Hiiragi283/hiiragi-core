package hiiragi283.core.setup

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.collection.mutableTableOf
import hiiragi283.core.api.collection.toFlatTable
import hiiragi283.core.api.event.HTMaterialPropertyEvent
import hiiragi283.core.api.event.HTRegisterExistingPartEvent
import hiiragi283.core.api.fluid.HTVirtualFluid
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.api.tag.fluid.HTFluidTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.common.HiiragiCoreAccessImpl
import hiiragi283.core.common.gui.sync.HTBoolSyncPayload
import hiiragi283.core.common.gui.sync.HTFluidSyncPayload
import hiiragi283.core.common.gui.sync.HTFractionSyncPayload
import hiiragi283.core.common.gui.sync.HTIntSyncPayload
import hiiragi283.core.common.gui.sync.HTItemSyncPayload
import hiiragi283.core.common.gui.sync.HTLongSyncPayload
import hiiragi283.core.common.material.HTMaterialManagerImpl
import hiiragi283.core.common.registry.HTDeferredFluid
import hiiragi283.core.common.registry.HTSimpleDeferredItem
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
import net.neoforged.fml.ModLoader
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

internal object HCMiscRegister {
    @JvmStatic
    private var hasInit: Boolean = false

    @JvmStatic
    val existingBlocks: HTTable.Mutable<HTTagPrefix, HTMaterialKey, HTBlockHolderLike<*>> = mutableTableOf()

    @JvmStatic
    val existingItems: HTTable.Mutable<HTTagPrefix, HTMaterialKey, HTItemHolderLike<*>> = mutableTableOf()

    @JvmStatic
    val existingTools: HTTable.Mutable<HTToolType, HTMaterialKey, HTItemHolderLike<*>> = mutableTableOf()

    @JvmStatic
    lateinit var materialBlocks: HTTable<HTTagPrefix, HTMaterialKey, HTBlockHolderLike<*>>
        private set

    @JvmStatic
    lateinit var materialFluids: HTTable<HTFluidTagPrefix, HTMaterialKey, HTFluidHolderLike<*>>
        private set

    @JvmStatic
    lateinit var materialItems: HTTable<HTTagPrefix, HTMaterialKey, HTItemHolderLike<*>>
        private set

    @JvmStatic
    lateinit var materialTools: HTTable<HTToolType, HTMaterialKey, HTItemHolderLike<*>>
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
    }

    //    Initialization    //

    @JvmStatic
    private fun initMaterials() {
        if (!hasInit) {
            // 素材のプロパティを定義する
            gatherProperties()
            // 既存の素材ブロックを登録する
            registerExistingBlocks()
            // 既存の素材アイテムを登録する
            registerExistingItems()
            // 既存の素材ツールを登録する
            registerExistingTools()
            hasInit = true
        }
    }

    @JvmStatic
    private fun gatherProperties() {
        val builderMap: MutableMap<HTMaterialKey, HTPropertyMap.Mutable> = mutableMapOf()
        ModLoader.postEvent(
            HTMaterialPropertyEvent { key: HTMaterialKey -> builderMap.computeIfAbsent(key) { HTBasicPropertyMap.Mutable() } },
        )
        HiiragiCoreAccessImpl.materialManagerCache = builderMap.filterValues(HTPropertyMap::isNotEmpty).let(::HTMaterialManagerImpl)
        HiiragiCoreAPI.LOGGER.info("Gathered Material Properties")
    }

    @JvmStatic
    private fun registerExistingBlocks() {
        ModLoader.postEvent(HTRegisterExistingPartEvent.BlockEvent(existingBlocks::put))

        HiiragiCoreAPI.LOGGER.info("Registered Existing Material Blocks")
    }

    @JvmStatic
    private fun registerExistingItems() {
        ModLoader.postEvent(HTRegisterExistingPartEvent.ItemEvent(existingItems::put))

        HiiragiCoreAPI.LOGGER.info("Registered Existing Material Items")
    }

    @JvmStatic
    private fun registerExistingTools() {
        ModLoader.postEvent(HTRegisterExistingPartEvent.ToolEvent(existingTools::put))

        HiiragiCoreAPI.LOGGER.info("Registered Existing Material Tools")
    }

    //    Register    //

    @JvmStatic
    private fun registerMaterialBlocks(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Block>) {
        // 素材ブロックを生成する
        materialBlocks = manager
            .toFlatTable { entry: HTMaterialManager.Entry ->
                entry
                    .getOrDefault(HTMaterialPropertyKeys.BLOCK_PREFIXES)
                    .mapNotNull { prefix: HTTagPrefix ->
                        val properties: BlockBehaviour.Properties = prefix[HTTagPropertyKeys.BLOCK_PROP] ?: return@mapNotNull null
                        val id: ResourceLocation = prefix.createId(entry)
                        val block = Block(properties)
                        helper.register(id, block)
                        Triple(prefix, entry.asMaterialKey(), HTBlockHolderLike.of(block))
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
                    .map { prefix: HTFluidTagPrefix ->
                        val id: ResourceLocation = prefix.createId(entry)

                        val typeHolder: HTDeferredHolder<FluidType, FluidType> = HTDeferredHolder(
                            NeoForgeRegistries.Keys.FLUID_TYPES,
                            id,
                        )
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

                        val content = HTFluidContent(
                            typeHolder,
                            HTDeferredFluid(id),
                            HTSimpleDeferredItem(bucketId),
                            Registries.FLUID.createCommonTag(id.path),
                            Registries.ITEM.createCommonTag(bucketId.path),
                            null,
                            null,
                        )
                        val fluid: HTVirtualFluid = Registry.register(BuiltInRegistries.FLUID, id, HTVirtualFluid(content))
                        helper.register(
                            bucketId,
                            BucketItem(fluid, Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)),
                        )

                        Triple(prefix, entry.asMaterialKey(), content)
                    }
            }
    }

    @JvmStatic
    private fun registerMaterialItems(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材ブロックのアイテムを生成する
        materialBlocks.forEach { (_, _, block: HTBlockHolderLike<*>) ->
            val id: ResourceLocation = block.getId()
            helper.register(id, HTBlockItem(block.asBlock(), Item.Properties()))
        }
        // 素材アイテムを生成する
        materialItems = manager
            .toFlatTable { entry: HTMaterialManager.Entry ->
                entry
                    .getOrDefault(HTMaterialPropertyKeys.ITEM_PREFIXES)
                    .map { prefix: HTTagPrefix ->
                        val id: ResourceLocation = prefix.createId(entry)
                        helper.register(id, Item(Item.Properties()))
                        Triple(prefix, entry.asMaterialKey(), HTSimpleDeferredItem(id))
                    }
            }
    }

    @JvmStatic
    private fun registerMaterialTools(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材ツールを生成する
        materialTools = manager
            .toFlatTable { entry: HTMaterialManager.Entry ->
                val material: HTToolMaterial =
                    entry[HTMaterialPropertyKeys.TOOL_MATERIAL] ?: return@toFlatTable setOf()
                entry
                    .getOrDefault(HTMaterialPropertyKeys.TOOL_PREFIXES)
                    .map { toolType: HTToolType ->
                        val id: ResourceLocation = toolType.createId(entry)
                        helper.register(id, toolType.createTool(material))
                        Triple(toolType, entry.asMaterialKey(), HTSimpleDeferredItem(id))
                    }
            }
    }
}
