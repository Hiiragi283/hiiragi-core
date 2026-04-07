package hiiragi283.core.common.item.tool

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.item.tool.HTCraftingToolItem
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.tag.HiiragiCoreTags
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ShearsItem
import net.neoforged.neoforge.common.Tags

data object CommonToolTypes {
    @JvmField
    val HAMMER: HTToolType = HTToolType.create("hammer") {
        factory = ::HTCraftingToolItem
        langPattern = HTLangPatternProvider.create("%s Hammer", "%sのハンマー")
        recipePattern = listOf(" B ", " B ", "ABA")
        toolTags += HiiragiCoreTags.Items.HAMMERS
        toolTags += HiiragiCoreTags.Items.TOOLS_HAMMER
    }

    @JvmField
    val SHEAR: HTToolType = HTToolType.create("shear") {
        factory = { material: HTToolMaterial, prop: Item.Properties ->
            ShearsItem(prop.durability(material.uses).component(DataComponents.TOOL, ShearsItem.createToolProperties()))
        }
        langPattern = HTLangPatternProvider.create("%s Shear", "%sのハサミ")
        recipePattern = listOf(" A", "A ")
        toolTags += Tags.Items.TOOLS_SHEAR
    }
}
