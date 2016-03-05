# AIDL-transfer-Byte-example
本例子利用AIDL实现了进程间图片的传输。基本流程是：client端发送请求，server端下载byte类型的数据并通过接口函数回传。
由于底层是通过传输byte实现的，因此该例子还可以扩展为传输其他可与byte互相转换的复杂类型。

##基本原理
1. 整个工程含两个module，一个是client端（即app），另一个是server端。

2. server端实质上是定义并注册了一个service类。系统内所有其他进程皆可调用该服务进行下载。

3. 调用的具体方法是给出所要下载文件的url，赋值给client端的urlString变量，然后绑定服务即可。

##使用方法
首先运行aidlserver这个module（开启服务），然后运行app（调用服务），界面加载完毕后即可看到下载好的图片。

##注意
1. 受限于Android本身对AIDL的设计，利用AIDL传输的数据大小不得大于1MB，否则会报TransactionTooLargeException，目前代码里暂时没有考虑该问题的解决方案。
2. server端的mainactivity其实无关紧要，可以在manifest里去掉它。
