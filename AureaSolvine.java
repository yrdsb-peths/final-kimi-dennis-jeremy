import greenfoot.*;

public class AureaSolvine extends Hero
{
    public int skillDamage = 10;

    static GreenfootImage[] idleFrames = new GreenfootImage[8];
    static GreenfootImage[] runFrames = new GreenfootImage[8];
    static GreenfootImage[] hitFrames = new GreenfootImage[3];
    static GreenfootImage[] deathFrames = new GreenfootImage[7];

    static
    {
        for(int i = 0; i < 8; i++)
        {
            String num = String.format("%03d", i);
            idleFrames[i] = new GreenfootImage("character/AureaSolvine/idle/tile" + num + ".png");
            runFrames[i] = new GreenfootImage("character/AureaSolvine/run/tile" + num + ".png");
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
            return;
        }

        boolean moving = moveX != 0 || moveY != 0;
        setState(moving ? State.RUN : State.IDLE);
    }

    private void animate()
    {
        animTimer++;

        if(animTimer % ANIM_SPEED != 0)
        {
            return;
        }

        GreenfootImage[] frames = getCurrentFrames();
        animFrame++;

        if(animFrame >= frames.length)
        {
            animFrame = state == State.HIT ? frames.length - 1 : 0;
        }

        GreenfootImage image = new GreenfootImage(frames[animFrame]);

        if(facingLeft)
        {
            image.mirrorHorizontally();
        }

        setImage(image);
    }

    @Override
    protected void onDeathAnimation()
    {
        animTimer++;

        if(animTimer % ANIM_SPEED != 0)
        {
            return;
        }

        if(animFrame < deathFrames.length - 1)
        {
            animFrame++;
        }

        GreenfootImage image = new GreenfootImage(deathFrames[animFrame]);

        if(facingLeft)
        {
            image.mirrorHorizontally();
        }

        setImage(image);
    }

    private GreenfootImage[] getCurrentFrames()
    {
        switch(state)
        {
            case RUN:
                return runFrames;
            case HIT:
                return hitFrames;
            case DEATH:
                return deathFrames;
            default:
                return idleFrames;
        }
    }

    @Override
    public int getDamage()
    {
        return Math.max(skillDamage, super.getDamage());
    }

    public String getStartingLoadoutText()
    {
        return "Aurea starts with Fireball equipped.";
    }

    public boolean hasEquippedFireball()
    {
        return true;
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
