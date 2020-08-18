## 前言
本篇文章主要讲解一个自己的一个三方库——EventLine，业务上的安全的、可感知的事件线。    
此库仍未开发完善，**请勿直接导入盈利项目，可导入普通demo项目或者用于学习使用。**

## 引入
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
```
dependencies {
	implementation 'com.github.zhaozuyuan:eventline:1.0.2-release'
}
```

## 概述
主要比对的是EventBus和RxBus。
- 它类似于RxBus，但是暂未支持RxJava；
- 它不会跟EventBus有冲突，它所针对的是业务上更加细粒度的事件传递，并非事件总线。
- 比起EventBus、RxBus，它的单线概念更加强烈，旨在创建一个更加安全的事件传输环境，此点与广播相反；
- 它支持初始化，可注入日志打印器、日志等级、IO线程池、CPU线程池；
- 它支持接收方限定接收事件的子类型；

若看完了还有些懵，请直接看演示。

## 演示
#### 初始化
- 它支持了自定义IO、CPU线程池，这利于实际项目的开发；
- 支持了自定义的日志打印器。
```
        //配置初始化信息
        ConfigParams params = new ConfigParams()
                //能否发现事件的父类
                .setCanFindEventParent(true)
                //cpu 线程
                .setCPUExecutor(null)
                //io 线程
                .setIOExecutor(null)
                //日志打印器
                .setLogger(new ILogger() {
                    @Override
                    public void logDebug(String msg) {
                        System.out.println(msg);
                    }

                    @Override
                    public void logWarn(String msg) {
                        System.out.println(msg);
                    }

                    @Override
                    public void logError(String msg) {
                        System.out.println(msg);
                    }
                })
                .setLogLevel(LogLevel.ERROR);
        //初始化库
        EventLineInitialization.init(params);
```

#### 发送事件
- 它在发送事件的时候要求必须传入当前发送者环境，多为当前Class；
- 它可以限定接收方，指定Class的接收方才能接收事件，不足的是这个功能**不支持多态**；
- 支持了一个状态池，仅发送粘性事件私有，代表的是发送方的状态；
```
        //发送事件准备
        //发送者的环境，多为当前类Class
        Class context = getClass();
        //限定接收方
        Class[] targets = new Class[]{ getClass() };
        //事件
        Object event = new Object();
        //自定义状态
        final int normalStatus = 1;
        final int hotStatus = 2;

        //发送事件
        EventSender.sendEvent(context, event);
        //发送事件+限定接收者
        EventSender.sendEvent(context, event, targets);
        //发送粘性事件
        EventSender.sendStickyEvent(context, event);
        //发送粘性事件+状态控制器
        StatusController controller = EventSender
                .sendStickyEventWithController(context, event, normalStatus);
        controller.refreshSenderStatus(hotStatus);

```

#### 接收事件
- 支持状态监听策略（**类似于拦截器**），可根据当前发送者的状态进行拦截或者线程调度；
- 类似于发送者，接收方也**必须指定“接收环境”**， 也可以指定接收某些发送方；
- 支持Lifecycle自动解绑；
- 支持单点设置是否能够接收事件的子类型对象；
- 支持一键切换线程，并且默认在当前线程，肯定不会存在多余的切换线程操作；
```
        //事件接收策略
        IEventStrategy strategy = new IEventStrategy() {
            @Override
            public void scheduleThread1(IScheduler threadScheduler) {
                //普通调度线程策略
                threadScheduler.currentThread();
            }

            @Override
            public boolean withCustomStatus(@Nullable IStatusReader statusReader,
                                            IScheduler threadScheduler) {
                //监控事件状态，可选择拦截事件继续传递
                //true 不拦截， false拦截事件
                return statusReader == null || statusReader.readSenderStatus() != normalStatus;
            }
        };

        //接收事件
        ICancellable cancellable = EventReceiver.generateReceiver(MainActivity.class, Object.class)
                //接收指定发送方的事件
                .setSpecifiedSenders(new Class[]{ getClass() })
                //自动解除
                .setAutoCancelObserver(getLifecycle())
                //能够接收粘性事件
                .canReceiveStickyEvent(true)
                //设置状态监听策略
                .setStatusStrategy(strategy)
                //能够接收监听事件的子类事件
                .canReceiveEventChild(true)
                //调度到主线程
                .scheduleToMainThread()
                //监听事件
                .observer(new IReceiver<Object>() {
                    @Override
                    public void onReceive(Object event) {
                        Log.d("tag", "-->" + event.toString());
                    }
                });
        //主动解绑
        cancellable.cancelObserver();
```

## 针对场景
- 事件发送方存在多状态的场景，使用带`StatusController`的粘性事件，接收方同样可以**感知**到发送方当前的状态，做出是否“拦截”的操作；
- 业务上代码复杂且需要事件传递的场景，使用它可以实现**“点对点”**的事件传输，代码会更加安全；
- 有EventBus（事件总线）的情况下，EventLine可以作为**事件单线**对项目的业务事件传递进行补充；

