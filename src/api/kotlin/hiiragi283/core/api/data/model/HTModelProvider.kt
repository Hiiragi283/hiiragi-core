package hiiragi283.core.api.data.model

import com.google.gson.JsonElement
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import java.util.function.BiConsumer
import java.util.function.Supplier
import net.mehvahdjukaar.moonlight.api.resources.ResType
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.model.DelegatedModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.data.models.ModelProvider

typealias HTModelOutput = BiConsumer<ResourceLocation, Supplier<JsonElement>>

/**
 * BlockState JSONおよびモデルJSONを生成する[ResourceGenTask]の抽象クラスです。
 *
 * 参照 : [Minecraft - ModelProvider][ModelProvider]
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
abstract class HTModelProvider : ResourceGenTask {
    final override fun accept(manager: ResourceManager, sink: ResourceSink) {
        val blockStates: MutableMap<Block, BlockStateGenerator> = hashMapOf()
        val models: MutableMap<ResourceLocation, Supplier<JsonElement>> = hashMapOf()
        val itemsSkipped: MutableSet<Item> = hashSetOf()
        val output: HTModelOutput = HTModelOutput { modelId: ResourceLocation, supplier: Supplier<JsonElement> ->
            check(models.put(modelId, supplier) == null) { "Duplicate model definition for $modelId" }
        }
        // モデルを登録
        registerModels(
            BlockModelGenerators(
                { generator: BlockStateGenerator ->
                    val block: Block = generator.block
                    check(blockStates.put(block, generator) == null) { "Duplicate blockstate definition for $block" }
                },
                output,
                itemsSkipped::add,
            ),
            manager,
            output,
        )
        // 不足している BlockItem のモデルを登録
        for (block: Block in blockStates.keys) {
            val blockItem: Item = Item.BY_BLOCK[block] ?: continue
            if (blockItem in itemsSkipped) continue
            val blockItemModelId: ResourceLocation = blockItem.toLike().itemId
            if (blockItemModelId !in models) {
                models[blockItemModelId] = DelegatedModel(block.toLike().blockId)
            }
        }

        // BlockState JSONを登録
        for ((block: Block, generator: BlockStateGenerator) in blockStates) {
            val blockId: ResourceLocation = block.toLike().getId()
            sink.addBlockState(blockId, generator.get())
        }
        // モデルJSONを登録
        for ((modelId: ResourceLocation, supplier: Supplier<JsonElement>) in models) {
            sink.addJson(modelId, supplier.get(), ResType.MODELS)
        }
    }

    protected abstract fun registerModels(blockModels: BlockModelGenerators, manager: ResourceManager, output: HTModelOutput)
}
