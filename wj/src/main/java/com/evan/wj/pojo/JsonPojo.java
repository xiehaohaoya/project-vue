package com.evan.wj.pojo;
// json的pojo类型 {"ErrorInfo":{"Message":"成功","Info":{"Name": "谢昊昊"}},"RetInfo":{"Age": 24}}
public class JsonPojo {
    ErrorInfo ErrorInfo;
    RetInfo RetInfo;
}

class ErrorInfo {
    String Message;
    Info Info;
}

class Info {
    String Name;
}

class RetInfo {
    int Age;
}