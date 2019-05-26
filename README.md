# SmartProgressBar
智能的进度条，样式风格有水平、竖直、圆环、扇形、......
------------------------------------------------------------

![图片预览](https://github.com/FPhoenixCorneaE/SmartProgressBar/blob/master/preview/smart_progress_bar.png)

-----------------------------

xml中使用
--------------
`水平样式进度条`
```java
<com.wkz.smartprogressbar.SmartProgressBar
        android:id="@+id/spb_horizontal"
        android:layout_width="match_parent"
        android:layout_height="20dp"
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
```java
<com.wkz.smartprogressbar.SmartProgressBar
        android:id="@+id/spb_vertical"
        android:layout_width="20dp"
        android:layout_height="300dp"
        android:layout_marginTop="10dp"
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
```java
<com.wkz.smartprogressbar.SmartProgressBar
        android:id="@+id/spb_ring"
        android:layout_width="150dp"
        android:layout_height="150dp"
        android:layout_marginTop="10dp"
        app:spb_border_color="@android:color/holo_red_dark"
        app:spb_border_width="5dp"
        app:spb_max="100"
        app:spb_percent_text_color="@android:color/holo_red_light"
        app:spb_percent_text_size="18sp"
        app:spb_progress="80"
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
```java
<com.wkz.smartprogressbar.SmartProgressBar
        android:id="@+id/spb_sector"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        app:spb_border_color="@android:color/holo_red_dark"
        app:spb_border_width="3dp"
        app:spb_max="100"
        app:spb_percent_text_color="@android:color/white"
        app:spb_percent_text_size="18sp"
        app:spb_progress="90"
        app:spb_progress_bar_bg_color="@android:color/darker_gray"
        app:spb_progress_center_color="@android:color/holo_red_light"
        app:spb_progress_end_color="@android:color/holo_orange_light"
        app:spb_progress_start_color="@android:color/holo_blue_light"
        app:spb_shape_style="SECTOR"
        app:spb_show_percent_sign="true"
        app:spb_show_percent_text="true" />
```

--------------------
