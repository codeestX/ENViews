#ENViews

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ENViews-green.svg?style=true)](https://android-arsenal.com/details/1/4749)
[![](https://jitpack.io/v/codeestX/ENViews.svg)](https://jitpack.io/#codeestX/ENViews)

ENViews, A cool dynamic view library.All designed by [Nick Buturishvili](https://dribbble.com/nick_buturishvili
)  
ENViews, 一个华丽丽的动效控件库，所有控件原型取自[Nick Buturishvili](https://dribbble.com/nick_buturishvili
)的设计作品

本项目为个人练习，发现问题或有建议欢迎[issue](https://github.com/codeestX/ENViews/issues)，email(codeest.dev@gmail.com),PR.  
如果你喜欢这些效果也欢迎戳个star支持一下.  
使用详情可以参考demo，如果需要引入到项目比起依赖库更推荐直接拷贝对应view的源码，方便根据需求随时自定义.  
源码中在关键的绘制点做了注释，方便阅读参考.


#Preview
| Original design | Android demo | Class |
| :--: | :--: | :--: |
| ![](http://odck16ake.bkt.clouddn.com/Github/o_download.gif) | ![](http://odck16ake.bkt.clouddn.com/Github/download.gif) |ENDownloadView|
| ![](http://odck16ake.bkt.clouddn.com/Github/o_volume.gif) | ![](http://odck16ake.bkt.clouddn.com/Github/volume.gif)|ENVolumeView|
| ![](http://odck16ake.bkt.clouddn.com/Github/o_loading.gif) | ![](http://odck16ake.bkt.clouddn.com/Github/loading.gif)  | ENLoadingView |
| ![](http://odck16ake.bkt.clouddn.com/Github/o_play.gif)| ![](http://odck16ake.bkt.clouddn.com/Github/play.gif) | ENPlayView |
| ![](http://odck16ake.bkt.clouddn.com/Github/o_search.gif)| ![](http://odck16ake.bkt.clouddn.com/Github/search.gif) |ENSearchView
|![](http://odck16ake.bkt.clouddn.com/Github/o_scroll.gif) | ![](http://odck16ake.bkt.clouddn.com/Github/scroll.gif)  |ENScrollView
|![](http://odck16ake.bkt.clouddn.com/Github/o_refresh.gif)  | ![](http://odck16ake.bkt.clouddn.com/Github/refresh.gif)  |ENRefreshView|

#Usage

Step 1. Add the JitPack repository to your build file

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
   
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.codeestX:ENViews:v1.0.2'
	}
	
Step 3. See DemoProject for details.


#Thanks
* [Nick Buturishvili](https://dribbble.com/nick_buturishvili)

* [GcsSloop](http://www.gcssloop.com/customview/CustomViewIndex)

#License

      Copyright (c) 2016 codeestX

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.
