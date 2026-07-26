package hiiragi283.core.api.item.tool

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.resource.modifyPath
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers

/**
 * 素材ツールの定義を担うクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTToolType(
    val name: String,
    private val idPattern: String,
    private val toolFactory: (HTToolMaterial, Item.Properties) -> Item,
    private val attributeFactory: (HTToolMaterial) -> ItemAttributeModifiers?,
    val langPattern: HTLangPatternProvider,
    val recipePattern: List<String>,
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

    /**
     * @since 21.1.0
     */
    fun createId(key: HTMaterialKey): ResourceLocation = key.getId().modifyPath { idPattern.replace("%s", it) }

    fun createTool(material: HTToolMaterial): Item {
        val properties = Item.Properties()
        val attribute: ItemAttributeModifiers? = attributeFactory(material)
        if (attribute != null) {
            properties.attributes(attribute)
        }
        return toolFactory(material, properties)
    }

    override fun compareTo(other: HTToolType): Int = this.name.compareTo(other.name)

    //    Builder    //

    /**
     * [HTToolType]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    class Builder(private val name: String) {
        var idPattern = "%s_$name"
        lateinit var factory: (HTToolMaterial, Item.Properties) -> Item
        var attribute: (HTToolMaterial) -> ItemAttributeModifiers? = { null }
        lateinit var langPattern: HTLangPatternProvider
        lateinit var recipePattern: List<String>
        val toolTags: MutableList<TagKey<Item>> = mutableListOf()

        fun build(): HTToolType {
            val toolType = HTToolType(name, idPattern, factory, attribute, langPattern, recipePattern, toolTags)
            check(instances.put(name, toolType) == null) { "Duplicated tool type: $name" }
            return toolType
        }
    }
}
