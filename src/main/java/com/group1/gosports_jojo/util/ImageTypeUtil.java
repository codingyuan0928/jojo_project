package com.group1.gosports_jojo.util;

import java.util.Arrays;

public class ImageTypeUtil {
    public static String getImageMimeType(byte[] imageData) {
        System.out.println("我進來了");
        if (imageData == null || imageData.length < 4) {
            return null;
        }

        // JPEG 文件的魔數: FF D8 FF
        if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8 && imageData[2] == (byte) 0xFF) {
            return "image/jpeg";
        }

        // PNG 文件的魔數: 89 50 4E 47
        if (imageData[0] == (byte) 0x89 && imageData[1] == (byte) 0x50 && imageData[2] == (byte) 0x4E && imageData[3] == (byte) 0x47) {
            return "image/png";
        }

        // GIF 文件的魔數: 47 49 46 38
        if (imageData[0] == (byte) 0x47 && imageData[1] == (byte) 0x49 && imageData[2] == (byte) 0x46 && imageData[3] == (byte) 0x38) {
            return "image/gif";
        }

        // SVG 文件判斷: 以 <?xml 開頭，因為 SVG 是 XML 格式
        String fileHeader = new String(Arrays.copyOfRange(imageData, 0, 5));
        if (fileHeader.startsWith("<?xml")) {
            return "image/svg+xml";
        }

        // 未知類型
        return null;
    }
}
