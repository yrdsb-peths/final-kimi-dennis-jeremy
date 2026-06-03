import greenfoot.*;

public class LeonClovis extends Hero
{
    public int gunDamage = 10;

    static GreenfootImage[] leonRightFrames = new GreenfootImage[8];
    static GreenfootImage[] leonLeftFrames = new GreenfootImage[8];
    static GreenfootImage[] leonFrontFrames = new GreenfootImage[4];
    static GreenfootImage[] leonBackFrames = new GreenfootImage[4];

    boolean facingBack = false;
    SimpleTimer leonAnimTimer = new SimpleTimer();

    static
    {
        for(int i = 0; i < 8; i++)
        {
            leonRightFrames[i] = new GreenfootImage("character/Leon_move/leon" + i + ".png");
            leonRightFrames[i].scale(50, 50);
            leonLeftFrames[i] = new GreenfootImage("character/Leon_move/leon" + i + ".png");
            leonLeftFrames[i].scale(50, 50);
            leonLeftFrames[i].mirrorHorizontally();
        }

        for(int i = 0; i < 4; i++)
        {
            leonFrontFrames[i] = new GreenfootImage("character/Leon_move_front/leon" + i + ".png");
            leonFrontFrames[i].scale(50, 50);
            leonBackFrames[i] = new GreenfootImage("character/Leon_move_back/leonBack" + i + ".png");
            leonBackFrames[i].scale(50, 50);
        }
    }

    public LeonClovis()
    {
        super();
        setImage(leonRightFrames[0]);
    }

    @Override
    public void readInput()
    {
        moveX = 0;
        moveY = 0;

        if(state == State.HIT)
        {
            return;
        }

        if(Greenfoot.isKeyDown("w"))
        {
            moveY = -speed;
            facingBack = true;
            facingLeft = false;
        }

        if(Greenfoot.isKeyDown("s"))
        {
            moveY = speed;
            facingBack = false;
            facingLeft = false;
        }

        if(Greenfoot.isKeyDown("a"))
        {
            moveX = -speed;
            facingLeft = true;
        }

        if(Greenfoot.isKeyDown("d"))
        {
            moveX = speed;
            facingLeft = false;
        }

        worldX += moveX;
        worldY += moveY;
    }

    @Override
    protected void updateAnimation()
    {
        animateLeon();
    }

    private void animateLeon()
    {
        boolean moving = moveX != 0 || moveY != 0;

        if(!moving)
        {
            if(facingLeft)
            {
                setImage(leonLeftFrames[0]);
            }
            else if(facingBack)
            {
                setImage(leonBackFrames[0]);
            }
            else if(moveY > 0)
            {
                setImage(leonFrontFrames[0]);
            }
            else
            {
                setImage(leonRightFrames[0]);
            }

            animFrame = 0;
            return;
        }

        if(leonAnimTimer.millisElapsed() < 100)
        {
            return;
        }

        leonAnimTimer.mark();

        if(facingLeft)
        {
            setImage(leonLeftFrames[animFrame % leonLeftFrames.length]);
        }
        else if(moveY < 0)
        {
            setImage(leonBackFrames[animFrame % leonBackFrames.length]);
        }
        else if(moveY > 0)
        {
            setImage(leonFrontFrames[animFrame % leonFrontFrames.length]);
        }
        else
        {
            setImage(leonRightFrames[animFrame % leonRightFrames.length]);
        }

        animFrame++;
    }

    @Override
    protected void onDeathAnimation()
    {
    }

    public void takeDamage(int damage)
    {
        takeHit(damage);
    }

    public void displayStats()
    {
        World world = getWorld();

        if(world == null)
        {
            return;
        }

        GreenfootImage background = world.getBackground();
        int hpBarWidth = Math.max(0, Math.min(300, hp * 300 / maxHp));
        int xpBarWidth = Math.max(0, Math.min(300, xp * 300 / xpToNextLevel));

        background.setColor(Color.WHITE);
        background.fillRect(10, 10, 300, 25);
        background.setColor(Color.RED);
        background.fillRect(10, 10, hpBarWidth, 25);

        background.setColor(Color.WHITE);
        background.fillRect(10, 45, 300, 25);
        background.setColor(Color.BLUE);
        background.fillRect(10, 45, xpBarWidth, 25);

        background.setColor(Color.BLACK);
        background.drawRect(10, 10, 300, 25);
        background.drawRect(10, 45, 300, 25);

        world.showText(hp + " / " + maxHp + " HP", 390, 23);
        world.showText("LV " + level + "   " + xp + " / " + xpToNextLevel + " XP", 410, 58);
        world.showText("Coin: " + coin, 90, 85);
    }
}
