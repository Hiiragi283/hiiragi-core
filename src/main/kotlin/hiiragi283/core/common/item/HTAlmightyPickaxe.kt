package hiiragi283.core.common.item

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.component.Tool
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.ItemAbility
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet

class HTAlmightyPickaxe(properties: Properties) :
    DiggerItem(AlmightyTier, HiiragiCoreTags.Blocks.INCORRECT_FOR_ALMIGHTY_PICKAXE, properties.rarity(Rarity.RARE)),
    HTSubCreativeTabContents {
    override fun getName(stack: ItemStack): Text = when (stack.has(DataComponents.UNBREAKABLE)) {
        true -> HCTranslation.ETERNAL_PICKAXE.translateColored(HTDefaultColor.RED)
        false -> translatableText(getDescriptionId(stack))
    }

    override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || stack.has(DataComponents.UNBREAKABLE)

    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean = itemAbility.name().endsWith("_dig")

    override fun canBeHurtBy(stack: ItemStack, source: DamageSource): Boolean = false

    //    HTSubCreativeTabContents    //

    override fun addItems(baseItem: HTItemHolderLike<*>, context: HTSubCreativeTabContents.Context) {
        createItemStack(baseItem, DataComponents.UNBREAKABLE, Unbreakable(true)).let(context)
    }

    //    Tier    //

    data object AlmightyTier : Tier {
        override fun getUses(): Int = Tiers.NETHERITE.uses

        override fun getSpeed(): Float = Tiers.NETHERITE.speed

        override fun getAttackDamageBonus(): Float = Tiers.NETHERITE.attackDamageBonus

        override fun getIncorrectBlocksForDrops(): TagKey<Block> = HiiragiCoreTags.Blocks.INCORRECT_FOR_ALMIGHTY_PICKAXE

        override fun getEnchantmentValue(): Int = Tiers.NETHERITE.enchantmentValue

        override fun getRepairIngredient(): Ingredient = HCEternalSmithingRecipe.ADDITIONAL_TAG

        override fun createToolProperties(block: TagKey<Block>): Tool = Tool(
            listOf(
                Tool.Rule.deniesDrops(incorrectBlocksForDrops),
                Tool.Rule(
                    AnyHolderSet(BuiltInRegistries.BLOCK.asLookup()),
                    40f.wrapOptional(),
                    true.wrapOptional(),
                ),
            ),
            1f,
            1,
        )
    }
}
