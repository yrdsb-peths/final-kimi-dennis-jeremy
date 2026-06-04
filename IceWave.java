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

    // 用于标记这一帧是否已经触发过伤害（防止重复）
    boolean hasDealtDamageThisFrame = false;

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

        animate();
        
        // 只在第10帧时触发伤害
        if(frame == 10 && !hasDealtDamageThisFrame)
        {
            hasDealtDamageThisFrame = true;
            hitAllEnemiesInRange();
        }
        
        // 动画循环时重置标志
        if(frame == 0)
        {
            hasDealtDamageThisFrame = false;
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
            }
            setImage(frames[frame]);
        }
    }

    // 击退范围内的所有敌人
    private void hitAllEnemiesInRange()
    {
        GameWorld gw = (GameWorld)getWorld();
        
        // 获取所有敌人
        java.util.List<Enemy> enemies = gw.getObjects(Enemy.class);
        java.util.List<Enemy> toRemove = new java.util.ArrayList<>();

        for(Enemy e : enemies)
        {
            double dx = e.worldX - worldX;
            double dy = e.worldY - worldY;
            double dist = Math.sqrt(dx * dx + dy * dy);

            // 检查敌人是否在范围内
            if(dist < RADIUS && dist > 0)
            {
                // 扣伤害
                boolean died = e.takeDamage(damage);
                
                if(died)
                {
                    // 敌人死亡，记下来稍后删除
                    gw.player.gainXP(e.xpDrop);
                    gw.player.gainCoin(e.coinDrop);
                    toRemove.add(e);
                }
                else
                {
                    // 敌人存活，进行击退
                    e.worldX += dx / dist * KNOCKBACK;
                    e.worldY += dy / dist * KNOCKBACK;
                }
            }
        }

        // 删除所有死亡的敌人
        for(Enemy e : toRemove)
        {
            if(e.getWorld() != null)
            {
                gw.removeObject(e);
            }
        }
    }

    @Override
    protected void onHitEnemy(Enemy e, double dx, double dy, double dist)
    {
        // 这个方法不再使用，改由 hitAllEnemiesInRange() 处理
    }
}