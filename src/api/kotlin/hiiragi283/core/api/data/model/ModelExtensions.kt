package hiiragi283.core.api.data.model

import com.google.gson.JsonElement
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import java.util.function.BiConsumer
import java.util.function.Supplier
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.client.model.generators.ModelProvider

typealias ModelOutput = BiConsumer<ResourceLocation, Supplier<JsonElement>>

//    ModelProvider    //

/**
 * 指定した[ID][id]でモデルのビルダーを作成します。
 */
fun <BUILDER : ModelBuilder<BUILDER>, PROVIDER : ModelProvider<BUILDER>> PROVIDER.getBuilder(id: ResourceLocation): BUILDER = this.getBuilder(id.toString())

/**
 * 指定した[like]からモデルのビルダーを作成します。
 */
fun <BUILDER : ModelBuilder<BUILDER>, PROVIDER : ModelProvider<BUILDER>> PROVIDER.getBuilder(like: HTIdLike): BUILDER = this.getBuilder(like.getId())

fun <BUILDER : ModelBuilder<BUILDER>, PROVIDER : ModelProvider<BUILDER>> PROVIDER.withExistingParent(
    like: HTIdLike,
    parent: ResourceLocation,
): BUILDER = this.withExistingParent(like.path, parent)

fun ModelProvider<*>.existsTexture(id: ResourceLocation): Boolean = this.existingFileHelper.exists(id, ModelProvider.TEXTURE)

fun ModelProvider<*>.trackTexture(id: ResourceLocation) {
    this.existingFileHelper.trackGenerated(id, ModelProvider.TEXTURE)
}

//    ModelBuilder    //

fun <BUILDER : ModelBuilder<BUILDER>> BUILDER.texture(key: String, like: HTIdLike): BUILDER = this.texture(key, like.getId())

fun <BUILDER : ModelBuilder<BUILDER>> BUILDER.blockTexture(key: String, like: HTIdLike): BUILDER = this.texture(key, like.blockId)

fun <BUILDER : ModelBuilder<BUILDER>> BUILDER.itemTexture(key: String, like: HTIdLike): BUILDER = this.texture(key, like.itemId)

fun <BUILDER : ModelBuilder<BUILDER>> BUILDER.fixedTexture(key: String, like: HTIdLike): BUILDER = this.texture(key, like.getId().withSuffix("_$key"))

fun <BUILDER : ModelBuilder<BUILDER>> BUILDER.fixedBlockTexture(key: String, like: HTIdLike): BUILDER = this.texture(key, like.blockId.withSuffix("_$key"))

fun <BUILDER : ModelBuilder<BUILDER>> BUILDER.fixedItemTexture(key: String, like: HTIdLike): BUILDER = this.texture(key, like.itemId.withSuffix("_$key"))
