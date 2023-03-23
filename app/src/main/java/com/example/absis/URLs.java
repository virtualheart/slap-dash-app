package com.example.absis;

public class URLs {
        // ROOT URL MEANS DOMAIN NAME OR LOCALHOST(END WITH "/"BACKSLASH)
        private static final String ROOT_URL = "http://192.168.1.8/com/";
        private static final String URL_APIFILE="api.php";
        private static final String URL_APICALL="?apicall=";
        private static final String URL_ROOT=ROOT_URL+URL_APIFILE+URL_APICALL;
        public static final String URL_REGISTER = URL_ROOT + "register";
        public static final String URL_LOGIN= URL_ROOT + "login";
        public static final String URL_COMPFILE= URL_ROOT + "compfile";
        public static final String URL_COMPLIST= URL_ROOT + "complist";
        public static final String URL_USERCOMPLIST= URL_ROOT + "usercomplist";
        public static final String URL_USERCLIST= URL_ROOT + "userclist";
        public static final String URL_USERRLIST= URL_ROOT + "userrlist";
        public static final String URL_UPDATECOM= URL_ROOT + "updatecom";

}
