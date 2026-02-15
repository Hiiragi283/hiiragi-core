package hiiragi283.core.api.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block

interface HTBlockHolderLike<BLOCK : Block> :
    HTIdLike,
    HTHasTranslationKey,
    HTHasText {
    fun asBlock(): BLOCK

    fun getBlockHolder(): Holder<Block>

    companion object {
        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTBlockHolderLike<*>> = VanillaBiCodecs
            .holder(Registries.BLOCK)
            .xmap(Holder<Block>::value.andThen(::of), HTBlockHolderLike<*>::getBlockHolder)

        @JvmStatic
        fun of(holder: Holder<Block>): HTBlockHolderLike<Block> = object : Simple<Block> {
            override fun asBlock(): Block = getBlockHolder().value()

            override fun getBlockHolder(): Holder<Block> = holder
        }

        @JvmStatic
        fun <BLOCK : Block> of(block: BLOCK): HTBlockHolderLike<BLOCK> = object : Simple<BLOCK> {
            override fun asBlock(): BLOCK = block

            @Suppress("DEPRECATION")
            override fun getBlockHolder(): Holder<Block> = block.builtInRegistryHolder()
        }
    }

    interface Simple<BLOCK : Block> : HTBlockHolderLike<BLOCK> {
        override fun getId(): ResourceLocation = getBlockHolder().toLike().getId()

        override val translationKey: String get() = asBlock().descriptionId

        override fun getText(): Component = asBlock().name
    }
}
