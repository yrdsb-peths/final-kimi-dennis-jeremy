import greenfoot.*;

public class LeonClovis extends Hero
{
    static GreenfootImage[] leonRightFrames = new GreenfootImage[8];
    static GreenfootImage[] leonLeftFrames  = new GreenfootImage[8];
    static GreenfootImage[] leonFrontFrames = new GreenfootImage[4];
    static GreenfootImage[] leonBackFrames  = new GreenfootImage[4];

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
            leonBackFrames[i]  = new GreenfootImage("character/Leon_move_back/leonBack" + i + ".png");
            leonBackFrames[i].scale(50, 50);
        }

        setImage(leonRight[0]);
    }

    boolean facingBack = false;
    SimpleTimer leonAnimTimer = new SimpleTimer();
    public int gunDamage = 10;

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
        if(state == State.HIT) return;

        if(Greenfoot.isKeyDown("w")) { moveY = -speed; facingBack = true;  facingLeft = false; }
        if(Greenfoot.isKeyDown("s")) { moveY =  speed; facingBack = false; facingLeft = false; }
        if(Greenfoot.isKeyDown("a")) { moveX = -speed; facingLeft = true;  }
        if(Greenfoot.isKeyDown("d")) { moveX =  speed; facingLeft = false; }

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
        boolean moving = (moveX != 0 || moveY != 0);

        if(!moving)
        {
            if(facingLeft)       setImage(leonLeftFrames[0]);
            else if(facingBack)  setImage(leonBackFrames[0]);
            else if(moveY > 0)   setImage(leonFrontFrames[0]);
            else                 setImage(leonRightFrames[0]);
            animFrame = 0;
            return;
        }

        if(leonAnimTimer.millisElapsed() < 100) return;
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
        // Leon has no death animation; keep last frame
    }
}
