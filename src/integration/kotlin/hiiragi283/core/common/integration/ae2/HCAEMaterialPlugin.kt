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
import hiiragi283.core.api.registry.HTDeferredBlockAndItem
import hiiragi283.core.api.registry.HTDeferredItem
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.HCIntegrationMaterialKeys
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

@HTPlugin
data object HCAEMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = 0

    override fun getId(): ResourceLocation = HCIConstants.AE2.toId("material_plugin", HiiragiCoreAPI.MOD_ID)

    override fun registerExistingBlock(consumer: HTMaterialPlugin.BlockConsumer) {
        // Gem
        consumer.accept(CommonParts.BLOCK, HCIntegrationMaterialKeys.CERTUS_QUARTZ, AEBlocks.QUARTZ_BLOCK.toHolder())
        consumer.accept(CommonParts.BLOCK, HCIntegrationMaterialKeys.FLUIX, AEBlocks.FLUIX_BLOCK.toHolder())
    }

    override fun registerExistingItem(consumer: HTMaterialPlugin.ItemConsumer) {
        // Gem
        consumer.accept(CommonParts.DUST, HCIntegrationMaterialKeys.CERTUS_QUARTZ, AEItems.CERTUS_QUARTZ_DUST.toHolder())
        consumer.accept(CommonParts.GEM, HCIntegrationMaterialKeys.CERTUS_QUARTZ, AEItems.CERTUS_QUARTZ_CRYSTAL.toHolder())

        consumer.accept(CommonParts.DUST, HCIntegrationMaterialKeys.FLUIX, AEItems.FLUIX_DUST.toHolder())
        consumer.accept(CommonParts.GEM, HCIntegrationMaterialKeys.FLUIX, AEItems.FLUIX_CRYSTAL.toHolder())
        // Other
        consumer.accept(CommonParts.DUST, HCIntegrationMaterialKeys.SKY_STONE, AEItems.SKY_DUST.toHolder())
    }

    override fun modifyMaterial(provider: HTMaterialPlugin.MaterialProvider) {
        // Gem
        provider.getBuilder(HCIntegrationMaterialKeys.CERTUS_QUARTZ).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(CommonParts.RAW, CommonParts.CRUSHED_ORE)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HCIConstants.AE2)

            setName("Certus Quartz", "ケルタスクォーツ")
            setTextureSet("amethyst")
        }
        provider.getBuilder(HCIntegrationMaterialKeys.FLUIX).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HCIConstants.AE2)

            setName("Fluix Crystal", "フルーシュ")
            setTextureSet("amethyst")
        }
        // Other
        provider.getBuilder(HCIntegrationMaterialKeys.SKY_STONE).apply {
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HCIConstants.AE2)
            setName("Sky Stone", "スカイストーン")
        }
    }

    //    Extensions    //

    fun <BLOCK : Block> BlockDefinition<BLOCK>.toHolder(): HTDeferredBlockAndItem<BLOCK, BlockItem> = HTDeferredBlockAndItem(this.id())

    fun <ITEM : Item> ItemDefinition<ITEM>.toHolder(): HTDeferredItem<ITEM> = HTDeferredItem(this.id())
}
