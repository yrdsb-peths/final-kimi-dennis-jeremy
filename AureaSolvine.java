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
    public double moveX  = 0;
    public double moveY  = 0;

    String heroType;

    // Aurea 动画帧
    static GreenfootImage[] idleFrames  = new GreenfootImage[8];
    static GreenfootImage[] runFrames   = new GreenfootImage[8];
    static GreenfootImage[] hitFrames   = new GreenfootImage[3];
    static GreenfootImage[] deathFrames = new GreenfootImage[7];

    // Leon 动画帧
    static GreenfootImage[] leonRightFrames = new GreenfootImage[8];
    static GreenfootImage[] leonLeftFrames  = new GreenfootImage[8];
    static GreenfootImage[] leonFrontFrames = new GreenfootImage[4];
    static GreenfootImage[] leonBackFrames  = new GreenfootImage[4];

    // Kaine 用默认图片
    static GreenfootImage kaineImage;

    static
    {
        // Aurea
        for(int i = 0; i < 8; i++)
        {
            String num = String.format("%03d", i);
            idleFrames[i] = new GreenfootImage("AureaSolvine/idle/tile" + num + ".png");
            runFrames[i]  = new GreenfootImage("AureaSolvine/run/tile"  + num + ".png");
        }
        for(int i = 0; i < 3; i++)
        {
            String num = String.format("%03d", i);
            hitFrames[i] = new GreenfootImage("AureaSolvine/hit/tile" + num + ".png");
        }
        for(int i = 0; i < 7; i++)
        {
            String num = String.format("%03d", i);
            deathFrames[i] = new GreenfootImage("AureaSolvine/death/tile" + num + ".png");
        }

        // Leon
        for(int i = 0; i < 8; i++)
        {
            leonRightFrames[i] = new GreenfootImage("images/leon_move/leon" + i + ".png");
            leonRightFrames[i].scale(50, 50);
            leonLeftFrames[i] = new GreenfootImage("images/leon_move/leon" + i + ".png");
            leonLeftFrames[i].scale(50, 50);
            leonLeftFrames[i].mirrorHorizontally();
        }
        for(int i = 0; i < 4; i++)
        {
            leonFrontFrames[i] = new GreenfootImage("images/leon_move_front/leon" + i + ".png");
            leonFrontFrames[i].scale(50, 50);
            leonBackFrames[i]  = new GreenfootImage("images/leon_move_back/leonBack" + i + ".png");
            leonBackFrames[i].scale(50, 50);
        }

        // Kaine — 用自带图
        kaineImage = new GreenfootImage("KaineVelsarth.png");
        if(kaineImage.getWidth() > 0)
            kaineImage.scale(72, 72);
    }

    enum State { IDLE, RUN, HIT, DEATH }
    State state = State.IDLE;

    int animFrame = 0;
    int animTimer = 0;
    static final int ANIM_SPEED = 4;

    boolean facingLeft  = false;
    boolean facingBack  = false;  // Leon 用
    public boolean isDead = false;
    int hitTimer = 0;

    // Leon 动画计时（用毫秒）
    SimpleTimer leonAnimTimer = new SimpleTimer();

    public AureaSolvine(String heroType)
    {
        this.heroType = heroType;
        if(heroType.equals("kaine") && kaineImage.getWidth() > 0)
            setImage(kaineImage);
        else if(heroType.equals("leon"))
            setImage(leonRightFrames[0]);
        else
            setImage(idleFrames[0]);
    }

    public AureaSolvine()
    {
        this("aurea");
    }

    public void act()
    {
        if(isDead)
        {
            if(!heroType.equals("leon") && !heroType.equals("kaine"))
                playDeathAnimation();
            return;
        }

        readInput();
        checkLevelUp();

        if(heroType.equals("leon"))
            animateLeon();
        else if(heroType.equals("kaine"))
            animateKaine();
        else
        {
            updateState();
            animate();
            checkDead();
        }
    }

    public void readInput()
    {
        moveX = 0;
        moveY = 0;
        if(state == State.HIT && !heroType.equals("leon")) return;

        if(Greenfoot.isKeyDown("w")) { moveY = -speed; facingBack = true;  facingLeft = false; }
        if(Greenfoot.isKeyDown("s")) { moveY =  speed; facingBack = false; facingLeft = false; }
        if(Greenfoot.isKeyDown("a")) { moveX = -speed; facingLeft = true;  }
        if(Greenfoot.isKeyDown("d")) { moveX =  speed; facingLeft = false; }

        worldX += moveX;
        worldY += moveY;
    }

    // ── Leon 动画 ──────────────────────────────────────
    private void animateLeon()
    {
        boolean moving = (moveX != 0 || moveY != 0);

        if(!moving)
        {
            // 停下时显示第0帧
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
        else if(moveY < 0) // 向上
        {
            setImage(leonBackFrames[animFrame % leonBackFrames.length]);
        }
        else if(moveY > 0) // 向下
        {
            setImage(leonFrontFrames[animFrame % leonFrontFrames.length]);
        }
        else
        {
            setImage(leonRightFrames[animFrame % leonRightFrames.length]);
        }
        animFrame++;
    }

    // ── Kaine 动画 ─────────────────────────────────────
    private void animateKaine()
    {
        // Kaine 用静态图片，朝向通过镜像处理
        GreenfootImage img = new GreenfootImage(kaineImage);
        if(facingLeft) img.mirrorHorizontally();
        setImage(img);
    }

    // ── Aurea 动画 ─────────────────────────────────────
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
            animFrame = (state == State.HIT) ? frames.length - 1 : 0;

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
                ((GameWorld)getWorld()).addAttributePoint();
        }
    }

    public int getDamage()
    {
        return 10 + (power - 3) * 5;
    }
}