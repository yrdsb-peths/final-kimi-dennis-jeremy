import greenfoot.*;

public class IceWave extends Actor
{
    static GreenfootImage[] frames = new GreenfootImage[28];

    static
    {
        for(int i = 0; i < 28; i++)
        {
            frames[i] = new GreenfootImage("IceWave/IceWave" + i + ".png");
            frames[i].scale(
                frames[i].getWidth() * 3,
                frames[i].getHeight() * 3
            );
        }
    }

    int frame = 0;
    int animationTimer = 0;

    
    public double worldX;
    public double worldY;

    static final int RADIUS   = 160;  
    static final int DAMAGE   = 8;   
    static final int KNOCKBACK = 60; 

    
    boolean damageDealtThisCycle = false;

    public IceWave()
    {
        setImage(frames[0]);
    }

    public void act()
    {
        if(getWorld() == null) return;

        
        GameWorld gw = (GameWorld)getWorld();
        worldX = gw.aureaSolvine.worldX;
        worldY = gw.aureaSolvine.worldY;

        animate();
        hitEnemies();
    }

    public void animate()
    {
        animationTimer++;
        if(animationTimer % 3 == 0)
        {
            frame++;
            if(frame >= frames.length)
            {
                frame = 0;
                damageDealtThisCycle = false; 
            }
            setImage(frames[frame]);
        }
    }

    public void hitEnemies()
    {
     
        if(frame != 10) return;
        if(damageDealtThisCycle) return;
        damageDealtThisCycle = true;

        GameWorld gw = (GameWorld)getWorld();

        for(Enemy e : gw.getObjects(Enemy.class))
        {
            double dx = e.worldX - worldX;
            double dy = e.worldY - worldY;
            double dist = Math.sqrt(dx*dx + dy*dy);

            if(dist < RADIUS && dist > 0)
            {
                boolean died = e.takeDamage(DAMAGE);
                if(died)
                {
                    gw.aureaSolvine.gainXP(e.xpDrop);
                    gw.aureaSolvine.gainCoin(e.coinDrop);
                    if(e.getWorld() != null) gw.removeObject(e);
                }
                else
                {
                    e.worldX += dx / dist * KNOCKBACK;
                    e.worldY += dy / dist * KNOCKBACK;
                }
            }
        }
    }
}
