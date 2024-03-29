package slabmagic.utils

import java.text.ParseException

object ByteConversion {

    /**
     * @exception ParseException
     */
    fun parse(str: String): Array<Byte>{
        require(str.length%2==0)
        val ret=Array<Byte>(str.length/2){0}
        for(i in ret.indices){
            ret[i]= parsePair(str[i*2],str[i*2+1])
        }
        return ret
    }

    /**
     * @exception ParseException
     */
    fun parse(c: Char): Byte{
        return if(c in '0'..'9') (c-'0').toByte()
        else if(c in 'a'..'f') (c-'a'+10).toByte()
        else throw ParseException("Invalid hexadecimal character '$c'", 0)
    }

    /**
     * @exception ParseException
     */
    fun parsePair(c: Char, c2: Char): Byte{
        return (parse(c)*16+ parse(c2)).toByte()
    }

    fun serialize(array: Array<Byte>): String{
        val ret=StringBuilder()
        for(i in array.indices)ret.append(serializePair(array[i]))
        return ret.toString()
    }


    fun serialize(c: Byte): Char{
        require(c in 0..15)
        return if(c in 0..9) '0'+c.toInt()
        else 'a'+c.toInt()-10
    }

    fun serializePair(byte: Byte): String{
        return String(charArrayOf(serialize((byte/16).toByte()), serialize((byte%16).toByte())))
    }
}