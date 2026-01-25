package hiiragi283.core.api.item.tool

import hiiragi283.core.api.item.equipment.HTEquipmentMaterial
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Tier
import net.minecraft.world.item.component.Tool
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

/**
 * [道具の素材][Tier]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.tools.common.material.BaseMekanismMaterial
 */
interface HTToolMaterial :
    HTEquipmentMaterial,
    Tier {
    fun getSwordDamage(): Float = 3f

    fun getSwordAttackSpeed(): Float = -2.4f

    fun getShovelDamage(): Float = 1.5f

    fun getShovelAttackSpeed(): Float = -3f

    fun getAxeDamage(): Float

    fun getAxeAttackSpeed(): Float

    fun getPickaxeDamage(): Float = 1f

    fun getPickaxeAttackSpeed(): Float = -2.8f

    fun getHoeDamage(): Float = -attackDamageBonus

    fun getHoeAttackSpeed(): Float = attackDamageBonus - 3f

    /**
     * 既存の[Tier]に実装を委譲した[HTToolMaterial]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    interface Delegated : HTToolMaterial {
        fun getTier(): Tier

        override fun getUses(): Int = getTier().uses

        override fun getSpeed(): Float = getTier().speed

        override fun getAttackDamageBonus(): Float = getTier().attackDamageBonus

        override fun getIncorrectBlocksForDrops(): TagKey<Block> = getTier().incorrectBlocksForDrops

        override fun getEnchantmentValue(): Int = getTier().enchantmentValue

        override fun getRepairIngredient(): Ingredient = getTier().repairIngredient

        override fun createToolProperties(block: TagKey<Block>): Tool = getTier().createToolProperties(block)
    }
}
