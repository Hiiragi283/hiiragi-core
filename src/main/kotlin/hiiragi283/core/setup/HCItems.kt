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

    @JvmField
    val NETHERITE_NUGGET: HTSimpleDeferredItem = REGISTER.registerSimpleItem("netherite_nugget") { it.fireResistant() }

    //    Resources    //

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
