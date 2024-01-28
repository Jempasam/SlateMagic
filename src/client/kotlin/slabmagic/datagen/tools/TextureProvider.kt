package slabmagic.datagen.tools

import com.google.common.hash.HashCode
import net.minecraft.block.Block
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import slabmagic.utils.ImageBuilder
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import javax.imageio.ImageIO

abstract class TextureProvider(val datagen: DataGenerator): DataProvider {
    override fun run(writer: DataWriter) {
        generateTextures(TextureGenerator(writer))
    }

    abstract fun generateTextures(texture: TextureGenerator)

    override fun getName() = "Texture"


    inner class TextureGenerator(val writer: DataWriter){

        private val generateds= mutableSetOf<Identifier>()
        private val images= mutableMapOf<Identifier,BufferedImage>()

        fun input(id: Identifier): Path{
            return datagen.output.parent.resolve("resources/assets").resolve(id.namespace).resolve("textures").resolve(id.path+".png")
        }

        fun output(id: Identifier): Path{
            return datagen.resolveRootDirectoryPath(DataGenerator.OutputType.RESOURCE_PACK).resolve(id.namespace).resolve("textures").resolve(id.path+".png")
        }

        fun texture(id: Identifier) = images.getOrPut(id){ ImageIO.read(input(id).toFile()) }

        fun item(item: Item) = Registry.ITEM.getId(item).let{ Identifier(it.namespace,"item/${it.path}") }

        fun block(block: Block) = Registry.BLOCK.getId(block).let{ Identifier(it.namespace,"block/${it.path}") }

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