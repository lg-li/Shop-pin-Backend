package cn.edu.neu.shop.pin.service.message;
import com.alibaba.fastjson.JSONObject;

public class TemplateMessageFactory {
    private String touser;
    private String template_id;
    private String page;
    private String form_id;
    private JSONObject data;

    private TemplateMessageFactory(String touser, String template_id, String page, String form_id, JSONObject data) {
        this.touser = touser;
        this.template_id = template_id;
        this.page = page;
        this.form_id = form_id;
        this.data = data;
    }

    public static JSONObject generateOrderReceiptConfirmedMessage(String openId, String page, String form_id, String... parameters) {
        JSONObject dataJSON = getParameters(parameters);
        return (JSONObject) JSONObject.toJSON(new TemplateMessageFactory(openId, "DfNz3n-zjdFYxN28Wz0mzecvfkRlVzlNzvDasZkHiFw", page, form_id, dataJSON));
    }

    public static JSONObject generateOrderPaymentSuccessMessage(String openId, String page, String form_id, String... parameters) {
        JSONObject dataJSON = getParameters(parameters);
        return (JSONObject) JSONObject.toJSON(new TemplateMessageFactory(openId, "wgBmj6xFFoJ8BdlyDlaLMCJwheguZgcp8RAo5luuT_w", page, form_id, dataJSON));
    }

    public static JSONObject generateOrderShippedMessage(String openId, String page, String form_id, String... parameters) {
        JSONObject dataJSON = getParameters(parameters);
        return (JSONObject) JSONObject.toJSON(new TemplateMessageFactory(openId, "gc1Bs6caVzDz0exD42cUjOvUx5ErtHoLkMShFUlhFPU", page, form_id, dataJSON));
    }

    public static JSONObject generateGroupSuccessfullyClosedMessage(String openId, String page, String form_id, String... parameters) {
        JSONObject dataJSON = getParameters(parameters);
        return (JSONObject) JSONObject.toJSON(new TemplateMessageFactory(openId, "X4UYvNXzGefhKGut9v4xz_knxJcUmaSrFfUEItCe-zg", page, form_id, dataJSON));
    }

    private static JSONObject getParameters(String[] parameters) {
        JSONObject dataJSON = new JSONObject();
        for (int i = 1; i <= parameters.length; i++) {
            JSONObject temp = new JSONObject();
            temp.put("value", parameters[i - 1]);
            dataJSON.put("keyword" + i, temp);
        }
        return dataJSON;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
