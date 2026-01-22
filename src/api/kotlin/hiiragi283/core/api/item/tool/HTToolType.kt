package hiiragi283.core.api.item.tool

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.material.HTMaterialLike
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.TieredItem

class HTToolType(
    val name: String,
    private val idPattern: String,
    val toolFactory: ToolFactory,
    val langPattern: HTLangPatternProvider,
    val toolTags: List<TagKey<Item>>,
) : Comparable<HTToolType> {
    companion object {
        @JvmStatic
        private val instances: MutableMap<String, HTToolType> = mutableMapOf()

        @JvmStatic
        fun getAllTypes(): Map<String, HTToolType> = instances

        @JvmStatic
        inline fun create(name: String, builderAction: Builder.() -> Unit): HTToolType = Builder(name).apply(builderAction).build()
    }

    fun createId(material: HTMaterialLike): ResourceLocation = material.asMaterialId().withPath { idPattern.replace("%s", it) }

    override fun compareTo(other: HTToolType): Int = this.name.compareTo(other.name)

    //    ToolFactory    //

    fun interface ToolFactory {
        fun createTool(material: HTToolMaterial, properties: Item.Properties): TieredItem
    }

    //    Builder    //

    class Builder(private val name: String) {
        var idPattern = "%s_$name"
        lateinit var factory: (HTToolMaterial, Item.Properties) -> TieredItem
        lateinit var langPattern: HTLangPatternProvider
        val toolTags: MutableList<TagKey<Item>> = mutableListOf()

        fun build(): HTToolType {
            val toolType = HTToolType(name, idPattern, factory, langPattern, toolTags)
            check(instances.put(name, toolType) == null) { "Duplicated tool type: $name" }
            return toolType
        }
    }
}
