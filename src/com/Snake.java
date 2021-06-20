package com;

import javax.swing.JFrame;

public class Snake extends JFrame {
    public static void main(String[] args) {
        //создаем змейку, которая и несет в себе всю логику (смотрим конструктор змейки)
        JFrame ex = new Snake();
        ex.setVisible(true);
    }

    public Snake() {
        //попадаем сюда из мейна, вызываем метод выше
        add(new Area());

        setResizable(false);
        pack();

        setTitle("Snake-game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
