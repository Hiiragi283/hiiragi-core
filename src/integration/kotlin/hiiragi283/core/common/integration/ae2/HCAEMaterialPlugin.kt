package hiiragi283.core.common.integration.ae2

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import appeng.core.definitions.BlockDefinition
import appeng.core.definitions.ItemDefinition
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.plugin.HTPlugin
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.resource.SupplierWithKey
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.HCIntegrationMaterialKeys
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

@HTPlugin
data object HCAEMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = 0

    override fun getId(): ResourceLocation = HCIConstants.AE2.toId("material_plugin", HiiragiCoreAPI.MOD_ID)

    override fun registerExistingBlock(consumer: HTMaterialPlugin.BlockConsumer) {
        // Gem
        consumer.accept(CommonParts.BLOCK, HCIntegrationMaterialKeys.CERTUS_QUARTZ, BlockDefinitionWrapper(AEBlocks.QUARTZ_BLOCK))
        consumer.accept(CommonParts.BLOCK, HCIntegrationMaterialKeys.FLUIX, BlockDefinitionWrapper(AEBlocks.FLUIX_BLOCK))
    }

    override fun registerExistingItem(consumer: HTMaterialPlugin.ItemConsumer) {
        // Gem
        consumer.accept(CommonParts.DUST, HCIntegrationMaterialKeys.CERTUS_QUARTZ, ItemDefinitionWrapper(AEItems.CERTUS_QUARTZ_DUST))
        consumer.accept(CommonParts.GEM, HCIntegrationMaterialKeys.CERTUS_QUARTZ, ItemDefinitionWrapper(AEItems.CERTUS_QUARTZ_CRYSTAL))

        consumer.accept(CommonParts.DUST, HCIntegrationMaterialKeys.FLUIX, ItemDefinitionWrapper(AEItems.FLUIX_DUST))
        consumer.accept(CommonParts.GEM, HCIntegrationMaterialKeys.FLUIX, ItemDefinitionWrapper(AEItems.FLUIX_CRYSTAL))
        // Other
        consumer.accept(CommonParts.DUST, HCIntegrationMaterialKeys.SKY_STONE, ItemDefinitionWrapper(AEItems.SKY_DUST))
    }

    override fun modifyMaterial(provider: HTMaterialPlugin.MaterialProvider) {
        // Gem
        provider.getBuilder(HCIntegrationMaterialKeys.CERTUS_QUARTZ).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(CommonParts.RAW, CommonParts.CRUSHED_ORE)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Certus Quartz", "ケルタスクォーツ")
            setTextureSet("amethyst")
        }
        provider.getBuilder(HCIntegrationMaterialKeys.FLUIX).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Fluix Crystal", "フルーシュ")
            setTextureSet("amethyst")
        }
        // Other
        provider.getBuilder(HCIntegrationMaterialKeys.SKY_STONE).apply {
            setName("Sky Stone", "スカイストーン")
        }
    }

    //    Extensions    //

    @JvmInline
    value class BlockDefinitionWrapper<BLOCK : Block>(val definition: BlockDefinition<BLOCK>) : SupplierWithKey<Block, BLOCK> {
        override fun get(): BLOCK = definition.block()

        override fun getKey(): ResourceKey<Block> = Registries.BLOCK.createKey(definition.id())
    }

    @JvmInline
    value class ItemDefinitionWrapper<ITEM : Item>(val definition: ItemDefinition<ITEM>) : SupplierWithKey<Item, ITEM> {
        override fun get(): ITEM = definition.asItem()

        override fun getKey(): ResourceKey<Item> = Registries.ITEM.createKey(definition.id())
    }
}
