package hiiragi283.lib.registry

import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.Text
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block

typealias HTSimpleDeferredBlock = HTDeferredBlock<Block>

class HTDeferredBlock<BLOCK : Block> :
    HTDeferredHolder<Block, BLOCK>,
    HTIdLike.Translatable {
    constructor(key: ResourceKey<Block>) : super(key)

    constructor(id: Identifier) : super(Registries.BLOCK.createKey(id))

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().name
}
