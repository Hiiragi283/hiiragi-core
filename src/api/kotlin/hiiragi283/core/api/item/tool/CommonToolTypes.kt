package hiiragi283.core.api.item.tool

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.tag.HiiragiCoreTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.PickaxeItem
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.SwordItem

object CommonToolTypes {
    //    Vanilla    //

    @JvmField
    val SWORD: HTToolType = HTToolType.create("sword") {
        factory = { material: HTToolMaterial, properties: Item.Properties ->
            SwordItem(
                material,
                properties.attributes(
                    SwordItem.createAttributes(material, material.getSwordDamage(), material.getSwordAttackSpeed()),
                ),
            )
        }
        langPattern = HTLangPatternProvider.create("%s Sword", "%sの剣")
        toolTags += ItemTags.SWORDS
    }

    @JvmField
    val SHOVEL: HTToolType = HTToolType.create("shovel") {
        factory = { material: HTToolMaterial, properties: Item.Properties ->
            ShovelItem(
                material,
                properties.attributes(
                    DiggerItem.createAttributes(material, material.getShovelDamage(), material.getShovelAttackSpeed()),
                ),
            )
        }
        langPattern = HTLangPatternProvider.create("%s Shovel", "%sのシャベル")
        toolTags += ItemTags.SHOVELS
    }

    @JvmField
    val PICKAXE: HTToolType = HTToolType.create("pickaxe") {
        factory = { material: HTToolMaterial, properties: Item.Properties ->
            PickaxeItem(
                material,
                properties.attributes(
                    DiggerItem.createAttributes(material, material.getPickaxeDamage(), material.getPickaxeAttackSpeed()),
                ),
            )
        }
        langPattern = HTLangPatternProvider.create("%s Pickaxe", "%sのツルハシ")
        toolTags += ItemTags.PICKAXES
    }

    @JvmField
    val AXE: HTToolType = HTToolType.create("axe") {
        factory = { material: HTToolMaterial, properties: Item.Properties ->
            AxeItem(
                material,
                properties.attributes(
                    DiggerItem.createAttributes(material, material.getAxeDamage(), material.getAxeAttackSpeed()),
                ),
            )
        }
        langPattern = HTLangPatternProvider.create("%s Axe", "%sの斧")
        toolTags += ItemTags.AXES
    }

    @JvmField
    val HOE: HTToolType = HTToolType.create("hoe") {
        factory = { material: HTToolMaterial, properties: Item.Properties ->
            HoeItem(
                material,
                properties.attributes(
                    DiggerItem.createAttributes(material, material.getHoeDamage(), material.getHoeAttackSpeed()),
                ),
            )
        }
        langPattern = HTLangPatternProvider.create("%s Hoe", "%sのクワ")
        toolTags += ItemTags.HOES
    }

    @JvmField
    val VANILLA_SET: Set<HTToolType> = setOf(SWORD, SHOVEL, PICKAXE, AXE, HOE)

    //    Common    //

    @JvmField
    val HAMMER: HTToolType = HTToolType.create("hammer") {
        factory = ::HTCraftingToolItem
        langPattern = HTLangPatternProvider.create("%s Hammer", "%sのハンマー")
        toolTags += HiiragiCoreTags.Items.TOOLS_HAMMER
    }
}
