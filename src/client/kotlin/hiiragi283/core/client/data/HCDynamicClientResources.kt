package hiiragi283.core.client.data

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.data.lang.HTLangTypes
import hiiragi283.core.api.data.model.HTModelTemplates
import hiiragi283.core.api.data.pack.HTDynamicResourceRegister
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import kotlin.system.measureTimeMillis
import net.minecraft.data.models.model.DelegatedModel
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.models.model.TexturedModel
import net.minecraft.resources.ResourceLocation

internal data object HCDynamicClientResources {
    @JvmStatic
    fun initialize() {
        HTDynamicResourceRegister.LOGGER.info("HiiragiCore Assets loading took {} ms", measureTimeMillis(::initializeInternal))
    }

    @JvmStatic
    private fun initializeInternal() {
        // Lang
        HTDynamicResourceRegister.addLang(HTLangTypes.EN_US, ::addTranslations)
        HTDynamicResourceRegister.addLang(HTLangTypes.JA_JP, ::addTranslations)
        // Texture
        // Model
        initializeModels()
    }

    //    Translation    //

    @JvmStatic
    private fun addTranslations(langType: HTLangType, consumer: (String, String) -> Unit) {
        val registered: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.registeredContents

        for (material: HTMaterial in HTMaterial.getManager()) {
            val key: HTMaterialKey = material.key
            // Material Name
            val materialName: HTLangName = material[HTMaterialPropertyKeys.LANG_NAME] ?: continue
            consumer(key.translationKey, materialName.getTranslatedName(langType))
            // Block
            for ((part: HTPart, block: HTMaterialContents.BlockEntry) in registered.blocks.column(key)) {
                val name: String = translate(langType, part, material) ?: continue
                consumer(block.translationKey, name)
            }
            // Fluid
            /*val fluids: HTMaterialContents<HTFluidPart, HTMaterialContents.FluidEntry> = HiiragiCoreAccess.INSTANCE.registeredFluids
            for ((part: HTFluidPart, fluid: HTMaterialContents.FluidEntry) in fluids.column(key)) {
                val name: String = translate(langType, part, material) ?: continue
                consumer(fluid.translationKey, name)
                consumer(Tags.getTagTranslationKey(part.createTagKey(key)), name)

                val bucketName: String = HTLangPatternProvider("%s Bucket", "%s入りバケツ").translate(langType, name)
                consumer(fluid.getBucketSupplier().translationKey, bucketName)
                consumer(Tags.getTagTranslationKey(part.createBucketTag(key)), bucketName)
            }*/
            // Item
            for ((part: HTPart, item: HTMaterialContents.ItemEntry) in registered.items.column(key)) {
                val name: String = translate(langType, part, material) ?: continue
                consumer(item.translationKey, name)
            }
            // Tool
            for ((toolType: HTToolType, tool: HTMaterialContents.ItemEntry) in registered.tools.column(key)) {
                consumer(tool.translationKey, toolType.langPattern.translate(langType, materialName))
            }
        }
    }

    @JvmStatic
    private fun translate(type: HTLangType, part: HTPart, material: HTMaterial): String? = translate(type, part, material, HTMaterialPropertyKeys.CUSTOM_LANG_NAME)

    @JvmStatic
    private fun <T : HTPartLike> translate(
        type: HTLangType,
        part: T,
        material: HTMaterial,
        key: HTPropertyKey<Map<T, HTLangName>>,
    ): String? = material.getOrDefault(key)[part]?.getTranslatedName(type) ?: run {
        val materialName: HTLangName = material[HTMaterialPropertyKeys.LANG_NAME] ?: return@run null
        part.getOrDefault(HTPartPropertyKeys.LANG_PATTERN).translate(type, materialName)
    }

    //    Model    //

    @JvmStatic
    private fun initializeModels() {
        val registered: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.registeredContents
        // Block
        registered.blocks.forEach { (part: HTPart, key: HTMaterialKey, block: HTMaterialContents.BlockEntry) ->
            if (HTPartPropertyKeys.IS_ORE in part) {
                val stoneTexture: ResourceLocation = part[HTPartPropertyKeys.ORE_STONE_TEX] ?: return@forEach
                HTDynamicResourceRegister.BLOCK_MODEL_GENERATOR.createTrivialBlock(
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
                HTDynamicResourceRegister.BLOCK_MODEL_GENERATOR.createTrivialCube(block.get())
            }
        }
        registered.blocks.values.forEach { block: HTMaterialContents.BlockEntry ->
            HTDynamicResourceRegister.MODEL_OUTPUT.accept(block.itemId, DelegatedModel(block.blockId))
        }
        // Fluid
        /*HiiragiCoreAccess.INSTANCE.registeredFluids.forEach { (part: HTFluidPart, _, fluid: HTMaterialContents.FluidEntry) ->
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
            HTDynamicResourceRegister.addToData(fluid.getBucketSupplier().itemId.withPrefix("models/"), root)
        }*/
        // Item
        registered.items.forEach { (part: HTPart, _, item: HTIdLike) ->
            HTDynamicResourceRegister.addModel(part.getOrDefault(HTPartPropertyKeys.ITEM_MODEL_PROVIDER), item)
        }
        registered.tools.forEach { (_, _, item: HTIdLike) ->
            HTDynamicResourceRegister.addItemModel(ModelTemplates.FLAT_HANDHELD_ITEM, item, TextureMapping.layer0(item.itemId))
        }
    }
}
