# ContactSelector
[![JitPack](https://jitpack.io/v/awenzeng/ContactSelector.svg)](https://jitpack.io/#awenzeng/ContactSelector)
[![Downloads](https://jitpack.io/v/awenzeng/ContactSelector/month.svg)](https://jitpack.io/#awenzeng/ContactSelector)

A simple contact selector app.Please feel free to use this. (Welcome to Star and Fork)

# Demo：
[Download apk](https://fir.im/c6ta)

![](https://github.com/awenzeng/ContactSelector/blob/master/resource/contact_selector.gif)

# Download
You can download a jar from GitHub's [releases page](https://github.com/awenzeng/ContactSelector/releases).

Or use Gradle:
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
	dependencies {
	        compile 'com.github.awenzeng:ContactSelector:1.0.0'
	}


```
Or Maven:
```java
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  
<dependency>
	    <groupId>com.github.awenzeng</groupId>
	    <artifactId>ContactSelector</artifactId>
	    <version>1.0.0</version>
	</dependency>
```
For info on using the bleeding edge, see the [Snapshots](https://jitpack.io/#awenzeng/ContactSelector) wiki page.

# ProGuard
Depending on your ProGuard (DexGuard) config and usage, you may need to include the following lines in your proguard.cfg 

```java
#Glide混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

##tinypinying
-keep class com.github.promeg.pinyinhelper.**{*;}

## app proguard
-keep class com.awen.contact.widget.**{*;}

##Rxjava
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

```
# How do I use Camera?
Simple use cases with camera's generated API will look something like this:

In your Activity:
```java
        mPermissionsModel.checkContactsPermission(new PermissionsModel.PermissionListener() {
                    @Override
                    public void onPermission(boolean isPermission) {
                        if(isPermission){
                            Intent intent = new Intent(MainActivity.this, ContactSelectorActivity.class);
                            intent.putExtra(ContactSelectorActivity.CHOOSE_MODE, RModelAdapter.MODEL_SINGLE);
                            startActivityForResult(intent, RESULT_CODE);
                        }
                    }
      });
        
    
       @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case RESULT_CODE:
                ArrayList<ContactsInfo> contactsInfos = (ArrayList<ContactsInfo>) data.getSerializableExtra(ContactSelectorActivity.REQUEST_OUTPUT);
                contactAdapter.setmListData(contactsInfos);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

```

# License

```java
Copyright 2017 AwenZeng

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```



