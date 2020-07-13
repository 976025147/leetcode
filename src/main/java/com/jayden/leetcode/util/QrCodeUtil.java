package com.jayden.leetcode.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;

public class QrCodeUtil {

    private static Logger logger = LoggerFactory.getLogger(QrCodeUtil.class);

    private QrCodeUtil() {
    }

    /**
     * 生成二维码图片对象
     *
     * @param content 内容
     * @param width   宽
     * @param height  高
     * @param margin  二维码与边框的外边距
     * @param color   颜色
     * @return
     */
    public static BufferedImage createQRCode(String content, int width, int height, int margin, Color color) {
        try {
            /**二维码参数设定*/
            EnumMap<EncodeHintType, Object> hintTypes = new EnumMap<>(EncodeHintType.class);
            hintTypes.put(EncodeHintType.CHARACTER_SET, CharacterSetECI.UTF8);
            hintTypes.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//容错级别，共4级，详细问度娘
            hintTypes.put(EncodeHintType.MARGIN, margin);//二维码外边距
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hintTypes);//创建二维码，本质为二位数组
            /**将二维码转为图片*/
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bufferedImage.setRGB(i, j, bitMatrix.get(i, j) ? color.getRGB() : Color.WHITE.getRGB());
                }
            }
            return bufferedImage;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 生成二维码保存到文件
     *
     * @param content  内容
     * @param width    宽
     * @param height   高
     * @param margin   二维码与边框的外边距
     * @param filePath 文件路径
     * @param color    颜色
     * @return
     */
    public static String qrCodeToFile(String content, int width, int height, int margin, String filePath, Color color) {
        BufferedImage bufferedImage = createQRCode(content, width, height, margin, color);
        File file = new File(filePath + File.separator + System.currentTimeMillis() + ".png");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            ImageIO.write(bufferedImage, "png", new FileOutputStream(file));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return file.getAbsolutePath();
    }

    /**
     * 生成二维码保存到文件（默认黑色）
     *
     * @param content  内容
     * @param width    宽
     * @param height   高
     * @param margin   二维码与边框的外边距
     * @param filePath 文件路径
     * @return
     */
    public static String qrCodeToFile(String content, int width, int height, int margin, String filePath) {
        return qrCodeToFile(content, width, height, margin, filePath, Color.BLACK);
    }

    /**
     * 生成二维码字节数组
     *
     * @param content 内容
     * @param width   宽
     * @param height  高
     * @param margin  二维码与边框的外边距
     * @param color   颜色
     * @return
     */
    public static byte[] getQrCodeBytes(String content, int width, int height, int margin, Color color) {
        BufferedImage bufferedImage = createQRCode(content, width, height, margin, color);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return baos.toByteArray();
    }

    /**
     * 生成二维码字节数组（默认黑色）
     *
     * @param content 内容
     * @param width   宽
     * @param height  高
     * @param margin  二维码与边框的外边距
     * @return
     */
    public static byte[] getQrCodeBytes(String content, int width, int height, int margin) {
        return getQrCodeBytes(content, width, height, margin, Color.BLACK);
    }
}
