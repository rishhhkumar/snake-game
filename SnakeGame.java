import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 20;

    Tile snakehead;
    ArrayList<Tile> snakebody;

    Tile food;
    Random random;

    Timer gameLoop;

    int velocityx;
    int velocityy;

    boolean gameover = false;


    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakehead = new Tile(5,5);
        snakebody = new ArrayList<Tile>();

        food = new Tile(15,15);
        random = new Random();
        placeFood();

        velocityx = 1;
        velocityy = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        // grid(x1,y1,x2,y2)
        // for(int i=0; i<boardWidth/tileSize; i++){
        //     g.setColor(Color.gray);    
        //     g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
        //     g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        // }

        // snake head
        g.setColor(Color.green);
        g.fill3DRect(snakehead.x * tileSize, snakehead.y * tileSize, tileSize, tileSize, true);

        // snake body
        for(int i=0; i<snakebody.size(); i++){
            Tile snakepart = snakebody.get(i);
            g.fill3DRect(snakepart.x * tileSize, snakepart.y * tileSize, tileSize, tileSize, true);
        }

        //food
        g.setColor(Color.yellow);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // score
        g.setFont(new Font("Montserrat", Font.PLAIN, 14));
        if(gameover){
            g.setColor(Color.magenta);
            g.drawString("Game Over: " + String.valueOf(snakebody.size()), tileSize-15, tileSize);
        }

        else{
            g.setColor(Color.cyan);
            g.drawString("Score: " + String.valueOf(snakebody.size()), tileSize-15, tileSize); 
        }

    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize); // 600/20 = 25
        food.y = random.nextInt(boardHeight/tileSize); // 600/20 = 25
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){

        if(collision(snakehead, food)){
            snakebody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for(int i=snakebody.size()-1; i>=0; i--){
            Tile snakepart = snakebody.get(i);
            if(i==0){
                snakepart.x=snakehead.x;
                snakepart.y=snakehead.y;
            }
            else{
                Tile prevsnakepart = snakebody.get(i-1);
                snakepart.x=prevsnakepart.x;
                snakepart.y=prevsnakepart.y;
            }
        }

        snakehead.x += velocityx;
        snakehead.y += velocityy;

        // gameOVER
        for(int i=0; i<snakebody.size(); i++){
            Tile snakepart = snakebody.get(i);
            if(collision(snakehead, snakepart)){
                gameover = true;
            }
        }

        if(snakehead.x*tileSize<0 || snakehead.x*tileSize>boardWidth || snakehead.y*tileSize<0 || snakehead.y*tileSize>boardHeight){
            gameover = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameover){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W && velocityy!=1){
            velocityx = 0;
            velocityy = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_S && velocityy!=-1){
            velocityx = 0;
            velocityy = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_A && velocityx!=1){
            velocityx = -1;
            velocityy = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_D && velocityx!=-1){
            velocityx = 1;
            velocityy = 0;
        }
    }

    // DO NOT NEED
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
