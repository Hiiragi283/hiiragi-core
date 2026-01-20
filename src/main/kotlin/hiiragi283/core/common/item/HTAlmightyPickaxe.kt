package hiiragi283.core.common.item

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.common.crafting.HTEternalSmithingRecipe
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.CreativeModeTab
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
import java.util.Optional
import java.util.function.Consumer

class HTAlmightyPickaxe(properties: Properties) :
    DiggerItem(Tiers.NETHERITE, HiiragiCoreTags.Blocks.INCORRECT_FOR_ALMIGHTY_PICKAXE, properties.rarity(Rarity.RARE)),
    HTSubCreativeTabContents {
    override fun getTier(): Tier = object : Tier {
        override fun getUses(): Int = this@HTAlmightyPickaxe.tier.uses

        override fun getSpeed(): Float = this@HTAlmightyPickaxe.tier.speed

        override fun getAttackDamageBonus(): Float = this@HTAlmightyPickaxe.tier.attackDamageBonus

        override fun getIncorrectBlocksForDrops(): TagKey<Block> = HiiragiCoreTags.Blocks.INCORRECT_FOR_ALMIGHTY_PICKAXE

        override fun getEnchantmentValue(): Int = this@HTAlmightyPickaxe.tier.enchantmentValue

        override fun getRepairIngredient(): Ingredient = HTEternalSmithingRecipe.ADDITIONAL_TAG

        override fun createToolProperties(block: TagKey<Block>): Tool = Tool(
            listOf(
                Tool.Rule.deniesDrops(block),
                Tool.Rule(
                    AnyHolderSet(BuiltInRegistries.BLOCK.asLookup()),
                    Optional.of(40f),
                    Optional.of(true),
                ),
            ),
            1f,
            1,
        )
    }

    override fun getName(stack: ItemStack): Component = when (stack.has(DataComponents.UNBREAKABLE)) {
        true -> HCTranslation.ETERNAL_PICKAXE.translateColored(HTDefaultColor.RED)
        false -> translatableText(getDescriptionId(stack))
    }

    override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || stack.has(DataComponents.UNBREAKABLE)

    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean = itemAbility.name().endsWith("_dig")

    override fun canBeHurtBy(stack: ItemStack, source: DamageSource): Boolean = false

    //    HTSubCreativeTabContents    //

    override fun addItems(
        baseItem: HTItemHolderLike<*>,
        parameters: CreativeModeTab.ItemDisplayParameters,
        consumer: Consumer<ItemStack>,
    ) {
        createItemStack(baseItem, DataComponents.UNBREAKABLE, Unbreakable(true)).let(consumer::accept)
    }
}
