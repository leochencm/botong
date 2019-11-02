package com.botongglcontroller;

/**
 * Created by hasee on 2016/12/19.
 */

public class Api {

    public static String Apikey = "5e7qnW94qbYX4=Cm5tJ==d3Y7sY=";
    // public static String BASEURL = "http://mk105.mantoo.com.cn:5486/";
    public static String BASEURL = "http://118.178.59.142:5488/";
    // public static String BASEURL = "http://yican.mantoo.com.cn:1051/";
    //public static String BASEURL ="http://220e48h291.51mypc.cn:80/";
    // public static String BASEURL ="http://192.168.1.7:23440/";
    public static String PUBLIC = BASEURL + "Boiler.asmx/";


    //    获取加密信息
    public static String GetScreatMsg = PUBLIC + "GetScreatMsg";
    //    登录
    public static String Login = PUBLIC + "Login";
    public static String LoginA = PUBLIC + "LoginA";
    //    注册发送验证码
    public static String Register_Mobile = PUBLIC + "Register_Mobile";
    //    注册保存密码
    public static String Register_Password = PUBLIC + "Register_Password";
    //    修改公司信息
    public static String ChangeCompany = PUBLIC + "ChangeCompany";
    //    修改地址信息
    public static String ChangeAddress = PUBLIC + "ChangeAddress";
    //    修改联系人信息
    public static String ChangeContactName = PUBLIC + "ChangeContactName";
    //    修改旧手机号发送验证码
    public static String ChangeAccount_OldSendCode = PUBLIC + "ChangeAccount_OldSendCode";
    //    验证旧手机验证码
    public static String ChangeAccount_OldVerifyCode = PUBLIC + "ChangeAccount_OldVerifyCode";
    //    修改手机号之新手机号发送验证码
    public static String ChangeAccount_NewSendCode = PUBLIC + "ChangeAccount_NewSendCode";
    //    验证新手机验证码
    public static String ChangeAccount_NewVerifyCode = PUBLIC + "ChangeAccount_NewVerifyCode";
    //    修改密码
    public static String ChangePassword = PUBLIC + "ChangePassword";
    //    找回密码发送短信
    public static String Recover_Mobile = PUBLIC + "Recover_Mobile";
    //    找回密码设置新密码
    public static String Recover_Password = PUBLIC + "Recover_Password";
    //    获取帐号信息
    public static String GetAccountMsg = PUBLIC + "GetAccountMsg";
    //    获取帐号信息及权限
    public static String GetAccountMsgA = PUBLIC + "GetAccountMsgA";
    //    绑定锅炉
    public static String BindBoiler = PUBLIC + "BindBoiler";
    //    解除绑定锅炉
    public static String RelieveBindBoiler = PUBLIC + "RelieveBindBoiler";
    //    获取锅炉列表
    public static String GetBoilers = PUBLIC + "GetBoilers";
    //获取锅炉列表
    public static String GetBoilersList = PUBLIC + "GetBoilersList";
    //    获取锅炉参数
    public static String GetBoilersPara = PUBLIC + "GetBoilersPara";
    //是否在线
    public static String IsOnline = PUBLIC + "IsOnline";
    //    获取锅炉参数名字
//public static String GetBoilersParaName = PUBLIC+"GetBoilersParaName";
//    设置锅炉参数
    public static String SetBoilersAttribute = PUBLIC + "SetBoilersAttribute";
    //  设置锅炉参数：
    public static String SetBoilersSwitches = PUBLIC + "SetBoilersSwitches";
    //    获取实时状态
    public static String GetBoilerState = PUBLIC + "GetBoilerState";
    //    切换网络工作环境
    public static String SwitchWorkWifi = PUBLIC + "SwitchWorkWifi";

    //    获取建议列表
    public static String GetSuggestion = PUBLIC + "GetSuggestion";
    //    上传建议
    public static String UploadSuggestion = PUBLIC + "UploadSuggestion";
    //    获取消息列表
    public static String GetMessages = PUBLIC + "GetMessages";
    //    上传消息
    public static String UploadMessages = PUBLIC + "UploadMessages";
    //    监测网络
    public static String TestNetWork = PUBLIC + "TestNetWork";

    //完全解锁锅炉：
    public static String OpenLock = PUBLIC + "OpenLock";

    //  获取版本号
    public static String GetVersion = PUBLIC + "GetVersion";

    // 28、获取消息通知
    public static String GetInform = PUBLIC + "GetInform";

    // 29、读取处理消息通知：
    public static String ReadInform = PUBLIC + "ReadInform";
    //  30、设备工作指令：
    public static String FacilityWorkControl = PUBLIC + "FacilityWorkControl";
    //  31、经销商连接验证：
    public static String VerifyConn = PUBLIC + "VerifyConn";
    //  32、获取账单接口：获取当前账户账单信息：
    public static String GetBill = PUBLIC + "GetBill";
    public static String GetBilldetail = PUBLIC + "GetBilldetail";
    public static String GetHelpinfo = PUBLIC + "GetHelpinfo";
    public static String HandleHelp = PUBLIC + "HandleHelp";
    public static String SeekHelp = PUBLIC + "SeekHelp";
    public static String ReplyAuthority = PUBLIC + "ReplyAuthority";

    //消息接口
    public static String GetInformNum = PUBLIC + "GetInformNum";
    //获取锅炉分配经销商列表
    public static String GetRuleBoilerlogin = PUBLIC + "GetRuleBoilerlogin";
    // 7、获取设备固件版本号：联网状态下进入锅炉运行工况，获取当前锅炉固件版本号和固件最新版本号
    public static String GetFirmwareVersion = PUBLIC + "GetFirmwareVersion";
    public static String PayBill = PUBLIC + "PayBill";
    //分配锅炉
    public static String SENDALLOTBOILER = PUBLIC + "SendAllotBoiler";
    //重置锅炉经销商
    public static String RESETALLOTSELLER = PUBLIC + "ResetAllotSeller";
    //查询供应商已分配的锅炉
    public static String SEARCHALLOTBOILER = PUBLIC + "SearchAllotBoiler";
    public static String aliPayOrder = PUBLIC + "aliPayOrder";

    // 升级
    public static String InformUpdate = PUBLIC + "InformUpdate";


}
