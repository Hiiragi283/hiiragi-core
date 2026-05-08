package hiiragi283.core.api.material.part

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.property.addNamePattern
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.buildPropertyMap
import hiiragi283.core.api.tag.RawTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

/**
 * [液体][Fluid]向けに[HTPartLike]を実装したクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
enum class HTFluidPart(private val idPattern: String, private val tagPattern: String, getter: HTPropertyGetter) :
    HTPartLike,
    HTPropertyGetter by getter {
    MOLTEN("molten_%s", "molten_%s", buildPropertyMap { addNamePattern("Molten %s", "溶融%s") }),
    ;

    @Deprecated("Not Implemented", level = DeprecationLevel.ERROR)
    override fun asPart(): HTPart = throw UnsupportedOperationException()

    override fun asPartName(): String = name.lowercase()

    override fun createId(material: HTMaterialLike): ResourceLocation = material.asMaterialId().withPath { idPattern.replace("%s", it) }

    /**
     * 指定した[素材][material]から，[液体][Fluid]の共通タグを生成します。
     */
    fun createTagKey(material: HTMaterialLike): TagKey<Fluid> = RawTagKey.common(tagPattern.replace("%s", material.asMaterialId().path)).create(Registries.FLUID)

    /**
     * 指定した[素材][material]から，[液体バケツ][Item]の共通タグを生成します。
     */
    fun createBucketTag(material: HTMaterialLike): TagKey<Item> = RawTagKey
        .common(tagPattern.replace("%s", material.asMaterialId().path) + "_bucket")
        .create(Registries.ITEM)
}
