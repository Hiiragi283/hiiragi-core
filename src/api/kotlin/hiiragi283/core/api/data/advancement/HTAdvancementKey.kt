package hiiragi283.core.api.data.advancement

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.toDescriptionKey
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.resources.ResourceLocation

/**
 * 進捗の[ID][ResourceLocation]を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@JvmInline
value class HTAdvancementKey private constructor(private val id: ResourceLocation) :
    HTIdLike,
    Comparable<HTAdvancementKey> {
    companion object {
        @JvmStatic
        fun of(holder: AdvancementHolder): HTAdvancementKey = of(holder.id())

        @JvmStatic
        fun of(id: ResourceLocation): HTAdvancementKey = HTAdvancementKey(id)
    }

    val titleKey: String get() = getId().toDescriptionKey("advancements", "title")
    val descKey: String get() = getId().toDescriptionKey("advancements", "desc")

    override fun getId(): ResourceLocation = this.id

    override fun compareTo(other: HTAdvancementKey): Int = this.id.compareNamespaced(other.id)
}
