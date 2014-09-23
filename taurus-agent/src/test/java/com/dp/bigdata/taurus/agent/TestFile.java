package com.dp.bigdata.taurus.agent;

/**
 * Created by kirinli on 14-9-22.
 */
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TestFile {
    public static String fff = "/Users/kirinli/Downloads/e";

    public static void main(String[] args) throws Exception {

        final int BUFFER_SIZE = 0x100000;// 缓冲区大小为3M

        File f = new File(fff);

        /**
         *
         * map(FileChannel.MapMode mode,long position, long size)
         *
         * mode - 根据是按只读、读取/写入或专用（写入时拷贝）来映射文件，分别为 FileChannel.MapMode 类中所定义的
         * READ_ONLY、READ_WRITE 或 PRIVATE 之一
         *
         * position - 文件中的位置，映射区域从此位置开始；必须为非负数
         *
         * size - 要映射的区域大小；必须为非负数且不大于 Integer.MAX_VALUE
         *
         * 所以若想读取文件后半部分内容，如例子所写；若想读取文本后1/8内容，需要这样写map(FileChannel.MapMode.READ_ONLY,
         * f.length()*7/8,f.length()/8)
         *
         * 想读取文件所有内容，需要这样写map(FileChannel.MapMode.READ_ONLY, 0,f.length())
         *
         */
        long len =  1024*1024;
        long fileLen = f.length();
        long startPos =  0;
          if (fileLen > len){
              startPos =  f.length() - len;
              fileLen = len;
          }


        MappedByteBuffer inputBuffer = new RandomAccessFile(f, "r")
                .getChannel().map(FileChannel.MapMode.READ_ONLY,
                        startPos, fileLen);

        byte[] dst = new byte[BUFFER_SIZE];// 每次读出3M的内容

        long start = System.currentTimeMillis();
        String strRet="";
        for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {

            if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {

                for (int i = 0; i < BUFFER_SIZE; i++)

                    dst[i] = inputBuffer.get(offset + i);

            } else {

                for (int i = 0; i < inputBuffer.capacity() - offset; i++)

                    dst[i] = inputBuffer.get(offset + i);

            }

            int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE
                    : inputBuffer.capacity() % BUFFER_SIZE;

            strRet += new String(dst, 0, length);
            //System.out.println();// new
            // String(dst,0,length)这样可以取出缓存保存的字符串，可以对其进行操作

        }
        System.out.println(strRet);
        long end = System.currentTimeMillis();

        System.out.println("读取文件文件一半内容花费：" + (end - start) + "毫秒");

    }




}
