# PictureUnlock
#### Android: 图案解锁

- **原理：首先将所有点亮的点全部添加到对应的位置，然后设置`Visibility`为不可见。通过监听触摸，得到触摸点的坐标。判断触摸的点是否在可点亮的点的范围内，在则点亮。不是第一次点亮的时候还需要判断两个点之间的线条是否可以被点亮，是否可以被点亮的规则可以自己制定。每个点设置一个整型的数字，每个点连起来的线条用这些数字组成的字符串来代替。使用 `SharedPreferences` 对密码进行持续化存储。**

- **学习内容：`RelativeLayout` `代码布局` `View的setOnTouchListener` `SharedPreferences`**

- **难度：新手。**

- **博客发布处：[Android项目2：图案解锁](http://www.fanandjiu.com/article/d6ac3118.html)**

