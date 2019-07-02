package cn.edu.neu.shop.pin.nlp;

import cn.edu.neu.shop.pin.message_queue.consumer.GroupMessageConsumer;
import com.baidu.aip.nlp.AipNlp;
import com.baidu.aip.nlp.ESimnetType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NLPUtil {

    private static Logger logger = LoggerFactory.getLogger(NLPUtil.class);

    // 基本设置
    private static final String APP_ID = "16690605";
    private static final String API_KEY = "7isYGtkgxPF0nS4CAv3A9842";
    private static final String SECRET_KEY = "IaDQPA91676R20FTYB0ylD5Sk501o3Lq";

    private static AipNlp client;

    public static void init() {
        logger.info("初始化 NLP 工具箱...");
        // 初始化一个AipNlp
        client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        logger.info("NLP 工具箱初始化完成");
    }

    public static synchronized String analyzeCommentTag(String commentContent) {
        logger.info("评论情感分析：" + commentContent);
        JSONObject resultJSON = client.commentTag(commentContent, ESimnetType.SHOPPING, null);
        return resultJSON.getJSONArray("items").toString();
    }
}
