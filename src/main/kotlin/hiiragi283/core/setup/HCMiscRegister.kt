package hiiragi283.core.setup

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.collection.toFlatTable
import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.common.gui.sync.HTBoolSyncPayload
import hiiragi283.core.common.gui.sync.HTFluidSyncPayload
import hiiragi283.core.common.gui.sync.HTFractionSyncPayload
import hiiragi283.core.common.gui.sync.HTIntSyncPayload
import hiiragi283.core.common.gui.sync.HTLongSyncPayload
import hiiragi283.core.common.material.HTMaterialManagerImpl
import hiiragi283.core.common.registry.HTDeferredBlock
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.RegisterEvent

object HCMiscRegister {
    @JvmStatic
    private var hasInit: Boolean = false

    @JvmStatic
    internal lateinit var materialBlocks: HTTable<HTTagPrefix, HTMaterialKey, HTSimpleDeferredBlock>

    @JvmStatic
    internal lateinit var materialItems: HTTable<HTTagPrefix, HTMaterialKey, HTSimpleDeferredItem>

    @JvmStatic
    internal lateinit var toolItems: HTTable<HTToolType, HTMaterialKey, HTSimpleDeferredItem>

    @JvmStatic
    fun register(event: RegisterEvent) {
        // 素材のプロパティを定義する
        if (!hasInit) {
            HTMaterialManagerImpl.gatherAttributes()
            hasInit = true
        }
        val manager: HTMaterialManager = HTMaterialManager.INSTANCE
        event.register(Registries.BLOCK) { helper ->
            // 素材ブロックを生成する
            materialBlocks = manager
                .toFlatTable { (key: HTMaterialKey, propertyMap: HTPropertyMap) ->
                    propertyMap
                        .getOrDefault(HTMaterialPropertyKeys.BLOCK_PREFIXES)
                        .mapNotNull { prefix: HTTagPrefix ->
                            val properties: BlockBehaviour.Properties = prefix[HTTagPropertyKeys.BLOCK_PROP] ?: return@mapNotNull null
                            val id: ResourceLocation = prefix.createId(key)
                            val block = Block(properties)
                            helper.register(id, block)
                            Registry.register(
                                BuiltInRegistries.ITEM,
                                id,
                                HTBlockItem(block, Item.Properties()),
                            )
                            Triple(prefix, key, HTDeferredBlock(id))
                        }
                }
        }

        event.register(Registries.ITEM) { helper ->
            // 素材アイテムを生成する
            materialItems = manager
                .toFlatTable { (key: HTMaterialKey, propertyMap: HTPropertyMap) ->
                    propertyMap
                        .getOrDefault(HTMaterialPropertyKeys.ITEM_PREFIXES)
                        .map { prefix: HTTagPrefix ->
                            val id: ResourceLocation = prefix.createId(key)
                            helper.register(id, Item(Item.Properties()))
                            Triple(prefix, key, HTDeferredItem.simple(id))
                        }
                }
            // 素材ツールを生成する
            toolItems = manager
                .toFlatTable { (key: HTMaterialKey, propertyMap: HTPropertyMap) ->
                    val material: HTToolMaterial =
                        propertyMap[HTMaterialPropertyKeys.TOOL_MATERIAL] ?: return@toFlatTable setOf()
                    propertyMap
                        .getOrDefault(HTMaterialPropertyKeys.TOOL_PREFIXES)
                        .map { toolType: HTToolType ->
                            val id: ResourceLocation = toolType.createId(key)
                            helper.register(id, toolType.toolFactory.createTool(material, Item.Properties()))
                            Triple(toolType, key, HTDeferredItem.simple(id))
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
        }
    }
}
