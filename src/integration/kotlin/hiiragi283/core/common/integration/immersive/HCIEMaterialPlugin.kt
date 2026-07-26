package hiiragi283.core.common.integration.immersive

import blusunrize.immersiveengineering.api.EnumMetals
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.common.register.IEItems
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialLevel
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.plugin.HTPlugin
import hiiragi283.core.api.property.plusAssign
import hiiragi283.core.api.registry.HTDeferredBlockAndItem
import hiiragi283.core.api.registry.HTDeferredItem
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.times
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCIntegrationMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.material.part.HCIntegrationParts
import hiiragi283.core.common.tag.HCIntegrationTagPrefixes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import org.apache.commons.lang3.math.Fraction

@HTPlugin
data object HCIEMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = 0

    override fun getId(): ResourceLocation = HCIConstants.IMMERSIVE.toId("material_plugin", HiiragiCoreAPI.MOD_ID)

    override fun registerPart(registrar: HTMaterialPlugin.PartRegistrar) {
        registrar.register("sheetmetal", "%s_sheetmetal") {
            put(HTPartPropertyKeys.ITEM_SCALE) { base: Fraction, _ -> base * 4 }
            put(HTPartPropertyKeys.TAG_PREFIX, HCIntegrationTagPrefixes.SHEETMETAL)
        }
    }

    override fun registerExistingBlock(consumer: HTMaterialPlugin.BlockConsumer) {
        for ((metals: EnumMetals, block: IEBlocks.BlockEntry<*>) in IEBlocks.Metals.SHEETMETAL) {
            val key: HTMaterialKey = when (metals) {
                EnumMetals.COPPER -> VanillaMaterialKeys.COPPER
                EnumMetals.ALUMINUM -> CommonMaterialKeys.ALUMINUM
                EnumMetals.LEAD -> CommonMaterialKeys.LEAD
                EnumMetals.SILVER -> CommonMaterialKeys.SILVER
                EnumMetals.NICKEL -> CommonMaterialKeys.NICKEL
                EnumMetals.URANIUM -> CommonMaterialKeys.URANIUM
                EnumMetals.CONSTANTAN -> CommonMaterialKeys.CONSTANTAN
                EnumMetals.ELECTRUM -> CommonMaterialKeys.ELECTRUM
                EnumMetals.STEEL -> CommonMaterialKeys.STEEL
                EnumMetals.IRON -> VanillaMaterialKeys.IRON
                EnumMetals.GOLD -> VanillaMaterialKeys.GOLD
            }
            consumer.accept(HCIntegrationParts.SHEETMETAL, key, block.toHolder())
        }
    }

    override fun registerExistingItem(consumer: HTMaterialPlugin.ItemConsumer) {
        consumer.accept(CommonParts.DUST, HCIntegrationMaterialKeys.HOP_GRAPHITE, IEItems.Ingredients.DUST_HOP_GRAPHITE.toHolder())
        // consumer.accept(CommonParts.INGOT, HCIntegrationMaterialKeys.HOP_GRAPHITE, ItemEntryWrapper(IEItems.Ingredients.INGOT_HOP_GRAPHITE))
        consumer.accept(CommonParts.PLATE, HCIntegrationMaterialKeys.HOP_GRAPHITE, IEItems.Ingredients.PLATE_HOP_GRAPHITE.toHolder())
    }

    override fun modifyMaterial(provider: HTMaterialPlugin.MaterialProvider) {
        provider.getBuilder(HCIntegrationMaterialKeys.HOP_GRAPHITE).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            set(HTMaterialPropertyKeys.HARDNESS, HTMaterialLevel.MEDIUM)
            set(HTMaterialPropertyKeys.MELTING_POINT, HTMaterialLevel.NONE)
            this += HTMaterialPropertyKeys.DISABLE_SMELTING
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HCIConstants.IMMERSIVE)

            setName("HOP Graphite", "高配向パイログラファイト")
            put(HTMaterialPropertyKeys.TEXTURE_COLOR, CommonMaterialKeys.CARBON.getId())
        }
    }

    //    Extensions    //

    fun <BLOCK : Block> IEBlocks.BlockEntry<BLOCK>.toHolder(): HTDeferredBlockAndItem<BLOCK, Item> = HTDeferredBlockAndItem(this.id)

    fun <ITEM : Item> IEItems.ItemRegObject<ITEM>.toHolder(): HTDeferredItem<ITEM> = HTDeferredItem(this.id)
}
