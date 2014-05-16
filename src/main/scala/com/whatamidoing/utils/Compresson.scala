package com.whatamidoing.utils

import java.io._
import java.util.zip._

import org.apache.commons.io.IOUtils

object Compressor{

    def compress(content: Array[Byte] ): Array[Byte] = {
         val byteArrayOutputStream:  ByteArrayOutputStream = new ByteArrayOutputStream()
        try{
            val gzipOutputStream: GZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream)
            gzipOutputStream.write(content);
            gzipOutputStream.close();
        } catch {
	   case io: IOException =>  throw new RuntimeException(io)
        }
     //   System.out.println("Compression ratio %f\n", (1.0f * content.length/byteArrayOutputStream.size()))
        return byteArrayOutputStream.toByteArray()
    }

    def decompress(contentBytes: Array[Byte]): String = {
        val out: ByteArrayOutputStream  = new ByteArrayOutputStream()
        try{
            IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(contentBytes)), out)
        } catch {
	   case io: IOException =>  throw new RuntimeException(io)
        }
        return new String(out.toByteArray(),"UTF-8")

    }

}
