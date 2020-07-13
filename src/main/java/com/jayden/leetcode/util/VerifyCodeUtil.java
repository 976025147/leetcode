package com.jayden.leetcode.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 验证码生成
 * Jayden 2020年07月02日
 */
public class VerifyCodeUtil {


    private static final Logger logger = LoggerFactory.getLogger(VerifyCodeUtil.class);

    private static final String[] FONT_TYPES = {"\u5b8b\u4f53", "\u65b0\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66"};

    private VerifyCodeUtil() {

    }

    /**
     * 设置背景颜色及大小，干扰线
     *
     * @param graphics
     * @param width
     * @param height
     */
    private static void fillBackground(Graphics graphics, int width, int height) {
        // 填充背景
        graphics.setColor(Color.WHITE);
        //设置矩形坐标x y 为0
        graphics.fillRect(0, 0, width, height);

        // 加入干扰线条
        for (int i = 0; i < 8; i++) {
            //设置随机颜色算法参数
            graphics.setColor(randomColor(40, 150));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            graphics.drawLine(x, y, x1, y1);
        }
    }

    /**
     * 生成随机字符
     *
     * @param width
     * @param height
     * @param os
     * @return
     * @throws IOException
     */
    private static String generate(int width, int height, int verifyCodeLength, int fontSize, OutputStream os) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        fillBackground(graphics, width, height);
        String randomStr = randomString(verifyCodeLength);
        createCharacter(graphics, randomStr, width, height, fontSize);
        graphics.dispose();
        //设置JPEG格式
        ImageIO.write(image, "JPEG", os);
        return randomStr;
    }

    /**
     * 生成默认大小的验证码
     *
     * @return
     */
    public static VerifyCode generate() {
        return generate(80, 28, 4, 26);
    }

    /**
     * 验证码生成
     *
     * @param width
     * @param height
     * @return
     */
    public static VerifyCode generate(int width, int height, int verifyCodeLength, int fontSize) {
        VerifyCode verifyCode = null;
        try (
                //将流的初始化放到这里就不需要手动关闭流
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            String code = generate(width, height, verifyCodeLength, fontSize, baos);
            verifyCode = new VerifyCode();
            verifyCode.setCode(code);
            verifyCode.setImgBytes(baos.toByteArray());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            verifyCode = null;
        }
        return verifyCode;
    }

    /**
     * 设置字符颜色大小
     *
     * @param g
     * @param randomStr
     */
    private static void createCharacter(Graphics g, String randomStr, int width, int height, int fontSize) {
        char[] charArray = randomStr.toCharArray();
        int widthAvg = (int) ((width * 0.8) / charArray.length);

        for (int i = 0; i < charArray.length; i++) {
            //设置RGB颜色算法参数
            g.setColor(new Color(50 + nextInt(100),
                    50 + nextInt(100), 50 + nextInt(100)));
            //设置字体大小，类型
            g.setFont(new Font(FONT_TYPES[nextInt(FONT_TYPES.length)], Font.BOLD, fontSize));
            //设置x y 坐标
            if (i % 2 == 0) {
                g.drawString(String.valueOf(charArray[i]), (widthAvg * i + (int) (width * 0.1)) + nextInt(widthAvg / 2), ((height / 2) - (fontSize / 2)) + (nextInt(height / 20)));
            } else {
                g.drawString(String.valueOf(charArray[i]), (widthAvg * i + (int) (width * 0.1)) + nextInt(widthAvg / 2), ((height / 2) - (fontSize / 2)) - (nextInt(height / 20)));
            }

        }
    }


    public static class VerifyCode {
        private String code;

        private byte[] imgBytes;

        private long expireTime;//过期时间 暂时没用

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public byte[] getImgBytes() {
            return imgBytes;
        }

        public void setImgBytes(byte[] imgBytes) {
            this.imgBytes = imgBytes;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }
    }


    private static final char[] CODE_SEQ = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};


    private static Random random = new Random();

    private static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CODE_SEQ[random.nextInt(CODE_SEQ.length)]);
        }
        return sb.toString();
    }


    private static Color randomColor(int fc, int bc) {
        int f = fc;
        int b = bc;
        if (f > 255) {
            f = 255;
        }
        if (b > 255) {
            b = 255;
        }
        return new Color(f + random.nextInt(b - f), f + random.nextInt(b - f), f + random.nextInt(b - f));
    }

    private static int nextInt(int bound) {
        return random.nextInt(bound);
    }

}
