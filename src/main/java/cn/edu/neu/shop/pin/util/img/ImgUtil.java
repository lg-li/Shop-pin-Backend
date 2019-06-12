package cn.edu.neu.shop.pin.util.img;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.Arrays;

public class ImgUtil {
    public static String getSuffix(byte[] source) {

        byte[] byteSuffix = Arrays.copyOf(source, 3);

        String hexSuffix = bytesToHexString(byteSuffix);

        switch (hexSuffix) {
            case "89504e":
                return ".png";
            case "ffd8ff":
                return ".jpg";
            default:
                return ".jpg";
        }

    }

    public static JSONObject upload(String image, String url) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] rawBytes = decoder.decodeBuffer(image);
            String suffix = getSuffix(rawBytes);//获取图片的后缀名，也可以是其他任意文件名
            String fileName = "myImage" + suffix;
            ByteArrayResource fileResource = new ByteArrayResource(rawBytes);
            MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<String, Object>();
            postParameters.add("smfile", fileResource);
            RestTemplate restTemplate = new RestTemplate();
            JSONObject response = restTemplate.postForObject(url, postParameters, JSONObject.class);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toLowerCase();
    }

    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
