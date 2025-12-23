package hiiragi283.core.api.item

import hiiragi283.core.api.material.HTMaterialLike
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Tier

/**
 * [道具の素材][Tier]と[防具の素材][ArmorMaterial]をまとめたインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.tools.common.material.BaseMekanismMaterial
 */
interface HTEquipmentMaterial :
    Tier,
    HTMaterialLike {
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

    //    Armor    //

    /**
     * 防具強度の値を取得します。
     */
    fun getToughness(): Float

    /**
     * ノックバック耐性の値を取得します。
     */
    fun getKnockbackResistance(): Float

    /**
     * 装備時のSEを取得します。
     */
    fun getEquipSound(): Holder<SoundEvent>

    /**
     * [各部位][type]から防御力の値を取得します。
     */
    fun getArmorDefence(type: ArmorItem.Type): Int

    /**
     * 防具の耐久値の倍率を取得します。
     */
    fun getArmorMultiplier(): Int
}
