package hiiragi283.core.common.item

import hiiragi283.core.api.item.armor.HTArmorMaterial
import hiiragi283.core.api.item.tool.HTToolMaterial
import net.minecraft.core.Holder
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.ArmorMaterials
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers

enum class VanillaEquipmentMaterial(
    private val tier: Tier,
    private val axeDamage: Float,
    private val axeAttackSpeed: Float,
    private val armor: Holder<ArmorMaterial>,
    private val armorMultiplier: Int,
) : HTArmorMaterial.Delegated,
    HTToolMaterial.Delegated {
    WOOD(Tiers.WOOD, 6f, -3.2f, ArmorMaterials.LEATHER, 5),
    STONE(Tiers.STONE, 7f, -3.2f, ArmorMaterials.CHAIN, 15),
    IRON(Tiers.IRON, 6f, -3.1f, ArmorMaterials.IRON, 15),
    GOLD(Tiers.GOLD, 6f, -3f, ArmorMaterials.GOLD, 7),
    DIAMOND(Tiers.DIAMOND, 5f, -3f, ArmorMaterials.DIAMOND, 33),
    NETHERITE(Tiers.NETHERITE, 5f, -3f, ArmorMaterials.NETHERITE, 37),
    ;

    override fun getEnchantmentValue(): Int = super<HTToolMaterial.Delegated>.getEnchantmentValue()

    //    HTArmorMaterial    //

    override fun getHolder(): Holder<ArmorMaterial> = armor

    override fun getArmorMultiplier(): Int = armorMultiplier

    //    HTToolMaterial    //

    override fun getTier(): Tier = tier

    override fun getAxeDamage(): Float = axeDamage

    override fun getAxeAttackSpeed(): Float = axeAttackSpeed
}
