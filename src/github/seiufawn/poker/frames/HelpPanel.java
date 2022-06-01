package github.seiufawn.poker.frames;

import github.seiufawn.poker.Frame;

import javax.swing.*;

public class HelpPanel extends PanelBase {

    public HelpPanel() {
        super("游戏规则");
        setLayout(null);

        JButton b3 = new JButton("返回首页");

        b3.setBounds(340, 400, 200, 50);
        b3.setFont(Frame.BLOD_20);

        add(b3);

        addLabel("规则", 435, 50, 500, 20);
        addLabel("1.角色牌说明：10（唐三藏） 8（八戒） 5（悟空） 3（沙悟净） ", 20, 80, 500, 20);
        addLabel("2.万能牌:    2和A ", 20, 110, 800, 20);
        addLabel("2（顺过牌 跳过自己，顺延给下家去克制我的上家出的牌）", 60, 140, 800, 20);
        addLabel("A（反向顶回牌 就是跳过自己，让上家自己去克制自己刚刚出的牌）", 60, 170, 800, 20);
        addLabel("3.角色排序:", 20, 200, 800, 20);
        addLabel("如来（大鬼）> 观音（小鬼）> 唐三藏（10）> 悟空（5）> 八戒（8）> 沙僧（3）", 60, 230, 800, 20);
        addLabel("> K > Q > J > 9 > 7 > 6 > 4 > 唐三藏（10）", 60, 270, 800, 20);
        addLabel("备注：任何普通妖怪牌都是可以吃唐僧的哦！三个徒弟不能打师傅。", 20, 300, 800, 20);

        b3.addActionListener(e -> {
            close();
            new HomePanel();
        });
    }

}
