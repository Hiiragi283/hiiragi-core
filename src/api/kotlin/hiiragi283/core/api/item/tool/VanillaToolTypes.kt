package hiiragi283.core.api.item.tool

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SwordItem

data object VanillaToolTypes {
    @JvmField
    val SWORD: HTToolType = HTToolType.create("sword") {
        factory = ::SwordItem
        attribute = { material: HTToolMaterial ->
            SwordItem.createAttributes(material, material.getSwordDamage(), material.getSwordAttackSpeed())
        }
        langPattern = HTLangPatternProvider.create("%s Sword", "%sの剣")
        recipePattern = listOf("B", "A", "A")
        toolTags += ItemTags.SWORDS
    }

    @JvmField
    val SHOVEL: HTToolType = HTToolType.create("shovel") {
        factory = ::ShovelItem
        attribute = { material: HTToolMaterial ->
            DiggerItem.createAttributes(material, material.getShovelDamage(), material.getShovelAttackSpeed())
        }
        langPattern = HTLangPatternProvider.create("%s Shovel", "%sのシャベル")
        recipePattern = listOf("B", "B", "A")
        toolTags += ItemTags.SHOVELS
    }

    @JvmField
    val PICKAXE: HTToolType = HTToolType.create("pickaxe") {
        factory = ::PickaxeItem
        attribute = { material: HTToolMaterial ->
            DiggerItem.createAttributes(material, material.getPickaxeDamage(), material.getPickaxeAttackSpeed())
        }
        langPattern = HTLangPatternProvider.create("%s Pickaxe", "%sのツルハシ")
        recipePattern = listOf(" B ", " B ", "AAA")
        toolTags += ItemTags.PICKAXES
    }

    @JvmField
    val AXE: HTToolType = HTToolType.create("axe") {
        factory = ::AxeItem
        attribute = { material: HTToolMaterial ->
            DiggerItem.createAttributes(material, material.getAxeDamage(), material.getAxeAttackSpeed())
        }
        langPattern = HTLangPatternProvider.create("%s Axe", "%sの斧")
        recipePattern = listOf(" B", "AB", "AA")
        toolTags += ItemTags.AXES
    }

    @JvmField
    val HOE: HTToolType = HTToolType.create("hoe") {
        factory = ::HoeItem
        attribute = { material: HTToolMaterial ->
            DiggerItem.createAttributes(material, material.getHoeDamage(), material.getHoeAttackSpeed())
        }
        langPattern = HTLangPatternProvider.create("%s Hoe", "%sのクワ")
        recipePattern = listOf(" B", " B", "AA")
        toolTags += ItemTags.HOES
    }

    @JvmField
    val VANILLA_SET: Set<HTToolType> = setOf(SWORD, SHOVEL, PICKAXE, AXE, HOE)
}
