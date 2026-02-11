package hiiragi283.core.setup

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.collection.toFlatTable
import hiiragi283.core.api.fluid.HTVirtualFluid
import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.common.gui.sync.HTBoolSyncPayload
import hiiragi283.core.common.gui.sync.HTFluidSyncPayload
import hiiragi283.core.common.gui.sync.HTFractionSyncPayload
import hiiragi283.core.common.gui.sync.HTIntSyncPayload
import hiiragi283.core.common.gui.sync.HTItemSyncPayload
import hiiragi283.core.common.gui.sync.HTLongSyncPayload
import hiiragi283.core.common.material.HTMaterialManagerImpl
import hiiragi283.core.common.registry.HTDeferredBlock
import hiiragi283.core.common.registry.HTDeferredItem
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
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

internal object HCMiscRegister {
    @JvmStatic
    private var hasInit: Boolean = false

    @JvmStatic
    lateinit var materialBlocks: HTTable<HTTagPrefix, HTMaterialKey, HTBlockHolderLike<*, *>>
        private set

    @JvmStatic
    lateinit var moltenFluids: Map<HTMaterialKey, HTFluidContent>
        private set

    @JvmStatic
    lateinit var materialItems: HTTable<HTTagPrefix, HTMaterialKey, HTItemHolderLike<*>>
        private set

    @JvmStatic
    lateinit var toolItems: HTTable<HTToolType, HTMaterialKey, HTItemHolderLike<*>>
        private set

    @JvmStatic
    fun register(event: RegisterEvent) {
        // 素材のプロパティを定義する
        if (!hasInit) {
            HTMaterialManagerImpl.gatherAttributes()
            hasInit = true
        }
        val manager: HTMaterialManager = HiiragiCoreAccess.INSTANCE.materialManager
        event.register(Registries.BLOCK) { helper ->
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
                            Registry.register(
                                BuiltInRegistries.ITEM,
                                id,
                                HTBlockItem(block, Item.Properties()),
                            )
                            Triple(prefix, entry.asMaterialKey(), HTDeferredBlock<Block, Item>(id))
                        }
                }
        }

        event.register(Registries.ITEM) { helper ->
            // 素材液体を追加する
            moltenFluids = manager
                .mapNotNull { entry: HTMaterialManager.Entry ->
                    if (HTMaterialPropertyKeys.GENERATE_MOLTEN !in entry) return@mapNotNull null
                    val id: ResourceLocation = entry.getId().withPrefix("molten_")

                    val typeHolder: HTDeferredHolder<FluidType, FluidType> = HTDeferredHolder(NeoForgeRegistries.Keys.FLUID_TYPES, id)
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
                    val bucketHolder: HTDeferredItem<Item> = HTDeferredItem(bucketId)

                    val fluidHolder: HTDeferredHolder<Fluid, Fluid> = HTDeferredHolder(Registries.FLUID, id)
                    val content = HTFluidContent(
                        typeHolder,
                        fluidHolder,
                        bucketHolder,
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

                    entry.asMaterialKey() to content
                }.toMap()
            // 素材アイテムを生成する
            materialItems = manager
                .toFlatTable { entry: HTMaterialManager.Entry ->
                    entry
                        .getOrDefault(HTMaterialPropertyKeys.ITEM_PREFIXES)
                        .map { prefix: HTTagPrefix ->
                            val id: ResourceLocation = prefix.createId(entry)
                            helper.register(id, Item(Item.Properties()))
                            Triple(prefix, entry.asMaterialKey(), HTItemHolderLike.of(id))
                        }
                }
            // 素材ツールを生成する
            toolItems = manager
                .toFlatTable { entry: HTMaterialManager.Entry ->
                    val material: HTToolMaterial =
                        entry[HTMaterialPropertyKeys.TOOL_MATERIAL] ?: return@toFlatTable setOf()
                    entry
                        .getOrDefault(HTMaterialPropertyKeys.TOOL_PREFIXES)
                        .map { toolType: HTToolType ->
                            val id: ResourceLocation = toolType.createId(entry)
                            helper.register(id, toolType.createTool(material))
                            Triple(toolType, entry.asMaterialKey(), HTItemHolderLike.of(id))
                        }
                }
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
}
