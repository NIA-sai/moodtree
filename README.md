屎

基本一点错误处理也没做

就连了个api

GET 方法 /content/AIGC/{mood},得到 text 和 imgUrl

还有webclient到底怎么写同步啊

才开机或者token才过期的几次请求可能因为异步的原因出错，虽然稍微等一下就好了，但是我也觉得不该用 webclient 力 😳

于是在学长的指点下选择定时检查，提前一分钟刷新token

Supreme-Demon-Dragon：1月21日，在原有项目文件之间新加了一个文件夹“moodtree”，里面是我重做的另一个版本：没有用WebClient，有异常信息汇报，所有返回类型都为Result类，方便前端使用、调试。
但是测试的时候还有一点问题。我正在改。

mol 无上魔龙 佬👆

Supreme-Demon-Dragon：1月22日，完善了我写的moodtree的所有功能。没有任何bug，返回结果清晰，可以使用。文件在moodtree_Final中

remol 无上魔龙 佬👆



1.24: 添加了 POST 方法 /content/AIGC, “分析天气和心情” ，先内容随便，满足json格式就行，待移动✌测试
