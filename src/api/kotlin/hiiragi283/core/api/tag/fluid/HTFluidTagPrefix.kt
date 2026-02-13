package hiiragi283.core.api.tag.fluid

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

class HTFluidTagPrefix(
    val name: String,
    private val idPattern: String,
    private val tagPattern: String,
    langPattern: HTLangPatternProvider,
) : Comparable<HTFluidTagPrefix>,
    HTLangPatternProvider by langPattern {
    /**
     * 指定した[素材][material]から素材液体などの[ID][ResourceLocation]を生成します。
     */
    fun createId(material: HTMaterialLike): ResourceLocation = material.asMaterialId().withPath { idPattern.replace("%s", it) }

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

    override fun compareTo(other: HTFluidTagPrefix): Int = this.name.compareTo(other.name)
}
