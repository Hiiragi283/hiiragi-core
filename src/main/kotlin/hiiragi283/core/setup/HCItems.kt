package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.item.endgame.HTAmbrosiaItem
import hiiragi283.core.common.item.endgame.HTCreativeItem
import hiiragi283.core.common.item.endgame.HTEternalUpgradeItem
import hiiragi283.core.common.item.endgame.HTInfinityPotionItem
import hiiragi283.lib.item.component.buildItemAttributeModifiers
import hiiragi283.lib.item.component.consumables
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.Consumables
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.NeoForgeMod

data object HCItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Resources    //

    // Vanilla
    @JvmField
    val NETHERITE_NUGGET: HTSimpleDeferredItem = REGISTER.registerSimpleItem("netherite_nugget") { it.fireResistant() }

    @JvmField
    val ENDER_PEARL_DUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("ender_pearl_dust")

    @JvmField
    val OBSIDIAN_DUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("obsidian_dust")

    // Common
    @JvmField
    val RAW_IRIDIUM: HTSimpleDeferredItem = REGISTER.registerSimpleItem("raw_iridium") { it.rarity(Rarity.RARE) }

    @JvmField
    val IRIDIUM_DUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("iridium_dust") { it.rarity(Rarity.RARE) }

    @JvmField
    val IRIDIUM_INGOT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("iridium_ingot") { it.rarity(Rarity.RARE) }

    @JvmField
    val IRIDIUM_NUGGET: HTSimpleDeferredItem = REGISTER.registerSimpleItem("iridium_nugget") { it.rarity(Rarity.RARE) }

    // Mobs
    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart") { it.rarity(Rarity.UNCOMMON) }

    //    Ingredients    //

    // Synthetic
    @JvmField
    val SYNTHETIC_FEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_feather")

    @JvmField
    val SYNTHETIC_FIBER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_fiber")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_leather")

    //    End Game    //

    @JvmField
    val IRIDESCENT_POWDER: HTSimpleDeferredItem = REGISTER.registerItem("iridescent_powder", ::HTCreativeItem)

    @JvmField
    val AMBROSIA: HTSimpleDeferredItem = REGISTER.registerItem("ambrosia", ::HTAmbrosiaItem) { it.food(HCFoods.AMBROSIA) }

    @JvmField
    val ETERNAL_UPGRADE: HTSimpleDeferredItem = REGISTER.registerItem("eternal_upgrade", ::HTEternalUpgradeItem)

    @JvmField
    val POTION_OF_INFINITY: HTSimpleDeferredItem = REGISTER.registerItem("potion_of_infinity", ::HTInfinityPotionItem) { it.consumables(Consumables.DEFAULT_DRINK) }

    @JvmField
    val RING_OF_HYPERION: HTSimpleDeferredItem = REGISTER.registerItem("ring_of_hyperion", ::HTCreativeItem) {
        it.attributes(
            buildItemAttributeModifiers {
                add(
                    NeoForgeMod.CREATIVE_FLIGHT,
                    AttributeModifier(
                        HiiragiCoreAPI.id("ring_of_hyperion"),
                        1.0,
                        AttributeModifier.Operation.ADD_VALUE,
                    ),
                    EquipmentSlotGroup.OFFHAND,
                )
            },
        )
    }
}
