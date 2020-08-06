# SmartProgressBar
漂亮的进度条，如丝般顺滑，样式风格有水平、竖直、圆环、扇形......
------------------------------------------------------------

<div align="center">
    <img src="https://github.com/FPhoenixCorneaE/SmartProgressBar/blob/master/preview/smart_progress_bar.png" width="320"/>
</div>

-----------------------------

How to include it in your project:
--------------
**Step 1.** Add the JitPack repository to your build file
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency
```groovy
dependencies {
	implementation 'com.github.FPhoenixCorneaE:SmartProgressBar:1.0.2'
}
```

xml中使用
--------------
`水平样式进度条`
```xml
<com.wkz.smartprogressbar.SmartProgressBar
        android:id="@+id/spb_horizontal"
        android:layout_width="match_parent"
        android:layout_height="20dp"
        app:spb_animated="false"
        app:spb_animated_duration="1000"
        app:spb_border_color="@android:color/holo_red_dark"
        app:spb_border_width="3dp"
        app:spb_bottom_left_radius="0dp"
        app:spb_bottom_right_radius="0dp"
        app:spb_max="100"
        app:spb_percent_text_color="@android:color/white"
        app:spb_percent_text_size="12sp"
        app:spb_progress="70"
        app:spb_progress_bar_bg_color="@android:color/darker_gray"
        app:spb_progress_center_color="@android:color/holo_red_light"
        app:spb_progress_end_color="@android:color/holo_orange_light"
        app:spb_progress_start_color="@android:color/holo_blue_light"
        app:spb_radius="30dp"
        app:spb_shape_style="HORIZONTAL"
        app:spb_show_percent_sign="true"
        app:spb_show_percent_text="true"
        app:spb_top_left_radius="15dp"
        app:spb_top_right_radius="15dp" />
```

`竖直样式进度条`
```xml
<com.wkz.smartprogressbar.SmartProgressBar
        android:id="@+id/spb_vertical"
        android:layout_width="20dp"
        android:layout_height="250dp"
        android:layout_marginTop="10dp"
        app:spb_animated="true"
        app:spb_animated_duration="1500"
        app:spb_border_color="@android:color/holo_red_dark"
        app:spb_border_width="3dp"
        app:spb_bottom_left_radius="20dp"
        app:spb_bottom_right_radius="20dp"
        app:spb_max="100"
        app:spb_percent_text_color="@android:color/white"
        app:spb_percent_text_size="8sp"
        app:spb_progress="65"
        app:spb_progress_bar_bg_color="@android:color/darker_gray"
        app:spb_progress_center_color="@android:color/holo_red_light"
        app:spb_progress_end_color="@android:color/holo_orange_light"
        app:spb_progress_start_color="@android:color/holo_blue_light"
        app:spb_shape_style="VERTICAL"
        app:spb_show_percent_sign="true"
        app:spb_show_percent_text="true"
        app:spb_top_left_radius="20dp"
        app:spb_top_right_radius="20dp" />
```

`圆环样式进度条`
```xml
<com.wkz.smartprogressbar.SmartProgressBar
            android:id="@+id/spb_ring_1"
            android:layout_width="150dp"
            android:layout_height="150dp"
            app:spb_animated="true"
            app:spb_animated_duration="2000"
            app:spb_border_color="@android:color/holo_red_dark"
            app:spb_border_width="5dp"
            app:spb_clockwise="true"
            app:spb_max="100"
            app:spb_percent_text_color="@android:color/holo_red_light"
            app:spb_percent_text_size="18sp"
            app:spb_progress="75"
            app:spb_progress_bar_bg_color="@android:color/darker_gray"
            app:spb_progress_center_color="@android:color/holo_red_light"
            app:spb_progress_end_color="@android:color/holo_orange_light"
            app:spb_progress_start_color="@android:color/holo_blue_light"
            app:spb_radius="60dp"
            app:spb_shape_style="RING"
            app:spb_show_percent_sign="true"
            app:spb_show_percent_text="true" />
```

`扇形样式进度条`
```xml
<com.wkz.smartprogressbar.SmartProgressBar
            android:id="@+id/spb_ring_2"
            android:layout_width="150dp"
            android:layout_height="150dp"
            android:layout_marginStart="30dp"
            android:layout_marginLeft="30dp"
            app:spb_animated="true"
            app:spb_animated_duration="2500"
            app:spb_border_color="@android:color/holo_red_dark"
            app:spb_border_width="5dp"
            app:spb_clockwise="false"
            app:spb_max="100"
            app:spb_percent_text_color="@android:color/holo_red_light"
            app:spb_percent_text_size="18sp"
            app:spb_progress="75"
            app:spb_progress_bar_bg_color="@android:color/darker_gray"
            app:spb_progress_center_color="@android:color/holo_red_light"
            app:spb_progress_end_color="@android:color/holo_orange_light"
            app:spb_progress_start_color="@android:color/holo_blue_light"
            app:spb_radius="60dp"
            app:spb_shape_style="RING"
            app:spb_show_percent_sign="true"
            app:spb_show_percent_text="true" />
```

--------------------
