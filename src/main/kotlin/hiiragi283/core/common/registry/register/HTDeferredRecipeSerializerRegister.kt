package hiiragi283.core.common.registry.register

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.serialization.codec.MapBiCodec
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer

class HTDeferredRecipeSerializerRegister(namespace: String) :
    HTDeferredRegister<RecipeSerializer<*>>(Registries.RECIPE_SERIALIZER, namespace) {
    fun <RECIPE : Recipe<*>> registerSerializer(
        name: String,
        codec: MapBiCodec<RegistryFriendlyByteBuf, RECIPE>,
    ): RecipeSerializer<RECIPE> = registerSerializer(name, codec.codec, codec.streamCodec)

    fun <RECIPE : Recipe<*>> registerSerializer(
        name: String,
        codec: MapCodec<RECIPE>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, RECIPE>,
    ): RecipeSerializer<RECIPE> {
        val serializer: RecipeSerializer<RECIPE> = object : RecipeSerializer<RECIPE> {
            override fun codec(): MapCodec<RECIPE> = codec

            override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, RECIPE> = streamCodec
        }
        register(name) { _ -> serializer }
        return serializer
    }
}
