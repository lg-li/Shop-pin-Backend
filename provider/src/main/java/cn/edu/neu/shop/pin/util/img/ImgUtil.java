package cn.edu.neu.shop.pin.util.img;

import cn.edu.neu.shop.pin.file_fastdfs.FastDFSFile;
import cn.edu.neu.shop.pin.file_fastdfs.FileManager;
import cn.edu.neu.shop.pin.schedule.GroupClosingScheduler;
import com.alibaba.fastjson.JSONObject;
import org.csource.common.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(ImgUtil.class);
    public static String getSuffix(byte[] source) {

        byte[] byteSuffix = Arrays.copyOf(source, 3);

        String hexSuffix = bytesToHexString(byteSuffix);

        assert hexSuffix != null;
        if ("89504e".equals(hexSuffix)) {
            return ".png";
//            case "ffd8ff":
//                return ".jpg";
        }
        return ".jpg";

    }

    public static String upload(String image) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] rawBytes = decoder.decode(image.substring(22));
        String fileName = "img-" + System.currentTimeMillis() + ".png";
//        File file = new File("D://" + fileName);
        String filePath = "Upload error";
//
//        BufferedOutputStream bos = null;
//        java.io.FileOutputStream fos = null;
//        try {
//            fos = new java.io.FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        bos = new BufferedOutputStream(fos);
//        try {
//            bos.write(rawBytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            String ext = "png";
            FastDFSFile fastDFSfile = new FastDFSFile(rawBytes,ext);
            long length = rawBytes.length;
            NameValuePair[] meta_list = new NameValuePair[4];
            meta_list[0] = new NameValuePair("fileName", fileName);
            meta_list[1] = new NameValuePair("fileLength", String.valueOf(length));
            meta_list[2] = new NameValuePair("fileExt", ext);
            meta_list[3] = new NameValuePair("fileAuthor", "PinMerchant");
            logger.info("开始上传图片"+fileName+" 大小："+length+"bytes");
            filePath = FileManager.upload(fastDFSfile, meta_list);
            logger.info("上传完毕"+filePath);
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, requestEntity, JSONObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;


//        FileSystemResource fileResource = new FileSystemResource(new File("D://" + fileName));
//
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
//        requestHeaders.setContentType(MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));
//
//        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<String, Object>();
//        requestBody.add("smfile", fileResource);
//
//        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, requestHeaders);

//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, requestEntity, JSONObject.class);
//        return responseEntity;
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
