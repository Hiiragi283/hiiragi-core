package hiiragi283.core.client.data

import com.google.gson.JsonObject
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.model.HTModelOutput
import hiiragi283.core.api.data.model.HTModelProvider
import hiiragi283.core.api.data.model.HTModelTemplates
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.models.model.TexturedModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager

data object HCModelProvider : HTModelProvider() {
    override fun registerModels(blockModels: BlockModelGenerators, manager: ResourceManager, output: HTModelOutput) {
        val registered: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.registeredContents
        // Block
        registered.blocks.forEach { (part: HTPart, key: HTMaterialKey, block: HTMaterialContents.BlockEntry) ->
            if (HTPartPropertyKeys.IS_ORE in part) {
                val stoneTexture: ResourceLocation = part[HTPartPropertyKeys.ORE_STONE_TEX] ?: return@forEach
                blockModels.createTrivialBlock(
                    block.get(),
                    TexturedModel.createDefault(
                        { _ ->
                            TextureMapping()
                                .put(TextureSlot.LAYER0, stoneTexture)
                                .put(TextureSlot.LAYER1, CommonParts.ORE.createId(key).withPrefix("block/"))
                        },
                        HTModelTemplates.LAYERED,
                    ),
                )
            } else {
                blockModels.createTrivialCube(block.get())
            }
        }
        // Fluid
        HiiragiCoreAccess.INSTANCE.registeredFluids.forEach { (part: HTFluidPart, _, fluid: HTMaterialContents.FluidEntry) ->
            val parent: ResourceLocation = when {
                part == HTFluidPart.MOLTEN -> "bucket_drip"
                else -> "bucket"
            }.let { HTConst.NEOFORGE.toId(HTConst.ITEM, it) }
            val root = JsonObject()
            root.addProperty("parent", parent.toString())
            root.addProperty("fluid", fluid.getId().toString())
            root.addProperty("loader", "neoforge:fluid_container")
            if (fluid.get().fluidType.isLighterThanAir) {
                root.addProperty("flip_gas", "true")
            }
            output.accept(fluid.getBucketSupplier().getId()) { root }
        }
        // Item
        registered.items.forEach { (part: HTPart, _, item: HTIdLike) ->
            val itemId: ResourceLocation = item.itemId
            val textureIcon: String = part[HTPartPropertyKeys.TEXTURE_ICON] ?: part.name
            val overlay: ResourceLocation = HiiragiCoreAPI.id(HTConst.ITEM, "${textureIcon}_overlay")
            if (manager.getResource(overlay.withPath { "textures/$it.png" }).isPresent) {
                ModelTemplates.TWO_LAYERED_ITEM.create(itemId, TextureMapping.layered(itemId, overlay), output)
            } else {
                ModelTemplates.FLAT_ITEM.create(itemId, TextureMapping.layer0(itemId), output)
            }
        }
        registered.tools.forEach { (_, _, item: HTIdLike) ->
            val itemId: ResourceLocation = item.itemId
            ModelTemplates.FLAT_HANDHELD_ITEM.create(itemId, TextureMapping.layer0(itemId), output)
        }
    }
}
