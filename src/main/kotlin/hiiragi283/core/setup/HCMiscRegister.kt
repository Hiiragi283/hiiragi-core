package hiiragi283.core.setup

import hiiragi283.core.api.collection.ImmutableTable
import hiiragi283.core.api.collection.immutableTableOf
import hiiragi283.core.api.collection.toFlatTable
import hiiragi283.core.api.item.HTBlockItem
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
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
    internal var materialBlocks: ImmutableTable<HTTagPrefix, HTMaterialKey, HTSimpleDeferredBlock> = immutableTableOf()
        private set

    @JvmStatic
    internal var materialItems: ImmutableTable<HTTagPrefix, HTMaterialKey, HTSimpleDeferredItem> = immutableTableOf()
        private set

    @JvmStatic
    fun register(event: RegisterEvent) {
        // 素材のプロパティを定義する
        if (!hasInit) {
            HTMaterialManagerImpl.gatherAttributes()
            hasInit = true
        }
        val manager: HTMaterialManager = HTMaterialManager.INSTANCE
        // 素材ブロックを生成する
        event.register(Registries.BLOCK) { helper ->
            materialBlocks = manager.entries
                .toFlatTable { (key: HTMaterialKey, propertyMap: HTPropertyMap) ->
                    propertyMap
                        .getOrDefault(HTMaterialPropertyKeys.BLOCK_PREFIXES)
                        .mapNotNull { prefix ->
                            val properties: BlockBehaviour.Properties = prefix[HTTagPropertyKeys.BLOCK_PROP] ?: return@mapNotNull null
                            val id: ResourceLocation = prefix.createId(key)
                            val block = Block(properties)
                            helper.register(id, block)
                            Registry.register(
                                BuiltInRegistries.ITEM,
                                id,
                                HTBlockItem(block, Item.Properties()),
                            )
                            Triple(prefix, key, HTDeferredBlock<Block, HTBlockItem<Block>>(id))
                        }
                }
        }
        // 素材アイテムを生成する
        event.register(Registries.ITEM) { helper ->
            materialItems = manager.entries
                .toFlatTable { (key: HTMaterialKey, propertyMap: HTPropertyMap) ->
                    propertyMap
                        .getOrDefault(HTMaterialPropertyKeys.ITEM_PREFIXES)
                        .map { prefix: HTTagPrefix ->
                            val id: ResourceLocation = prefix.createId(key)
                            helper.register(id, Item(Item.Properties()))
                            Triple(prefix, key, HTDeferredItem.simple(id))
                        }
                }
        }
    }
}
