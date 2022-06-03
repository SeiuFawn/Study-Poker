package github.seiufawn.poker.frames;

import github.seiufawn.poker.Frame;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

// 面板基类
public abstract class PanelBase extends JPanel {

    // 计时器
    static Map<String, Timer> timerMap = new HashMap<>();

    String title;

    // 实例化界面并且自动添加到窗口中, 并且设置大小
    public PanelBase(String title, int wight, int height) {
        this(title);
        Frame.inst.setSize(wight, height);
        Frame.currentPanel = this;
    }

    // 实例化界面并且自动添加到窗口中
    public PanelBase(String title) {
        this.title = title;
        Frame.inst.setTitle(title);
        Frame.inst.add(this);
    }

    // 添加文字类
    public JLabel addLabel(String text, int x, int y, int width, int height) {
        JLabel newLabel = new JLabel(text);
        newLabel.setFont(Frame.PLAIN_15);
        newLabel.setBounds(x, y, width, height);
        add(newLabel);
        return newLabel;
    }

    // 创建定时任务
    protected void setDelayAction(String key, int delay, Runnable action) {
        // 用类名区分各个UI面板的定时任务
        String finalKey = this.getClass().getSimpleName() + "." + key;
        // 如果已存在 则杀死
        if (timerMap.containsKey(finalKey)) timerMap.get(finalKey).stop();
        Timer timer = new Timer(delay, e -> {
            action.run();
            timerMap.remove(finalKey).stop();
        });
        timerMap.put(finalKey, timer);
        timer.start();
    }

    //关闭界面
    public void close() {
        setVisible(false);
    }
}
