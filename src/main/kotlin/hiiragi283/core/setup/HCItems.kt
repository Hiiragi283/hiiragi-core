package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.capability.HTItemCapabilities
import hiiragi283.core.common.item.HTAlmightyPickaxe
import hiiragi283.core.common.item.HTAmbrosiaItem
import hiiragi283.core.common.item.HTCaptureEggItem
import hiiragi283.core.common.item.HTCreativeItem
import hiiragi283.core.common.item.HTEternalUpgradeItem
import hiiragi283.core.common.item.HTFluidFilterItem
import hiiragi283.core.common.item.HTItemFilterItem
import hiiragi283.core.common.item.HTTraderCatalogItem
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import hiiragi283.core.common.storage.fluid.HTComponentFluidTank
import hiiragi283.core.common.storage.item.HTComponentItemSlot
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.food.FoodConstants
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

object HCItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::modifyComponents)
        eventBus.addListener(::registerCapabilities)
    }

    //    Materials   //

    @JvmField
    val BAMBOO_CHARCOAL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("bamboo_charcoal")

    @JvmField
    val COMPRESSED_SAWDUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("compressed_sawdust")

    @JvmField
    val POLYMER_RESIN: HTSimpleDeferredItem = REGISTER.registerSimpleItem("polymer_resin")

    @JvmField
    val RAW_RUBBER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("raw_rubber")

    @JvmField
    val STEEL_COMPOUND: HTSimpleDeferredItem = REGISTER.registerSimpleItem("steel_compound")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_leather")

    // Mob
    @JvmField
    val LUMINOUS_PASTE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("luminous_paste")

    @JvmField
    val MAGMA_SHARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("magma_shard")

    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart")

    @JvmField
    val WITHER_DOLL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_doll")

    @JvmField
    val WITHER_STAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_star")

    //    Tools   //

    //    Utilities    //

    @JvmField
    val ELDRITCH_EGG: HTSimpleDeferredItem = REGISTER.registerItem("eldritch_egg", ::HTCaptureEggItem)

    @JvmField
    val FLUID_FILTER: HTSimpleDeferredItem = REGISTER.registerItem("fluid_filter", ::HTFluidFilterItem)

    @JvmField
    val ITEM_FILTER: HTSimpleDeferredItem = REGISTER.registerItem("item_filter", ::HTItemFilterItem)

    @JvmField
    val SLOT_COVER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("slot_cover")

    @JvmField
    val TRADER_CATALOG: HTSimpleDeferredItem = REGISTER.registerItem("trader_catalog", ::HTTraderCatalogItem)

    //    End Game    //

    @JvmField
    val IRIDESCENT_POWDER: HTSimpleDeferredItem = REGISTER.registerItem("iridescent_powder", ::HTCreativeItem)

    @JvmField
    val AMBROSIA: HTSimpleDeferredItem = REGISTER.registerItem("ambrosia", ::HTAmbrosiaItem) {
        it
            .food(
                FoodProperties
                    .Builder()
                    .nutrition(FoodConstants.MAX_FOOD)
                    .saturationModifier(0.5f)
                    .alwaysEdible()
                    .build(),
            )
    }

    @JvmField
    val ETERNAL_UPGRADE: HTSimpleDeferredItem = REGISTER.register("eternal_upgrade", ::HTEternalUpgradeItem)

    @JvmField
    val ALMIGHTY_PICKAXE: HTSimpleDeferredItem = REGISTER.registerItem("almighty_pickaxe", ::HTAlmightyPickaxe)

    //    Event    //

    @JvmStatic
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : Any> modify(item: ItemLike, type: DataComponentType<T>, value: T) {
            event.modify(item) { builder: DataComponentPatch.Builder -> builder.set(type, value) }
        }

        modify(AMBROSIA, HCDataComponents.DESCRIPTION, HCTranslation.AMBROSIA)
        modify(ELDER_HEART, HCDataComponents.DESCRIPTION, HCTranslation.ELDER_HEART)
        modify(ELDRITCH_EGG, HCDataComponents.DESCRIPTION, HCTranslation.ELDRITCH_EGG)
        modify(ETERNAL_UPGRADE, HCDataComponents.DESCRIPTION, HCTranslation.ETERNAL_UPGRADE)
        modify(IRIDESCENT_POWDER, HCDataComponents.DESCRIPTION, HCTranslation.IRIDESCENT_POWDER)
        modify(SLOT_COVER, HCDataComponents.DESCRIPTION, HCTranslation.SLOT_COVER)
        modify(TRADER_CATALOG, HCDataComponents.DESCRIPTION, HCTranslation.TRADER_CATALOG)
    }

    @JvmStatic
    private fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        HTFluidCapabilities.registerItem(event, 9, { HTComponentFluidTank.create(1000, it) }, FLUID_FILTER)

        HTItemCapabilities.registerItem(event, 9, HTComponentItemSlot::create, ITEM_FILTER)
    }
}
