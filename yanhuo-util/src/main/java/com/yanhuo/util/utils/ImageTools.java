package com.yanhuo.util.utils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 通过java获取图片的宽和高
 * @author sunlightcs
 * 2011-6-1
 * http://hi.juziku.com/sunlightcs/
 */
@Slf4j
public class ImageTools {

    /**
     * 获取图片宽度
     * @param file  图片文件
     * @return 宽度
     */
    public static int getImgWidth(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int ret = -1;
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getWidth(null); // 得到源图宽
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 获取图片高度
     * @param file  图片文件
     * @return 高度
     */
    public static int getImgHeight(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int ret = -1;
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getHeight(null); // 得到源图高
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static int getImgWidth(InputStream inputStream) {
        BufferedImage src = null;
        int ret = -1;
        try {
            src = javax.imageio.ImageIO.read(inputStream);
            ret = src.getWidth(null); // 得到源图宽
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static int getImgHeight(InputStream inputStream) {
        BufferedImage src = null;
        int ret = -1;
        try {
            src = javax.imageio.ImageIO.read(inputStream);
            ret = src.getHeight(null); // 得到源图高
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    public static BufferedImage resizeImageOne(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .outputFormat("JPEG")
                .outputQuality(1)
                .toOutputStream(outputStream);
        byte[] data = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return ImageIO.read(inputStream);
    }

    /**
     * BufferedImage图片流转byte[]数组
     */
    public static byte[] imageToBytes(BufferedImage bImage) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "jpg", out);
        } catch (IOException e) {
            log.error("错误信息: ", e);
        }
        return out.toByteArray();
    }


    /**
     * byte[]数组转BufferedImage图片流
     */
    private static BufferedImage bytesToBufferedImage(byte[] ImageByte) {
        ByteArrayInputStream in = new ByteArrayInputStream(ImageByte);
        BufferedImage image = null;
        try {
            image = ImageIO.read(in);
        } catch (IOException e) {
            log.error("错误信息: ", e);
        }
        return image;
    }
}