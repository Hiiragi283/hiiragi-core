package hiiragi283.lib.registry

import com.mojang.serialization.MapCodec
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer

class HTDeferredRecipeSerializerRegister(namespace: String) : HTDeferredRegister<RecipeSerializer<*>>(Registries.RECIPE_SERIALIZER, namespace) {
    fun <SERIALIZER : RecipeSerializer<*>> registerSerializer(name: String, serializer: SERIALIZER): SERIALIZER {
        this.register(name) { _ -> serializer }
        return serializer
    }

    fun <RECIPE : Recipe<*>> registerSerializer(
        name: String,
        codec: MapCodec<RECIPE>,
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, RECIPE> = ByteBufCodecs.fromCodecWithRegistries(codec.codec()),
    ): RecipeSerializer<RECIPE> = registerSerializer(name, RecipeSerializer(codec, streamCodec))
}
