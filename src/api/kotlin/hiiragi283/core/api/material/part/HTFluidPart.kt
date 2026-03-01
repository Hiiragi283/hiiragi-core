package hiiragi283.core.api.material.part

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.property.addNamePattern
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

enum class HTFluidPart(private val idPattern: String, private val tagPattern: String, properties: HTPropertyMap) :
    HTPartLike,
    HTPropertyMap by properties {
    MOLTEN("molten_%s", "molten_%s", HTBasicPropertyMap.Mutable().apply { addNamePattern("Molten %s", "溶融%s") }),
    ;

    @Deprecated("Not Implemented", level = DeprecationLevel.ERROR)
    override fun asPart(): HTPart = throw UnsupportedOperationException()

    override fun asPartName(): String = name.lowercase()

    override fun createId(material: HTMaterialLike): ResourceLocation = material.asMaterialId().withPath { idPattern.replace("%s", it) }

    /**
     * 指定した[素材][material]から，[液体][Fluid]の共通タグを生成します。
     */
    fun createTagKey(material: HTMaterialLike): TagKey<Fluid> {
        val id: ResourceLocation = HTConst.COMMON.toId(tagPattern.replace("%s", material.asMaterialId().path))
        return Registries.FLUID.createTagKey(id)
    }

    /**
     * 指定した[素材][material]から，[液体バケツ][Item]の共通タグを生成します。
     */
    fun createBucketTag(material: HTMaterialLike): TagKey<Item> {
        val id: ResourceLocation = HTConst.COMMON.toId(tagPattern.replace("%s", material.asMaterialId().path))
        return Registries.ITEM.createTagKey(id.withSuffix("_bucket"))
    }
}
