package hiiragi283.core.api.data.model

import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier

fun buildTextureMap(builderAction: TextureMapping.() -> Unit): TextureMapping = TextureMapping().apply(builderAction)

fun TextureMapping.put(slot: TextureSlot, id: Identifier): TextureMapping = this.put(slot, Material(id))
