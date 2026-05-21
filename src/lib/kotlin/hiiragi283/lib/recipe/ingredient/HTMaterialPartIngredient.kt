package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialItemEntry
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.listOrElement
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.listOf
import hiiragi283.lib.util.Ior
import java.util.stream.Stream
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType

@JvmRecord
data class HTMaterialPartIngredient(val contents: HolderSet<HTMaterialContents>, val parts: List<HTMaterialPartKey>) : ICustomIngredient {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMaterialPartIngredient> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                HTCodecs.holderSet(HTRegistries.Keys.MATERIAL_CONTENTS).fieldOf("contents").forGetter(HTMaterialPartIngredient::contents),
                HTMaterialPartKey.CODEC.listOrElement().fieldOf("parts").forGetter(HTMaterialPartIngredient::parts),
            ).apply(instance, ::HTMaterialPartIngredient)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMaterialPartIngredient> = StreamCodec.composite(
            HTStreamCodecs.holderSet(HTRegistries.Keys.MATERIAL_CONTENTS),
            HTMaterialPartIngredient::contents,
            HTMaterialPartKey.STREAM_CODEC.listOf(),
            HTMaterialPartIngredient::parts,
            ::HTMaterialPartIngredient,
        )

        @JvmField
        val TYPE: IngredientType<HTMaterialPartIngredient> = IngredientType(CODEC, STREAM_CODEC)
    }

    override fun test(stack: ItemStack): Boolean {
        for (holder: Holder<HTMaterialContents> in contents) {
            val contents: HTMaterialContents = holder.value()
            for (part: HTMaterialPartKey in parts) {
                val (entry: HTMaterialItemEntry?, tagKey: TagKey<Item>?) = contents.getRawEntry(part)?.toPair() ?: continue
                if (entry != null && stack.`is`(entry.get().asItem())) {
                    return true
                }
                if (tagKey != null && stack.`is`(tagKey)) {
                    return true
                }
            }
        }
        return false
    }

    @Suppress("DEPRECATION")
    override fun items(): Stream<Holder<Item>> = contents
        .map(Holder<HTMaterialContents>::value)
        .flatMap { contents: HTMaterialContents -> parts.mapNotNull(contents::getRawEntry) }
        .flatMap { ior: Ior<HTMaterialItemEntry, TagKey<Item>> ->
            buildList {
                val (entry: HTMaterialItemEntry?, tagKey: TagKey<Item>?) = ior.toPair()
                if (entry != null) {
                    add(entry.get().asItem().builtInRegistryHolder())
                }
                if (tagKey != null) {
                    addAll(BuiltInRegistries.ITEM.getTagOrEmpty(tagKey))
                }
            }
        }.stream()

    override fun isSimple(): Boolean = true

    override fun getType(): IngredientType<*> = TYPE
}
