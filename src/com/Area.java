package com;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class    Area extends JPanel implements ActionListener {
    //параметры поля
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int BOARD_WIDTH = 300;
    private final int BOARD_HEIGHT = 300;
    //параметры змеи
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];
    //параметры объектов
    private int dots;
    private int apple_x;
    private int apple_y;
    private final int[] stone_x = new int[7];
    private final int[] stone_y = new int[7];
    //флаги для выбора направления
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    //Проверка идет ли игра
    private boolean gameProcess = true;
    //Счет
    private int score = 0;
    //ресурсы
    private Timer timer;
    private Image stone;
    private Image ball;
    private Image apple;
    private Image head;

    public Area() {
        
        initBoard();
    }
    
    private void initBoard() {
        //адаптер для управления стрелочками
        addKeyListener(new TAdapter());
        //установка фона
        setBackground(Color.BLACK);
        //чтобы кнопки работали берем в фокус
        setFocusable(true);
        //устанавливаем размер поля
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        //подключение картинок
        loadImages();
        //начало игры
        initGame();
    }
    //подгружаем картинки
    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/body.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();

        ImageIcon iis = new ImageIcon("src/resources/stone.png");
        stone = iis.getImage();
    }

    private void initGame() {
        //начальный размер змейки
        dots = 3;
        //базовая разметка змейки
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        //располагаем камни
        locateStone();
        //располагаем яблока
        locateApple();
        //скорость змейки, можно увеличить или уменьшить
        int DELAY = 140;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    //перегружаем этот метод, он сам всё отрисовывает
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    //отрисовка основных компонентов
    private void doDrawing(Graphics g) {
        
        if (gameProcess) {
            //отрисовываем камни и яблоко
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < stone_x.length; i++) {
                g.drawImage(stone, stone_x[i], stone_y[i], this);
            }
            //отрисовка змейки
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            //каменные границы
            for (int z = 0; z < BOARD_WIDTH; z += 10) {
                g.drawImage(stone, z, 0, this);
                g.drawImage(stone, z, BOARD_HEIGHT - 10, this);
                g.drawImage(stone, 0, z, this);
                g.drawImage(stone, BOARD_WIDTH - 10, z, this);
            }
            //хз что это, но с ней работает, крч для синхронизации графики
            Toolkit.getDefaultToolkit().sync();
        //если gameProcess == false, то игра заканчивается
        } else {

            gameOver(g);
        }        
    }
    //метод для отрисовки окошка на случай проигрыша
    private void gameOver(Graphics g) {
        
        String text = "Вы проиграли!\n Ваш счет: " + score;
        Font window = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(window);

        g.setColor(Color.GRAY);
        g.setFont(window);
        g.drawString(text, (BOARD_WIDTH - metr.stringWidth(text)) / 2, BOARD_HEIGHT / 2);
    }
    //проверяет не на яблоке ли наша голова
    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++;
            locateApple();
        }
    }
    //проверяет не на камне ли наша голова
    private void checkStone() {
        for (int i = 0; i < stone_x.length; i++) {
            if ((x[0] == stone_x[i]) && (y[0] == stone_y[i])) {
                gameProcess = false;
                timer.stop();
            }
        }
    }
    //метод для движения
    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (left) {
            x[0] -= DOT_SIZE;
        }

        if (right) {
            x[0] += DOT_SIZE;
        }

        if (up) {
            y[0] -= DOT_SIZE;
        }

        if (down) {
            y[0] += DOT_SIZE;
        }
    }
    //метод для проверки не упираемся ли в хвост или границу
    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 3) && (x[0] == x[z]) && (y[0] == y[z])) {
                gameProcess = false;
                break;
            }
        }

        if (y[0] >= BOARD_HEIGHT - 10) {
            gameProcess = false;
        }

        if (y[0] < 10) {
            gameProcess = false;
        }

        if (x[0] >= BOARD_WIDTH - 10) {
            gameProcess = false;
        }

        if (x[0] < 10) {
            gameProcess = false;
        }
        
        if (!gameProcess) {
            timer.stop();
        }
    }
    //располагает яблоко
    private void locateApple() {

        int RAND_POS = 29;
        boolean flag = false;
        while (true) {
            apple_x = (((int) (Math.random() * RAND_POS) * DOT_SIZE));
            apple_y = (((int) (Math.random() * RAND_POS) * DOT_SIZE));
            //проверка, чтобы ягоды не спавнились в границах
            if (apple_x <= 20 || apple_x >= BOARD_WIDTH - 10 || apple_y <= 20 || apple_y >=BOARD_HEIGHT - 10) continue;
            for (int i = 0; i < stone_x.length; i++) {
                if (apple_x == stone_x[i] && apple_y == stone_y[i] ) {
                    flag = true;
                    break;
                }
            }
            if (!flag) break;
        }
        locateStone();
    }
    //расположение камней
    private void locateStone() {

        int RAND_POS = 29;
        for (int i = 0; i < stone_x.length; i++) {
            while (true) {
                stone_x[i] = (((int) (Math.random() * RAND_POS) * DOT_SIZE));
                stone_y[i] = (((int) (Math.random() * RAND_POS) * DOT_SIZE));
                if (!(stone_x[i] == x[0] && stone_y[i] == y[0])) break;
            }
        }
    }
    //метод выполняет команды на каждый шаг игры
    //сначала проверка не на камне ли голова, потом не на яблоке ли голова, потом смотри не было ли врезаний в границу или себя, потом движение
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameProcess) {
            checkStone();
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }
    //наследуется от KeyAdapter, задает клавиши
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!right)) {
                left = true;
                up = false;
                down = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!left)) {
                right = true;
                up = false;
                down = false;
            }

            if ((key == KeyEvent.VK_UP) && (!down)) {
                up = true;
                right = false;
                left = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!up)) {
                down = true;
                right = false;
                left = false;
            }
        }
    }
}
