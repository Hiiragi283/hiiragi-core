package hiiragi283.core.api.item.armor

import hiiragi283.core.api.item.equipment.HTEquipmentMaterial
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient

/**
 * [防具の素材][ArmorMaterial]をまとめたインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.tools.common.material.BaseMekanismMaterial
 */
interface HTArmorMaterial : HTEquipmentMaterial {
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

    /**
     * 既存の[ArmorMaterial]に実装を委譲した[HTArmorMaterial]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    interface Delegated :
        HTArmorMaterial,
        HTIdLike {
        fun getHolder(): Holder<ArmorMaterial>

        override fun getToughness(): Float = getHolder().value().toughness()

        override fun getKnockbackResistance(): Float = getHolder().value().knockbackResistance()

        override fun getEquipSound(): Holder<SoundEvent> = getHolder().value().equipSound()

        override fun getArmorDefence(type: ArmorItem.Type): Int = getHolder().value().getDefense(type)

        override fun getEnchantmentValue(): Int = getHolder().value().enchantmentValue()

        override fun getRepairIngredient(): Ingredient = getHolder().value().repairIngredient().get()

        override fun getId(): ResourceLocation = getHolder().getKeyOrThrow().location()
    }
}
