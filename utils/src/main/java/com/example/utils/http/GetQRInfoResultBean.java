package com.example.utils.http;

import java.util.List;

public class GetQRInfoResultBean {
    @Override
    public String toString() {
        return "GetQRInfoResultBean{" +
                "QRCode='" + QRCode + '\'' +
                ", ValidUntil='" + ValidUntil + '\'' +
                ", RemainTimes=" + RemainTimes +
                ", QRErrCode='" + QRErrCode + '\'' +
                ", QRErrMsg='" + QRErrMsg + '\'' +
                ", AlgorithmPara=" + AlgorithmPara +
                ", ListInfo=" + ListInfo +
                '}';
    }

    /**
     * QRCode : 4hjxkgO6T
     * ValidUntil : 2020-12-31
     * RemainTimes : 500
     * QRErrCode : 0
     * QRErrMsg : 
     * AlgorithmPara : [{"Title":"k","Order":1,"Value":"1"},{"Title":"b","Order":2,"Value":"0"}]
     * ListInfo : [{"Title":"剩余最大使用次数","Order":1,"Value":"2"},{"Title":"检测项目","Order":2,"Value":"甲醛"},{"Title":"检测方法","Order":3,"Value":"分光光度法"},{"Title":"判读方法","Order":4,"Value":"222"},{"Title":"国家标准","Order":5,"Value":"333"},{"Title":"国家标准单位","Order":6,"Value":"mg/kg"},{"Title":"灵敏度","Order":7,"Value":"1.000ppm"}]
     */

    private String QRCode;
    private String ValidUntil;
    private int RemainTimes;
    private String QRErrCode;
    private String QRErrMsg;
    private List<AlgorithmParaBean> AlgorithmPara;
    private List<ListInfoBean> ListInfo;

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public String getValidUntil() {
        return ValidUntil;
    }

    public void setValidUntil(String ValidUntil) {
        this.ValidUntil = ValidUntil;
    }

    public int getRemainTimes() {
        return RemainTimes;
    }

    public void setRemainTimes(int RemainTimes) {
        this.RemainTimes = RemainTimes;
    }

    public String getQRErrCode() {
        return QRErrCode;
    }

    public void setQRErrCode(String QRErrCode) {
        this.QRErrCode = QRErrCode;
    }

    public String getQRErrMsg() {
        return QRErrMsg;
    }

    public void setQRErrMsg(String QRErrMsg) {
        this.QRErrMsg = QRErrMsg;
    }

    public List<AlgorithmParaBean> getAlgorithmPara() {
        return AlgorithmPara;
    }

    public void setAlgorithmPara(List<AlgorithmParaBean> AlgorithmPara) {
        this.AlgorithmPara = AlgorithmPara;
    }

    public List<ListInfoBean> getListInfo() {
        return ListInfo;
    }

    public void setListInfo(List<ListInfoBean> ListInfo) {
        this.ListInfo = ListInfo;
    }

    public static class AlgorithmParaBean {
        @Override
        public String toString() {
            return "AlgorithmParaBean{" +
                    "Title='" + Title + '\'' +
                    ", Order=" + Order +
                    ", Value='" + Value + '\'' +
                    '}';
        }

        /**
         * Title : k
         * Order : 1
         * Value : 1
         */

        private String Title;
        private int Order;
        private String Value;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public int getOrder() {
            return Order;
        }

        public void setOrder(int Order) {
            this.Order = Order;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }
    }

    public static class ListInfoBean {
        @Override
        public String toString() {
            return "ListInfoBean{" +
                    "Title='" + Title + '\'' +
                    ", Order=" + Order +
                    ", Value='" + Value + '\'' +
                    '}';
        }

        /**
         * Title : 剩余最大使用次数
         * Order : 1
         * Value : 2
         */

        private String Title;
        private int Order;
        private String Value;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public int getOrder() {
            return Order;
        }

        public void setOrder(int Order) {
            this.Order = Order;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }
    }
}
