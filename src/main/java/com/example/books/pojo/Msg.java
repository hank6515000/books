package com.example.books.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * json統一返回類型
 */
    public class Msg {
    /**
     * 狀態碼
     */
    private  int code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 返回數據
     */
    private Map<String,Object> data = new HashMap<>();

    /**
     * 返回成功信息
     * @return
     */
        public static Msg success(){
            Msg result = new Msg();
            result.setCode(200);
            result.setMsg("處理成功");
            return result;
        }
    /**
     * 返回失敗信息
     * @return
     */
        public static Msg fail(){
            Msg result = new Msg();
            result.setCode(400);
            result.setMsg("處理失敗");
            return result;
        }
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> extend) {
            this.data = extend;
        }

    /**
     * 增加返回內容
     * @param key
     * @param value
     * @return
     */
        public Msg add(String key, Object value) {
            this.getData().put(key,value);
            return this;
        }

    }
