package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.item.HTEquipmentMaterial
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.item.HTAmbrosiaItem
import hiiragi283.core.common.item.HTCreativeItem
import hiiragi283.core.common.item.HTToolType
import hiiragi283.core.common.item.HTTraderCatalogItem
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

object HCItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::modifyComponents)
    }

    //    Materials   //

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTMaterialPrefix, HTSimpleDeferredItem> = buildTable {
        for (material: HCMaterial in HCMaterial.entries) {
            for (prefix: HTMaterialPrefix in material.getItemPrefixesToGenerate()) {
                this[prefix.asMaterialPrefix(), material.asMaterialKey()] = REGISTER.registerSimpleItem(material.createPath(prefix))
            }
        }
    }.let(::HTMaterialTable)

    @JvmField
    val BAMBOO_CHARCOAL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("bamboo_charcoal")

    @JvmField
    val COMPRESSED_SAWDUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("compressed_sawdust")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_leather")

    // Mob
    @JvmField
    val LUMINOUS_PASTE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("luminous_paste")

    @JvmField
    val MAGMA_SHARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("magma_shard")

    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart") {
        it.description(HCTranslation.ELDER_HEART)
    }

    @JvmField
    val WITHER_DOLL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_doll")

    @JvmField
    val WITHER_STAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_star")

    //    Foods   //

    @JvmField
    val WHEAT_FLOUR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wheat_flour")

    @JvmField
    val WHEAT_DOUGH: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wheat_dough")

    @JvmField
    val ANIMAL_FAT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("animal_fat")

    @JvmField
    val PULPED_FISH: HTSimpleDeferredItem = REGISTER.registerSimpleItem("pulped_fish")

    @JvmField
    val PULPED_SEED: HTSimpleDeferredItem = REGISTER.registerSimpleItem("pulped_seed")

    //    Tools   //

    @JvmStatic
    val TOOLS: HTMaterialTable<HTToolType<*>, HTDeferredItem<*>> = buildTable {
        fun register(toolType: HTToolType<*>, material: HTEquipmentMaterial) {
            this[toolType, material.asMaterialKey()] = toolType.createItem(REGISTER, material)
        }
    }.let(::HTMaterialTable)

    //    Utilities    //

    @JvmField
    val SLOT_COVER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("slot_cover") {
        it.description(HCTranslation.SLOT_COVER)
    }

    @JvmField
    val TRADER_CATALOG: HTSimpleDeferredItem = REGISTER.registerItem("trader_catalog", ::HTTraderCatalogItem) {
        it.description(HCTranslation.TRADER_CATALOG)
    }

    //    End Game    //

    @JvmField
    val IRIDESCENT_POWDER: HTSimpleDeferredItem = REGISTER.registerItem("iridescent_powder", ::HTCreativeItem) {
        it.description(HCTranslation.IRIDESCENT_POWDER)
    }

    @JvmField
    val AMBROSIA: HTSimpleDeferredItem = REGISTER.registerItem("ambrosia", ::HTAmbrosiaItem) {
        it.description(HCTranslation.AMBROSIA)
    }

    @JvmField
    val ETERNAL_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("eternal_ticket", ::HTCreativeItem) {
        it.description(HCTranslation.ETERNAL_TICKET)
    }

    //    Event    //

    @JvmStatic
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : Any> modify(item: ItemLike, type: DataComponentType<T>, value: T) {
            event.modify(item) { builder: DataComponentPatch.Builder -> builder.set(type, value) }
        }
    }

    //    Extensions    //

    private fun Item.Properties.description(translation: HTTranslation): Item.Properties =
        this.component(HCDataComponents.DESCRIPTION, translation)
}
