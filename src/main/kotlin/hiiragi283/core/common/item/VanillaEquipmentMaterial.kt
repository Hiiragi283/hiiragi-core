package hiiragi283.core.common.item

import hiiragi283.core.api.item.HTEquipmentMaterial
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.common.material.HCMaterial
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.ArmorMaterials
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

enum class VanillaEquipmentMaterial(
    private val material: HTMaterialLike,
    private val tier: Tier,
    private val axeDamage: Float,
    private val axeAttackSpeed: Float,
    private val armor: Holder<ArmorMaterial>,
    private val armorMultiplier: Int,
) : HTEquipmentMaterial,
    HTMaterialLike by material {
    WOOD(HCMaterial.Wood, Tiers.WOOD, 6f, -3.2f, ArmorMaterials.LEATHER, 5),
    STONE(HTMaterialKey.of("stone"), Tiers.STONE, 7f, -3.2f, ArmorMaterials.CHAIN, 15),
    IRON(HCMaterial.Metals.IRON, Tiers.IRON, 6f, -3.1f, ArmorMaterials.IRON, 15),
    GOLD(HCMaterial.Metals.GOLD, Tiers.GOLD, 6f, -3f, ArmorMaterials.GOLD, 7),
    DIAMOND(HCMaterial.Gems.DIAMOND, Tiers.DIAMOND, 5f, -3f, ArmorMaterials.DIAMOND, 33),
    NETHERITE(HCMaterial.Alloys.NETHERITE, Tiers.NETHERITE, 5f, -3f, ArmorMaterials.NETHERITE, 37),
    ;

    override fun getAxeDamage(): Float = axeDamage

    override fun getAxeAttackSpeed(): Float = axeAttackSpeed

    override fun getToughness(): Float = armor.value().toughness

    override fun getKnockbackResistance(): Float = armor.value().knockbackResistance

    override fun getEquipSound(): Holder<SoundEvent> = armor.value().equipSound

    override fun getArmorDefence(type: ArmorItem.Type): Int = armor.value().getDefense(type)

    override fun getArmorMultiplier(): Int = armorMultiplier

    override fun getUses(): Int = tier.uses

    override fun getSpeed(): Float = tier.speed

    override fun getAttackDamageBonus(): Float = tier.attackDamageBonus

    override fun getIncorrectBlocksForDrops(): TagKey<Block> = tier.incorrectBlocksForDrops

    override fun getEnchantmentValue(): Int = tier.enchantmentValue

    override fun getRepairIngredient(): Ingredient = tier.repairIngredient
}
