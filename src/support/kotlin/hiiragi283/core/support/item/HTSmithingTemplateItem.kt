package hiiragi283.core.support.item

import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.api.text.HTTranslation
import net.minecraft.world.item.SmithingTemplateItem

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[SmithingTemplateItem]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
open class HTSmithingTemplateItem(
    appliesTo: HTTranslation,
    ingredients: HTTranslation,
    description: HTTranslation,
    baseSlot: HTTranslation,
    additionsSlot: HTTranslation,
) : SmithingTemplateItem(
    appliesTo.translateColored(HTDefaultColor.BLUE),
    ingredients.translateColored(HTDefaultColor.BLUE),
    description.translateColored(HTDefaultColor.GRAY),
    baseSlot.translate(),
    additionsSlot.translate(),
    listOf(
        "item/empty_armor_slot_helmet",
        "item/empty_armor_slot_chestplate",
        "item/empty_armor_slot_leggings",
        "item/empty_armor_slot_boots",
        "item/empty_slot_hoe",
        "item/empty_slot_axe",
        "item/empty_slot_sword",
        "item/empty_slot_shovel",
        "item/empty_slot_pickaxe",
    ).map(::vanillaId),
    listOf(vanillaId("item", "empty_slot_ingot")),
)
