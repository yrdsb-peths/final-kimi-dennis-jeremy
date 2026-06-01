import greenfoot.*;

public class AureaSolvine extends Hero
{
    static GreenfootImage[] idleFrames  = new GreenfootImage[8];
    static GreenfootImage[] runFrames   = new GreenfootImage[8];
    static GreenfootImage[] hitFrames   = new GreenfootImage[3];
    static GreenfootImage[] deathFrames = new GreenfootImage[7];

    static
    {
        for(int i = 0; i < 8; i++)
        {
            String num = String.format("%03d", i);
            idleFrames[i] = new GreenfootImage("character/AureaSolvine/idle/tile" + num + ".png");
            runFrames[i]  = new GreenfootImage("character/AureaSolvine/run/tile"  + num + ".png");
        }
        for(int i = 0; i < 3; i++)
        {
            String num = String.format("%03d", i);
            hitFrames[i] = new GreenfootImage("character/AureaSolvine/hit/tile" + num + ".png");
        }
        for(int i = 0; i < 7; i++)
        {
            String num = String.format("%03d", i);
            deathFrames[i] = new GreenfootImage("character/AureaSolvine/death/tile" + num + ".png");
        }
    }

    public AureaSolvine()
    {
        super();
        setImage(idleFrames[0]);
    }

    @Override
    protected void updateAnimation()
    {
        updateState();
        animate();
    }

    private void updateState()
    {
        if(state == State.HIT)
        {
            hitTimer--;
            if(hitTimer <= 0) setState(State.IDLE);
            return;
        }
        boolean moving = (moveX != 0 || moveY != 0);
        setState(moving ? State.RUN : State.IDLE);
    }

    private void animate()
    {
        animTimer++;
        if(animTimer % ANIM_SPEED != 0) return;

        GreenfootImage[] frames = getCurrentFrames();
        animFrame++;
        if(animFrame >= frames.length)
            animFrame = (state == State.HIT) ? frames.length - 1 : 0;

        GreenfootImage img = new GreenfootImage(frames[animFrame]);
        if(facingLeft) img.mirrorHorizontally();
        setImage(img);
    }

    @Override
    protected void onDeathAnimation()
    {
        animTimer++;
        if(animTimer % ANIM_SPEED != 0) return;
        if(animFrame < deathFrames.length - 1) animFrame++;
        GreenfootImage img = new GreenfootImage(deathFrames[animFrame]);
        if(facingLeft) img.mirrorHorizontally();
        setImage(img);
    }

    private GreenfootImage[] getCurrentFrames()
    {
        switch(state)
        {
            case RUN:   return runFrames;
            case HIT:   return hitFrames;
            case DEATH: return deathFrames;
            default:    return idleFrames;
        }
    }
}