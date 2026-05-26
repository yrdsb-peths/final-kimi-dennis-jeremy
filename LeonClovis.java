import greenfoot.*;

public class LeonClovis extends Actor
{
    public int hp = 70;
    public int xp = 0;
    public int coin = 0;
    public int level = 1;
    int speed = 4;
    int gunDamage = 10;

    GreenfootImage[] leonRight = new GreenfootImage[8];
    GreenfootImage[] leonLeft = new GreenfootImage[8];
    GreenfootImage[] leonFront = new GreenfootImage[4];
    GreenfootImage[] leonBack = new GreenfootImage[4];

    String facing = "right";

    SimpleTimer animationTimer = new SimpleTimer();
    SimpleTimer shootTimer = new SimpleTimer();

    int imageIndex = 0;

    public LeonClovis()
    {
        for(int i = 0; i < leonRight.length; i++)
        {
            leonRight[i] = new GreenfootImage("images/leon_move/leon" + i + ".png");
            leonRight[i].scale(50,50);
        }

        for(int i = 0; i < leonLeft.length; i++)
        {
            leonLeft[i] = new GreenfootImage("images/leon_move/leon" + i + ".png");
            leonLeft[i].mirrorHorizontally();
            leonLeft[i].scale(50,50);
        }

        for(int i = 0; i < leonFront.length; i++)
        {
            leonFront[i] = new GreenfootImage("images/leon_move_front/leon" + i + ".png");
            leonFront[i].scale(50,50);
        }

        for(int i = 0; i < leonBack.length; i++)
        {
            leonBack[i] = new GreenfootImage("images/leon_move_back/leonBack" + i + ".png");
            leonBack[i].scale(50,50);
        }

        setImage(leonRight[0]);
    }

    public void act()
    {
        movePlayer();
        animateLeon();
        autoShoot();
        displayStats();
    }

    public void autoShoot()
    {
        if(shootTimer.millisElapsed() > 500)
        {
            java.util.List<Enemy> enemies = getWorld().getObjects(Enemy.class);

            if(enemies.size() > 0)
            {
                Enemy target = enemies.get(0);
                Bullet bullet = new Bullet(target, this);
                getWorld().addObject(bullet, getX(), getY());
                shootTimer.mark();
            }
        }
    }

    public void addReward()
    {
        {
            xp += 3;
            coin += 2;
        
            if(xp >= 10)
            {
                level++;
                xp = 0;
                gunDamage+=5;
            }
        }
    }

    public void takeDamage(int damage)
    {
        hp -= damage;

        if(hp <= 0)
        {
            hp = 0;
            Greenfoot.stop();
        }
    }

    public void displayStats()
    {
        GreenfootImage bg = getWorld().getBackground();
    
        // clear UI area
        bg.setColor(Color.BLACK);
        bg.fillRect(0, 0, 380, 90);
    
        // HP BAR BACKGROUND
        bg.setColor(Color.WHITE);
        bg.fillRect(10, 10, 200, 20);
    
        // HP BAR
        bg.setColor(Color.RED);
        bg.fillRect(10, 10, hp * 200 / 70, 20);
    
        // XP BAR BACKGROUND
        bg.setColor(Color.WHITE);
        bg.fillRect(10, 40, 200, 20);
    
        // XP BAR
        bg.setColor(Color.BLUE);
        bg.fillRect(10, 40, xp * 20, 20);
    
        // TEXT
        getWorld().showText(hp + " HP", 250, 20);
    
        getWorld().showText("LV " + level + "  " + xp + "/10 XP", 290, 50);
    
        getWorld().showText("Coin: " + coin, 80, 75);
    }

    public void movePlayer()
    {
        boolean moving = false;

        if (Greenfoot.isKeyDown("w"))
        {
            setLocation(getX(), getY() - speed);
            facing = "back";
            moving = true;
        }

        if (Greenfoot.isKeyDown("s"))
        {
            setLocation(getX(), getY() + speed);
            facing = "front";
            moving = true;
        }

        if (Greenfoot.isKeyDown("a"))
        {
            setLocation(getX() - speed, getY());
            facing = "left";
            moving = true;
        }

        if (Greenfoot.isKeyDown("d"))
        {
            setLocation(getX() + speed, getY());
            facing = "right";
            moving = true;
        }

        if(!moving)
        {
            imageIndex = 0;

            if(facing.equals("right"))
            {
                setImage(leonRight[0]);
            }
            else if(facing.equals("left"))
            {
                setImage(leonLeft[0]);
            }
            else if(facing.equals("front"))
            {
                setImage(leonFront[0]);
            }
            else if(facing.equals("back"))
            {
                setImage(leonBack[0]);
            }
        }
    }

    public void animateLeon()
    {
        if(animationTimer.millisElapsed() < 100)
        {
            return;
        }

        animationTimer.mark();

        if(facing.equals("right"))
        {
            setImage(leonRight[imageIndex]);
            imageIndex = (imageIndex + 1) % leonRight.length;
        }
        else if(facing.equals("left"))
        {
            setImage(leonLeft[imageIndex]);
            imageIndex = (imageIndex + 1) % leonLeft.length;
        }
        else if(facing.equals("front"))
        {
            imageIndex %= leonFront.length;
            setImage(leonFront[imageIndex]);
            imageIndex++;
        }
        else if(facing.equals("back"))
        {
            imageIndex %= leonBack.length;
            setImage(leonBack[imageIndex]);
            imageIndex++;
        }
    }
}