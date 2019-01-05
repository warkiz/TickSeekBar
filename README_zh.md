# TickSeekBar

[![DOWNLOAD](https://api.bintray.com/packages/warkiz/maven/tickseekbar/images/download.svg)](https://bintray.com/warkiz/maven/tickseekbar/_latestVersion)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

这是一个安卓自定义SeekBar库。 另外, 如果你需要指示器显示在SeekBar的顶部, 请看另外一个[SeekBar库](https://github.com/warkiz/TickSeekBar)。  [  English.md ](https://github.com/warkiz/TickSeekBar/blob/master/README.md)

## 预览

<img src="https://github.com/warkiz/TickSeekBar/blob/master/gif/overview.png?raw=true" width = "392" height = "115"/>


## 截图

<img src="https://github.com/warkiz/TickSeekBar/blob/master/gif/continuous.gif?raw=true" width = "264" height = "464"/><img src="https://github.com/warkiz/TickSeekBar/blob/master/gif/discrete.gif?raw=true" width = "264" height = "464"/><img src="https://github.com/warkiz/TickSeekBar/blob/master/gif/custom.gif?raw=true" width = "264" height = "464"/>

## 演示
[下载](https://github.com/warkiz/TickSeekBar/blob/master/apk/demo.apk)

扫描二维码下载:
<img src="https://github.com/warkiz/TickSeekBar/blob/master/gif/demo_qrcode.png?raw=true" width = "100" height = "100"/>

## 初始化

```gradle
implementation 'com.github.warkiz.tickseekbar:tickseekbar:0.1.4'
```

## 使用
#### xml

```xml
<com.warkiz.tickseekbar.TickSeekBar
    android:id="@+id/listener"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:tsb_ticks_count="5"
    app:tsb_thumb_color="@color/colorAccent"
    app:tsb_thumb_size="16dp"
    app:tsb_show_tick_marks_type="oval"
    app:tsb_tick_marks_color="@color/colorAccent"
    app:tsb_tick_marks_size="8dp"
    app:tsb_show_tick_texts="above"
    app:tsb_tick_texts_color="@color/color_pink"
    app:tsb_tick_texts_size="13sp"
    app:tsb_track_background_color="@color/color_gray"
    app:tsb_track_background_size="2dp"
    app:tsb_track_progress_color="@color/color_blue"
    app:tsb_track_progress_size="3dp" />
```

#### Java

```Java
TickSeekBar seekBar = TickSeekBar
             .with(getContext())
             .max(200)
             .min(10.2f)
             .progressValueFloat(true)
             .progress(33)
             .tickCount(7)
             .showTickMarksType(TickMarkType.DIVIDER)
             .tickMarksColor(getResources().getColor(R.color.color_blue))
             .tickMarksSize(6)//dp
             .tickTextsSize(13)//sp
             .showTickTextsPosition(TextPosition.ABOVE)
             .tickTextsColorStateList(getResources().getColorStateList(R.color.selector_tick_texts_3_color))
             .thumbColor(Color.parseColor("#ff0000"))
             .thumbSize(14)
             .trackProgressColor(getResources().getColor(R.color.colorAccent))
             .trackProgressSize(4)
             .trackBackgroundColor(getResources().getColor(R.color.color_gray))
             .trackBackgroundSize(2)
             .build();
```

## 自定义每一节的track块颜色
seekbar上每一块track的颜色都能被设置：

```Java
sectionSeekBar.customSectionTrackColor(new ColorCollector() {
    @Override
    public boolean collectSectionTrackColor(int[] colorIntArr) {
        //the length of colorIntArray equals section count
        colorIntArr[0] = getResources().getColor(R.color.color_blue, null);
        colorIntArr[1] = getResources().getColor(R.color.color_gray, null);
        colorIntArr[2] = Color.parseColor("#FF4081");
        ...
        return true; //True if apply color , otherwise no change
    }
});
```

## 支持Selector类型的图片或颜色

你可以为滑块和tick标记设置StateListDrawable 或者 ColorStateList, 而且，tick下面的文字也支持ColorStateList,使用的格式如下:

滑块图片 selector:

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--this drawable is for thumb when pressing-->
    <item android:drawable="@mipmap/ic_launcher_round" android:state_pressed="true" />
    <!--for thumb in normal-->
    <item android:drawable="@mipmap/ic_launcher" />
</selector>
```

滑块颜色 selectorr:

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--this color is for thumb which is at pressing status-->
    <item android:color="@color/colorAccent" android:state_pressed="true" />
    <!--for thumb which is at normal status-->
    <item android:color="@color/color_blue" />
</selector>
```

tick标记图片 selector：

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--this drawable is for tickMarks when pressing-->
    <item android:drawable="@mipmap/ic_launcher_round" android:state_selected="true" />
    <!--for tickMarks in normal-->
    <item android:drawable="@mipmap/ic_launcher" />
</selector>
```

tick标记颜色 selector：

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--this color is for marks those are at left side of thumb-->
    <item android:color="@color/colorAccent" android:state_selected="true" />
    <!--for marks those are at right side of thumb-->
    <item android:color="@color/color_gray" />
</selector>
```

tick文字颜色 selector：

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--this color is for texts those are at left side of thumb-->
    <item android:color="@color/colorAccent" android:state_selected="true" />
    <!--for tick text which is stopped under thumb -->
    <item android:color="@color/color_blue" android:state_hovered="true" />
    <!--for texts those are at right side of thumb-->
    <item android:color="@color/color_gray" />
</selector>
```

## 监听器
```Java
seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
        @Override
        public void onSeeking(SeekParams seekParams) {
            Log.i(TAG, seekParams.seekBar);
            Log.i(TAG, seekParams.progress);
            Log.i(TAG, seekParams.progressFloat);
            Log.i(TAG, seekParams.fromUser);
            //when tick count > 0
            Log.i(TAG, seekParams.thumbPosition);
            Log.i(TAG, seekParams.tickText);
        }

        @Override
        public void onStartTrackingTouch(TickSeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(TickSeekBar seekBar) {
        }

});
```

## 混淆配置

``` groovy
-dontwarn com.warkiz.tickseekbar.**
```

## 属性

[ attr.xml ](https://github.com/warkiz/TickSeekBar/blob/master/tickseekbar/src/main/res/values/attr.xml)

## 支持 & 联系我

感谢：
自从在文档里公布了二维码，我收到国内的朋友的一些打赏，虽然金额不大，但是一些支持和鼓励的话语还是让我感到开心，非常感谢。

感谢所有之前支持我的朋友。如果下次你要给我打赏，可以顺带写上你的github地址，我会在这里用链接贴出来，算是相互鼓励。

感谢你们的支持。


<img src="https://github.com/warkiz/IndicatorSeekBar/blob/master/app/src/main/res/mipmap-xxhdpi/alipay.png?raw=true" width = "400" height = "531"/><img src="https://github.com/warkiz/IndicatorSeekBar/blob/master/app/src/main/res/mipmap-xxhdpi/wechat_pay.png?raw=true" width = "387" height = "531"/>

Feel free to contact me if you have any trouble on this project:
1. Create an issue.
2. Send mail to me, "warkiz".concat("4j").concat("@").concat("gmail.com")

## 许可

	Copyright (C) 2017 zhuangguangquan warkiz

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
