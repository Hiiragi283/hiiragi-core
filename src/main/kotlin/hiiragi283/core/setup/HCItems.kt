package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTDeferredItemRegister
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.core.common.item.HTBlueprintItem
import hiiragi283.core.common.item.HTBombItem
import hiiragi283.core.common.item.HTCaptureEggItem
import hiiragi283.core.common.item.HTExperienceTomeItem
import hiiragi283.core.common.item.HTPaintBrushItem
import hiiragi283.core.common.item.HTTraderCatalogItem
import hiiragi283.core.common.item.endgame.HTAlmightyPickaxeItem
import hiiragi283.core.common.item.endgame.HTAmbrosiaItem
import hiiragi283.core.common.item.endgame.HTCreativeItem
import hiiragi283.core.common.item.endgame.HTEternalUpgradeItem
import hiiragi283.core.common.item.endgame.HTInfinityPotionItem
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.support.item.HTSmithingTemplateItem
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.food.FoodConstants
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.NeoForgeMod

object HCItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.addAlias("plastic_ingot", "plastic_plate")
        REGISTER.addAlias("plastic_wire", "synthetic_fiber")
        REGISTER.addAlias("rubber_ingot", "cured_rubber")
        REGISTER.addAlias("rubber_plate", "cured_rubber")
        REGISTER.addAlias("wood_plate", "particle_board")

        REGISTER.register(eventBus)
    }

    //    Materials   //

    // Wood
    @JvmField
    val BAMBOO_CHARCOAL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("bamboo_charcoal")

    @JvmField
    val PARTICLE_BOARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("particle_board")

    // Metal
    @JvmField
    val STEEL_COMPOUND: HTSimpleDeferredItem = REGISTER.registerSimpleItem("steel_compound")

    // Polymer
    @JvmField
    val RAW_RUBBER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("raw_rubber")

    @JvmField
    val CURED_RUBBER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("cured_rubber")

    @JvmField
    val POLYMER_RESIN: HTSimpleDeferredItem = REGISTER.registerSimpleItem("polymer_resin")

    @JvmField
    val SYNTHETIC_FEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_feather")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_leather")

    @JvmField
    val SYNTHETIC_FIBER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_fiber")

    // Crops
    @JvmField
    val WHEAT_FLOUR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wheat_flour")

    @JvmField
    val WHEAT_DOUGH: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wheat_dough")

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

    @JvmField
    val ANCIENT_UPGRADE: HTSimpleDeferredItem = REGISTER.registerItem("ancient_upgrade") {
        HTSmithingTemplateItem(
            HCTranslation.ANCIENT_UPGRADE_APPLIES_TO,
            HCTranslation.ANCIENT_UPGRADE_INGREDIENTS,
            HCTranslation.ANCIENT_UPGRADE_DESC,
            HCTranslation.ANCIENT_UPGRADE_BASE_SLOT_DESCRIPTION,
            HCTranslation.ANCIENT_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
        )
    }

    @JvmField
    val PAINT_BRUSH: HTSimpleDeferredItem = REGISTER.registerItem("paint_brush", ::HTPaintBrushItem) { it.stacksTo(1) }

    //    Utilities    //

    @JvmField
    val BLUEPRINT: HTSimpleDeferredItem = REGISTER.registerItem("blueprint", ::HTBlueprintItem) {
        it.stacksTo(1).component(HCDataComponents.BLUEPRINT_NUMBER, 0)
    }

    @JvmField
    val BOMB: HTSimpleDeferredItem = REGISTER.registerItem("bomb", ::HTBombItem)

    @JvmField
    val ELDRITCH_EGG: HTSimpleDeferredItem = REGISTER.registerItem("eldritch_egg", ::HTCaptureEggItem)

    @JvmField
    val SLOT_COVER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("slot_cover")

    @JvmField
    val TRADER_CATALOG: HTSimpleDeferredItem = REGISTER.registerItem("trader_catalog", ::HTTraderCatalogItem)

    @JvmField
    val EXPERIENCE_TOME: HTSimpleDeferredItem = REGISTER.registerItem("experience_tome", ::HTExperienceTomeItem)

    //    End Game    //

    @JvmField
    val IRIDESCENT_POWDER: HTSimpleDeferredItem = REGISTER.registerItem("iridescent_powder", ::HTCreativeItem)

    @JvmField
    val ALMIGHTY_PICKAXE: HTSimpleDeferredItem = REGISTER.registerItem("almighty_pickaxe", ::HTAlmightyPickaxeItem)

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
    val ETERNAL_UPGRADE: HTSimpleDeferredItem = REGISTER.registerItem("eternal_upgrade", ::HTEternalUpgradeItem)

    @JvmField
    val POTION_OF_INFINITY: HTSimpleDeferredItem = REGISTER.registerItem("potion_of_infinity", ::HTInfinityPotionItem)

    @JvmField
    val RING_OF_HYPERION: HTSimpleDeferredItem = REGISTER.registerItem("ring_of_hyperion", ::HTCreativeItem) {
        it.attributes(
            ItemAttributeModifiers.builder()
                .add(
                    NeoForgeMod.CREATIVE_FLIGHT,
                    AttributeModifier(
                        HiiragiCoreAPI.id("ring_of_hyperion"),
                        1.0,
                        AttributeModifier.Operation.ADD_VALUE,
                    ),
                    EquipmentSlotGroup.OFFHAND,
                )
                .build(),
        )
    }
}
