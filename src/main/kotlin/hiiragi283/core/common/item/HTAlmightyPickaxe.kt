package hiiragi283.core.common.item

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.item.HTEquipmentMaterial
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.api.text.withStyle
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TieredItem
import net.minecraft.world.item.component.Tool
import net.neoforged.neoforge.common.ItemAbility
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet
import java.util.Optional

class HTAlmightyPickaxe(tier: Tier, properties: Properties) :
    TieredItem(tier, properties.rarity(Rarity.RARE).component(DataComponents.TOOL, createToolProperties())) {
    companion object {
        @JvmStatic
        fun create(material: HTEquipmentMaterial, properties: Properties): HTAlmightyPickaxe = HTAlmightyPickaxe(
            material,
            properties.attributes(DiggerItem.createAttributes(material, material.getPickaxeDamage(), material.getPickaxeAttackSpeed())),
        )

        @JvmStatic
        fun createToolProperties(): Tool = Tool(
            listOf(
                Tool.Rule.deniesDrops(HiiragiCoreTags.Blocks.INCORRECT_FOR_ALMIGHTY_PICKAXE),
                Tool.Rule(
                    AnyHolderSet(BuiltInRegistries.BLOCK.asLookup()),
                    Optional.of(20f),
                    Optional.of(true),
                ),
            ),
            1f,
            1,
        )
    }

    override fun getName(stack: ItemStack): Component = translatableText(getDescriptionId(stack)).withStyle(HTDefaultColor.RED)

    override fun isFoil(stack: ItemStack): Boolean = true

    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean = itemAbility.name().endsWith("_dig")

    override fun canBeHurtBy(stack: ItemStack, source: DamageSource): Boolean = false
}
