# TickSeekBar

[![DOWNLOAD](https://api.bintray.com/packages/warkiz/maven/tickseekbar/images/download.svg)](https://bintray.com/warkiz/maven/tickseekbar/_latestVersion)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

This is a customizable SeekBar library on Android. Also, If you need Indicator to show top of seek bar, please see [the other library](https://github.com/warkiz/IndicatorSeekBar). [  中文.md ](https://github.com/warkiz/TickSeekBar/blob/master/README_zh.md)


## OverView
<img src="https://github.com/warkiz/TickSeekBar/blob/master/gif/overview.png?raw=true" width = "392" height = "115"/>

## Screenshot

<img src="https://github.com/warkiz/TickSeekBar/blob/master/gif/continuous.gif?raw=true" width = "264" height = "464"/><img src="https://github.com/warkiz/TickSeekBar/blob/master/gif/discrete.gif?raw=true" width = "264" height = "464"/><img src="https://github.com/warkiz/TickSeekBar/blob/master/gif/custom.gif?raw=true" width = "264" height = "464"/>

## Demo
[download](https://github.com/warkiz/TickSeekBar/blob/master/apk/demo.apk)

 Scan QR code to download:
<img src="https://github.com/warkiz/TickSeekBar/blob/master/gif/demo_qrcode.png?raw=true" width = "100" height = "100"/>

## Setup

```gradle
implementation 'com.github.warkiz.tickseekbar:tickseekbar:0.1.4'
```

> 如果想要使用本分支的fork

```gradle
implementation 'com.github.jdpxiaoming.tickseekbar:tickseekbar:0.1.5.1'
```

## Usage
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
#### 控制thumb text的y距离和尺寸
![pic](https://github.com/jdpxiaoming/TickSeekBar/blob/master/gif/tick_thumby.png?raw=true)
```xml
<com.warkiz.tickseekbar.TickSeekBar
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:tsb_progress="88"
            app:tsb_show_thumb_text="above"
            app:tsb_thumb_color="@color/color_blue"
            app:tsb_thumb_text_color="@color/color_pink"
            app:tsb_track_progress_color="@color/color_blue"
            app:tsb_tick_texts_size="8sp"
            app:tsb_thumb_text_distance="5dp"
            />
```

#### 增加左右两侧自增按钮.
![pic](https://github.com/jdpxiaoming/TickSeekBar/blob/master/gif/auto.png?raw=true)


#### 自定义thumb text显示内容可用于实现任意step步长
#### delegate listener for thumb text .
```Java
final List<String> rateArray = new ArrayList();
        rateArray.add("5");
        rateArray.add("15");
        rateArray.add("55");
        rateArray.add("115");
        rateArray.add("225");
        rateArray.add("255");

        if(rateArray!=null && rateArray.size()>0){
            delegateSeekBar.setOnSeekCallback(new OnSeekCallBack() {
                @Override
                public String getThumbText(float progress) {
                    int index = Math.round(progress);
                    if(index<rateArray.size()){
                        return rateArray.get(index);
                    }
                    return rateArray.get(rateArray.size()-1);
                }
            });
            //为了保持对齐 : index会滑到size最大值，而数组是从0开始的
            delegateSeekBar.setMax(rateArray.size()-1);
            delegateSeekBar.setProgress(0);
        }
```
#### 获取当前显示的thumb text. 
```txt
   /**
       *
       * @return thumb text content .
       */
      public String getThumbText(){
          return mThumbTextContent;
      }      
```

### 在`seekbar`外部增加手动点击的`十` or `一`
```xml
<com.warkiz.tickseekbar.AutoTickSeekBar
            android:id="@+id/auto_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:action_color="#CB8DFF"
            >
            <com.warkiz.tickseekbar.TickSeekBar
                android:id="@+id/tick_seek_bar_auto"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:tsb_thumb_color="#8874C5"
                app:tsb_thumb_text_color="#8874C5"
                app:tsb_track_progress_color="#8874C5"
                app:tsb_show_thumb_text="above"
                app:tsb_tick_texts_size="12sp"
                app:tsb_thumb_adjust_auto="true"
                app:tsb_thumb_text_distance="5dp"
                app:tsb_track_background_size="8dp"
                app:tsb_track_progress_size="8dp"
                app:tsb_track_rounded_corners="true"
                />
        </com.warkiz.tickseekbar.AutoTickSeekBar>
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

## Custom section tracks color
The color of every block of seek bar can also be custom.

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

## Selector drawable&color were supported

You can set the StateListDrawable or ColorStateList for the thumb, tickMarks;
Also, ColorStateList for tickTexts is supported, too. Usage's format acccording to:

Thumb selector drawable:

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--this drawable is for thumb when pressing-->
    <item android:drawable="@mipmap/ic_launcher_round" android:state_pressed="true" />
    <!--for thumb in normal-->
    <item android:drawable="@mipmap/ic_launcher" />
</selector>
```

Thumb selector color:

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--this color is for thumb which is at pressing status-->
    <item android:color="@color/colorAccent" android:state_pressed="true" />
    <!--for thumb which is at normal status-->
    <item android:color="@color/color_blue" />
</selector>
```

TickMarks selector drawable：

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--this drawable is for tickMarks when pressing-->
    <item android:drawable="@mipmap/ic_launcher_round" android:state_selected="true" />
    <!--for tickMarks in normal-->
    <item android:drawable="@mipmap/ic_launcher" />
</selector>
```

TickMarks selector color：

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!--this color is for marks those are at left side of thumb-->
    <item android:color="@color/colorAccent" android:state_selected="true" />
    <!--for marks those are at right side of thumb-->
    <item android:color="@color/color_gray" />
</selector>
```

TickTexts selector color：

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

## Proguard

``` groovy
-dontwarn com.warkiz.tickseekbar.**
```

## Attributes

[ attr.xml ](https://github.com/warkiz/TickSeekBar/blob/master/tickseekbar/src/main/res/values/attr.xml)

## Support & Contact me

感谢：
自从在文档里公布了二维码，我收到国内的朋友的一些打赏，虽然金额不大，但是一些支持和鼓励的话语还是让我感到开心，非常感谢。

感谢所有之前支持我的朋友。如果下次你要给我打赏，可以顺带写上你的github地址，我会在这里用链接贴出来，算是相互鼓励。

感谢你们的支持。


<img src="https://github.com/warkiz/IndicatorSeekBar/blob/master/app/src/main/res/mipmap-xxhdpi/alipay.png?raw=true" width = "400" height = "531"/><img src="https://github.com/warkiz/IndicatorSeekBar/blob/master/app/src/main/res/mipmap-xxhdpi/wechat_pay.png?raw=true" width = "387" height = "531"/>

Feel free to contact me if you have any trouble on this project:
1. Create an issue.
2. Send mail to me, "warkiz".concat("4j").concat("@").concat("gmail.com")

## License

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
