import greenfoot.*;

public class LeonClovis extends Actor
{
    public int hp = 70;
    public int maxHp = 70;
    public int xp = 0;
    public int coin = 0;
    public int level = 1;
    public int xpToNextLevel = 10;
    int speed = 4;
    public int gunDamage = 10;

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
            leonRight[i].scale(50, 50);
        }

        for(int i = 0; i < leonLeft.length; i++)
        {
            leonLeft[i] = new GreenfootImage("images/leon_move/leon" + i + ".png");
            leonLeft[i].mirrorHorizontally();
            leonLeft[i].scale(50, 50);
        }

        for(int i = 0; i < leonFront.length; i++)
        {
            leonFront[i] = new GreenfootImage("images/leon_move_front/leon" + i + ".png");
            leonFront[i].scale(50, 50);
        }

        for(int i = 0; i < leonBack.length; i++)
        {
            leonBack[i] = new GreenfootImage("images/leon_move_back/leonBack" + i + ".png");
            leonBack[i].scale(50, 50);
        }

        setImage(leonRight[0]);
    }

    public void act()
    {
        movePlayer();
        animateLeon();
        autoShoot();
        checkLevelUp();
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
        xp += 3;
        coin += 2;
    }

    public void takeDamage(int damage)
    {
        hp -= damage;

        if(hp <= 0)
        {
            hp = 0;
            showGameOver();
            Greenfoot.stop();
        }
    }

    private void showGameOver()
    {
        World world = getWorld();

        if(world != null)
        {
            world.addObject(new GameOver(), world.getWidth() / 2, world.getHeight() / 2);
        }
    }

    public void checkLevelUp()
    {
        if(xp >= xpToNextLevel)
        {
            level++;
            xp = 0;
            gunDamage += 5;
            maxHp += 10;
            hp = maxHp;
        }
    }

    public void displayStats()
    {
        GreenfootImage background = getWorld().getBackground();
        background.setColor(Color.BLACK);
        background.fillRect(0, 0, 500, 100);

        background.setColor(Color.WHITE);
        background.fillRect(10, 10, 300, 25);

        background.setColor(Color.RED);
        background.fillRect(10, 10, hp * 300 / maxHp, 25);

        background.setColor(Color.WHITE);
        background.fillRect(10, 45, 300, 25);

        background.setColor(Color.BLUE);
        background.fillRect(10, 45, xp * 300 / xpToNextLevel, 25);

        getWorld().showText(hp + " HP", 370, 23);
        getWorld().showText("LV " + level + "   " + xp + "/10 XP", 390, 58);
        getWorld().showText("Coin: " + coin, 80, 85);
    }

    public void movePlayer()
    {
        boolean moving = false;

        if(Greenfoot.isKeyDown("w"))
        {
            setLocation(getX(), getY() - speed);
            facing = "back";
            moving = true;
        }

        if(Greenfoot.isKeyDown("s"))
        {
            setLocation(getX(), getY() + speed);
            facing = "front";
            moving = true;
        }

        if(Greenfoot.isKeyDown("a"))
        {
            setLocation(getX() - speed, getY());
            facing = "left";
            moving = true;
        }

        if(Greenfoot.isKeyDown("d"))
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
            imageIndex = imageIndex % leonFront.length;
            setImage(leonFront[imageIndex]);
            imageIndex++;
        }
        else if(facing.equals("back"))
        {
            imageIndex = imageIndex % leonBack.length;
            setImage(leonBack[imageIndex]);
            imageIndex++;
        }
    }
}
