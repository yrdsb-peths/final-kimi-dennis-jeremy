import greenfoot.*;

public class IceWave extends Weapon
{
    static GreenfootImage[] frames = new GreenfootImage[28];

    static
    {
        for(int i = 0; i < 28; i++)
        {
            frames[i] = new GreenfootImage("IceWave/IceWave" + i + ".png");
            int w = frames[i].getWidth();
            int h = frames[i].getHeight();
            if(w > 0 && h > 0)
                frames[i].scale(w * 3, h * 3);
        }
    }

    int frame = 0;
    int animationTimer = 0;

    static final int RADIUS   = 160;
    static final int KNOCKBACK = 60;
    static final String SOUND_ICEWAVE = "icewave.mp3";

    boolean soundPlayedThisCycle = false;
    int wavesThisCycle = 1;
    int wavesTriggered = 0;

    public IceWave()
    {
        super();
        setImage(frames[0]);
    }

    @Override
    public void act()
    {
        if(getWorld() == null) return;

        GameWorld gw = (GameWorld)getWorld();
        worldX = gw.player.worldX;
        worldY = gw.player.worldY;
        damage = gw.player.getDamage() + (gw.iceWaveLevel - 1) * 5;
        wavesThisCycle = GameWorld.weaponTriggerCount(gw.iceWaveLevel);

        animate();

        if(frame == 10 && wavesTriggered < wavesThisCycle)
        {
            if(!soundPlayedThisCycle)
            {
                Greenfoot.playSound(SOUND_ICEWAVE);
                soundPlayedThisCycle = true;
            }

            triggerIceWaveHit();
            wavesTriggered++;
        }
    }

    private void animate()
    {
        animationTimer++;
        if(animationTimer % 3 == 0)
        {
            frame++;
            if(frame >= frames.length)
            {
                frame = 0;
                soundPlayedThisCycle = false;
                wavesTriggered = 0;
            }
            setImage(frames[frame]);
        }
    }

    private void triggerIceWaveHit()
    {
        if(getWorld() == null)
        {
            return;
        }

        for(Enemy e : getWorld().getObjects(Enemy.class))
        {
            double dx = e.worldX - worldX;
            double dy = e.worldY - worldY;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if(dist < RADIUS)
            {
                onHitEnemy(e, dx, dy, dist);
            }
        }
    }

    @Override
    protected void onHitEnemy(Enemy e, double dx, double dy, double dist)
    {
        if(dist == 0)
        {
            return;
        }

        boolean died = e.takeDamage(damage);

        if(!died)
        {
            e.worldX += dx / dist * KNOCKBACK;
            e.worldY += dy / dist * KNOCKBACK;
        }
    }
}