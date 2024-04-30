package slabmagic.datagen.tools

import com.google.common.hash.HashCode
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.block.Block
import net.minecraft.data.DataOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.Identifier
import slabmagic.utils.ImageBuilder
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import javax.imageio.ImageIO

abstract class TextureProvider(val output: FabricDataOutput, val registries: CompletableFuture<RegistryWrapper.WrapperLookup>): DataProvider {
    override fun run(writer: DataWriter): CompletableFuture<*> {
        return registries.thenApply {
            generateTextures(TextureGenerator(writer,it),it)
        }
    }

    abstract fun generateTextures(texture: TextureGenerator, registries: RegistryWrapper.WrapperLookup)

    override fun getName() = "Texture"


    inner class TextureGenerator(val writer: DataWriter, val registries: RegistryWrapper.WrapperLookup){

        private val generateds= mutableSetOf<Identifier>()
        private val images= mutableMapOf<Identifier,BufferedImage>()

        fun input(id: Identifier): Path{
            return output.path.parent.resolve("resources/assets").resolve(id.namespace).resolve("textures").resolve(id.path+".png")
        }

        fun output(id: Identifier): Path{
            return output.resolvePath(DataOutput.OutputType.RESOURCE_PACK).resolve(id.namespace).resolve("textures").resolve(id.path+".png")
        }

        fun texture(id: Identifier) = images.getOrPut(id){ ImageIO.read(input(id).toFile()) }

        fun item(item: Item) = Registries.ITEM.getId(item).let{ Identifier(it.namespace,"item/${it.path}") }

        fun block(block: Block) = Registries.BLOCK.getId(block).let{ Identifier(it.namespace,"block/${it.path}") }

        fun builder() = ImageBuilder()

        fun save(id: Identifier, builder: ImageBuilder){
            if(!generateds.contains(id)){
                generateds.add(id)
                val baos = ByteArrayOutputStream()
                ImageIO.write(builder.result(), "png", baos)
                val bytes: ByteArray = baos.toByteArray()
                writer.write(output(id), bytes, HashCode.fromBytes(bytes))
            }
        }
    }


}