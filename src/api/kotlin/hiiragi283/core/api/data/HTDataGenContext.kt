package hiiragi283.core.api.data

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

/**
 * データ生成で使用されるインスタンスを束ねたクラスです。
 * @param output 生成されたデータの生成先を管理するインスタンス
 * @param registries レジストリを保持するインスタンス
 * @param fileHelper 指定したリソースが存在するかを判定するインスタンス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@JvmRecord
data class HTDataGenContext(
    val output: PackOutput,
    val registries: CompletableFuture<HolderLookup.Provider>,
    val fileHelper: ExistingFileHelper,
)
