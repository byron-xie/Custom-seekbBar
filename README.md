# StrongerSeekbBar
非添加windowmanager实现漂浮框，因为在项目中用到过 层级太高覆盖不了，处理麻烦 
后来自定义了一个strongerbar来完成需求。支持帧动画！！支持颜色渐变，支持单色，支持二进度等。。
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/ezgif-4-eb8d7d5ab1.gif?raw=true)
==
向外暴露的接口：onStateChangeListener();
----
函数:onColorChangeListener(int positon,int MaxPosition;int color;)返回值void
----
函数onThumbNeedAnimation(int position,int maxposition,int radius);返回值bitmap
----
 (帧动画实现)；返回一个拖动按钮的图片;可以使用同一个；(支持首次初始化回调)
函数 onBubbleTextNeedUpdate(int position ,int maxposition);显示bubbleText的时候才会回调
返回值string字符串；
 ----

![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/viewfile.jpg?raw=true)
---
barHeight:为进度条的总高度 属性int dp；
----
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/viewfile%20(1).jpg?raw=true)
---
colorGradient:为进度条的渐变颜色；类型为数组，当数组color颜色为单一的时候不在渐变属性int [] ；
----
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/viewfile%20(2).jpg?raw=true)
---
currentPosition:目前的进度点 属性Int：
----
maxPosition;总进度为多少属性Int;
----
bgColor;strongerBar的背景颜色属性color
----
barMargin:margin值属性Int dp；
----
isvertical:方向为竖着的，默认水平显示 属性值boolean
----
offset:是否支持offset 属性值boolean，默认不支持：第一个为offset为true，第二个为false;
----
当支持的时候偏移量为圆的半径，以圆心点为计算位置：当不支持的时候，往右滑动的时候计算是以thumbbar右边计算，往左滑动左边计算；进度条的长度大小不变，
---
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/viewfile%20(3).jpg?raw=true)
---
offsetNopadding 是否充满控件，属性为boolean;默认值为true：看一下差异：
----
第一个图为false:第二图为true; 结果是少了一个圆的直径；
此时有疑问：为什么这么设计？
 ---
1，当以圆中心计算进度，也就是offset为false; offsetNopadding为true的情况下；thumbar会超出控件的半径；所以offsetNopadding就是为了防止thumbar超出显示范围而设定的；(原生不支持，原生默认减thumbar的1/2左右的偏移量)；
----
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/viewfile%20(4).jpg?raw=true)
----
SencondColor:属性为int 为第二进度；颜色自定义，如果不设置则不显示第二进度
----
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/viewfile%20(5).jpg?raw=true)
---
textFacePath:text文本类型：类型string;需要路径。如果不设置，默认使用原生自带的类型
----
frameColor;边框颜色，类型 color (图片边框为红色);
----
frameWidth 边框宽度 类型int px;,(自适应，当边框太宽会超过显示范围这时候组件会自己计算，给出合理范围并显示正确)
----
bubbleMargin 类型文字距离thumb icon距离（参考一下图片）
----
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/viewfile%20(6).jpg?raw=true)
 ---
BubbleTextSize:文字显示的大小sp类型，默认为14
----
isShowBubble类型Boolean是否显示漂浮的文字默认不显示
----
ColorChangeCallBack;当为颜色渐变的进度多少时候 是否需要回调；默认不回调
----
Adjustname:调节颜色的名字
----
adjustNameMargin;文字距离进度条的垂直距离，默认0dp;
----
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/viewfile%20(7).jpg?raw=true)
----
AdjustNameSize；文字大小默认14sp;
----
Rotation:vertical情况下使用；属性枚举：left or right;
----
 

功能支持：seekbar支持的功能在这都支持 在开发成它的基础上又增加了新的共呢个，有考虑不足的地方还请（1_8_8_1_1_1_3_2_5_1_1）微信我 一起讨论
----
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/viewfile%20(8).jpg?raw=true)

![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/Screenshot_20171120-192847.png?raw=true)
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/Screenshot_20171120-192855.png?raw=true)
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/Screenshot_20171121-110614.png?raw=true)
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/Screenshot_20171121-112402.png?raw=true)
![image](https://github.com/byron-xie/StrongerSeekbBar/blob/master/app/src/main/res/drawable/Screenshot_20171121-112416.png?raw=true)
