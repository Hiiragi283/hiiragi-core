package hiiragi283.core.common.item

import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.item.HTCraftingToolItem
import hiiragi283.core.api.item.HTEquipmentMaterial
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import hiiragi283.core.common.tag.HCCommonTags
import hiiragi283.core.common.tag.HCModTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility

class HTHammerItem(tier: Tier, properties: Properties) :
    DiggerItem(tier, HCModTags.Blocks.MINEABLE_WITH_HAMMER, properties),
    HTCraftingToolItem {
    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean =
        itemAbility in setOf(ItemAbilities.PICKAXE_DIG, ItemAbilities.SHOVEL_DIG)

    data object ToolType : HTToolType<HTHammerItem> {
        override fun getToolTags(): List<TagKey<Item>> = listOf(HCCommonTags.Items.TOOLS_HAMMER)

        override fun createItem(
            register: HTDeferredItemRegister,
            material: HTEquipmentMaterial,
            name: String,
        ): HTDeferredItem<HTHammerItem> = register.registerItemWith(name, material, ::HTHammerItem) {
            it.attributes(createAttributes(material, material.getPickaxeDamage(), material.getPickaxeAttackSpeed()))
        }

        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> "$value Hammer"
            HTLanguageType.JA_JP -> "${value}のハンマー"
        }

        override fun getSerializedName(): String = "hammer"
    }
}
