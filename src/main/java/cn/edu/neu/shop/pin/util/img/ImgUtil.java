package cn.edu.neu.shop.pin.util.img;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;

public class ImgUtil {
    public static String getSuffix(byte[] source) {

        byte[] byteSuffix = Arrays.copyOf(source, 3);

        String hexSuffix = bytesToHexString(byteSuffix);

        switch (hexSuffix) {
            case "89504e":
                return ".png";
//            case "ffd8ff":
//                return ".jpg";
            default:
                return ".jpg";
        }

    }

    public static ResponseEntity<JSONObject> upload(String image, String url) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] rawBytes = decoder.decode(image.substring(22));

        String fileName = "" + System.currentTimeMillis() + ".png";
        File file = new File("D://" + fileName);

        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {
            fos = new java.io.FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bos = new BufferedOutputStream(fos);
        try {
            bos.write(rawBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileSystemResource fileResource = new FileSystemResource(new File("D://" + fileName));

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        requestHeaders.setContentType(MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<String, Object>();
        requestBody.add("smfile", fileResource);

        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, requestEntity, JSONObject.class);
        return responseEntity;
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
