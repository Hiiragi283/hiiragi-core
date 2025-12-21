package hiiragi283.core.api.data

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

/**
 * データ生成でよく使うインスタンスを束ねたデータクラス
 */
@JvmRecord
data class HTDataGenContext(
    val output: PackOutput,
    val registries: CompletableFuture<HolderLookup.Provider>,
    val fileHelper: ExistingFileHelper,
)
