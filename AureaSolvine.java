import greenfoot.*;

public class AureaSolvine extends Actor
{
    public int hp = 70;
    public int maxHp = 70;
    public int xp = 0;
    public int coin = 0;
    public int level = 1;
    public int speed = 4;
    public int stamina = 3;
    public int power = 3;
    public int xpToNextLevel = 10;

    public double worldX = 0;
    public double worldY = 0;
    public double moveX = 0;
    public double moveY = 0;

    static GreenfootImage[] idleFrames  = new GreenfootImage[8];
    static GreenfootImage[] runFrames   = new GreenfootImage[8];
    static GreenfootImage[] hitFrames   = new GreenfootImage[3];
    static GreenfootImage[] deathFrames = new GreenfootImage[7];

    static
    {
        for(int i = 0; i < 8; i++)
        {
            String num = String.format("%03d", i);
            idleFrames[i] = new GreenfootImage(
                "AureaSolvine/idle/tile" + num + ".png");
            runFrames[i]  = new GreenfootImage(
                "AureaSolvine/run/tile"  + num + ".png");
        }
        for(int i = 0; i < 3; i++)
        {
            String num = String.format("%03d", i);
            hitFrames[i] = new GreenfootImage(
                "AureaSolvine/hit/tile" + num + ".png");
        }
        for(int i = 0; i < 7; i++)
        {
            String num = String.format("%03d", i);
            deathFrames[i] = new GreenfootImage(
                "AureaSolvine/death/tile" + num + ".png");
        }
    }


    enum State { IDLE, RUN, HIT, DEATH }
    State state     = State.IDLE;
    State prevState = State.IDLE;

    int animFrame   = 0;
    int animTimer   = 0;
    static final int ANIM_SPEED = 4; 

    boolean facingLeft = false; 
    boolean isDead     = false;


    int hitTimer = 0;

    public AureaSolvine()
    {
        setImage(idleFrames[0]);
    }

    public void act()
    {
        if(isDead)
        {
            playDeathAnimation();
            return;
        }

        readInput();
        checkLevelUp();
        updateState();
        animate();
        checkDead();
    }

    public void readInput()
    {
        moveX = 0;
        moveY = 0;
        if(state == State.HIT) return; 

        if(Greenfoot.isKeyDown("w")) moveY = -speed;
        if(Greenfoot.isKeyDown("s")) moveY =  speed;
        if(Greenfoot.isKeyDown("a")) { moveX = -speed; facingLeft = true;  }
        if(Greenfoot.isKeyDown("d")) { moveX =  speed; facingLeft = false; }

        worldX += moveX;
        worldY += moveY;
    }

    public void takeHit(int damage)
    {
        if(isDead) return;
        hp -= damage;
        if(hp <= 0)
        {
            hp = 0;
            isDead = true;
            setState(State.DEATH);
        }
        else
        {
            setState(State.HIT);
            hitTimer = hitFrames.length * ANIM_SPEED; 
        }
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

    private void setState(State newState)
    {
        if(state != newState)
        {
            state     = newState;
            animFrame = 0;
            animTimer = 0;
        }
    }

    private void animate()
    {
        animTimer++;
        if(animTimer % ANIM_SPEED != 0) return;

        GreenfootImage[] frames = getCurrentFrames();

        animFrame++;
        if(animFrame >= frames.length)
        {
            animFrame = (state == State.HIT) ? frames.length - 1 : 0;
        }

        GreenfootImage img = new GreenfootImage(frames[animFrame]);

      
        if(facingLeft) img.mirrorHorizontally();

        setImage(img);
    }

    private void playDeathAnimation()
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

    private void checkDead()
    {
        if(hp <= 0 && !isDead)
        {
            isDead = true;
            setState(State.DEATH);
        }
    }

    public void gainXP(int amount)   { xp   += amount; }
    public void gainCoin(int amount) { coin += amount; }

    public void checkLevelUp()
    {
        if(xp >= xpToNextLevel)
        {
            xp -= xpToNextLevel;
            level++;
            xpToNextLevel = (int)(xpToNextLevel * 1.5);
            speed   += 1;
            stamina += 1;
            power   += 1;
            maxHp   += 10;
            hp       = maxHp;
    
            if(getWorld() instanceof GameWorld)
            {
                ((GameWorld)getWorld()).addAttributePoint();
            }
        }
    }

    public int getDamage()
    {
        return 10 + (power - 3) * 5;
    }
}