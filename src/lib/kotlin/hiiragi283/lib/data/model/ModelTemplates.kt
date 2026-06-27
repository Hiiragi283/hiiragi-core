package hiiragi283.lib.data.model

import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.itemId
import java.util.function.BiConsumer
import kotlin.jvm.optionals.getOrElse
import net.minecraft.client.data.models.model.ModelInstance
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.resources.Identifier

fun ModelTemplate.createBlock(like: HTIdLike, textures: TextureMapping, output: BiConsumer<Identifier, ModelInstance>): Identifier = this.create(like.blockId.withSuffix(this.suffix.getOrElse { "" }), textures, output)

fun ModelTemplate.createItem(like: HTIdLike, textures: TextureMapping, output: BiConsumer<Identifier, ModelInstance>): Identifier = this.create(like.itemId.withSuffix(this.suffix.getOrElse { "" }), textures, output)
