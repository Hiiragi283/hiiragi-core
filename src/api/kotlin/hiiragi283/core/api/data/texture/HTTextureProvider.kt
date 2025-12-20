package hiiragi283.core.api.data.texture

import com.google.common.hash.HashCode
import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.data.HTDataGenContext
import net.minecraft.Util
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.io.IOException
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.io.path.inputStream

abstract class HTTextureProvider(private val packOutput: PackOutput, private val fileHelper: ExistingFileHelper) : DataProvider {
    constructor(context: HTDataGenContext) : this(context.output, context.fileHelper)

    private val pathProvider: PackOutput.PathProvider =
        packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "textures")

    override fun run(output: CachedOutput): CompletableFuture<*> {
        val set: MutableSet<ResourceLocation> = mutableSetOf()
        val list: MutableList<CompletableFuture<*>> = mutableListOf()
        gather { id: ResourceLocation, image: NativeImage ->
            check(set.add(id)) { "Duplicate texture $id" }
            list += writeImage(output, image, pathProvider.file(id, "png"))
            fileHelper.trackGenerated(id, ModelProvider.TEXTURE)
        }
        return CompletableFuture.allOf(*list.toTypedArray())
    }

    protected abstract fun gather(output: HTTextureOutput)

    private fun writeImage(output: CachedOutput, image: NativeImage, path: Path): CompletableFuture<*> = CompletableFuture.runAsync(
        {
            try {
                val byteArray: ByteArray = image.asByteArray()
                output.writeIfNeeded(path, byteArray, HashCode.fromBytes(byteArray))
            } catch (exception: IOException) {
                DataProvider.LOGGER.error("Failed to save image to {}", path, exception)
            }
        },
        Util.backgroundExecutor(),
    )

    override fun getName(): String = "Texture"

    //    Extensions    //

    protected fun getTexture(id: ResourceLocation): NativeImage? {
        val filePath: Path = packOutput.outputFolder.resolve("../../main/resources/assets/${id.namespace}/textures/${id.path}.png")
        try {
            return NativeImage.read(filePath.inputStream())
        } catch (_: IOException) {
            DataProvider.LOGGER.error("Failed to load image to {}", filePath)
        }
        return null
    }

    protected fun copyFrom(other: NativeImage): NativeImage {
        val image = NativeImage(other.width, other.height, true)
        image.copyFrom(other)
        return image
    }
}
