AIDL-transfer-Byte-example
-----
本例子利用`AIDL`实现了进程间图片的传输。基本流程是：client端发送请求，server端下载byte类型的数据并通过接口函数回传。
由于底层是通过传输byte实现的，因此该例子还可以扩展为传输其他可与byte互相转换的复杂类型。由于Server与Client端之间仅靠`.aidl`文件进行通信，因此Server端如何进行下载对Client端没有影响。`AIDL`即体现出了`interface`的作用。

###环境
Mac OS X  
Android Studio 1.5

###基本原理

1. 整个工程含两个module，一个是client端（即app），另一个是server端。

2. server端实质上是定义并注册了一个service类。系统内所有其他进程皆可调用该服务进行下载。

3. 调用的具体方法是给出所要下载文件的url，赋值给client端的urlString变量，然后绑定服务即可。

###使用方法
首先运行aidlserver这个module（开启服务），然后运行app（调用服务），界面加载完毕后即可看到下载好的图片。

###注意  
~~受限于Android本身对AIDL的设计，利用AIDL传输的数据大小不得大于1MB，否则会报TransactionTooLargeException，目前代码里暂时没有考虑该问题的解决方案。~~   
**--update--**  
**2016-03-05**  
`问题已解决，方法为对每个较大的请求拆分成若干个小于1MB的Request，然后依次调用server端进行传输，将结果放到一个大的byte缓冲区进行拼接恢复即可。因此现在传输的图片大小可以大于1MB，工程中所给的url链接指向的图片大小为3.1MB，测试通过。`

