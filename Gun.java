import greenfoot.*;

public class Gun extends Weapon
{
    static GreenfootImage bullet;

    static
    {
        bullet = new GreenfootImage(18, 6);
        bullet.setColor(new Color(220, 220, 80));
        bullet.fillOval(0, 0, 18, 6);
        bullet.setColor(new Color(80, 80, 40));
        bullet.drawOval(0, 0, 17, 5);
    }

    double speed = 10;
    double velX, velY;

    public Gun(double startX, double startY,
               double targetX, double targetY, int damage)
    {
        super();
        this.worldX = startX;
        this.worldY = startY;
        this.damage = damage;

        double dx = targetX - startX;
        double dy = targetY - startY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if(dist == 0) dist = 1;
        velX = dx / dist * speed;
        velY = dy / dist * speed;

        setImage(bullet);
        setRotation((int)Math.toDegrees(Math.atan2(dy, dx)));
    }

    @Override
    public void act()
    {
        if(getWorld() == null) return;

        worldX += velX;
        worldY += velY;

        checkHitEnemy(worldX, worldY, 20);
        checkRange();
    }

    @Override
    protected void onHitEnemy(Enemy e, double dx, double dy, double dist)
    {
        GameWorld gw = (GameWorld)getWorld();
        boolean died = e.takeDamage(damage);
        if(died)
        {
            gw.player.gainXP(e.xpDrop);
            gw.player.gainCoin(e.coinDrop);
            if(e.getWorld() != null) gw.removeObject(e);
        }
        if(getWorld() != null) gw.removeObject(this);
    }

    private void checkRange()
    {
        if(getWorld() == null) return;
        GameWorld gw = (GameWorld)getWorld();
        double dx = worldX - gw.player.worldX;
        double dy = worldY - gw.player.worldY;
        if(Math.sqrt(dx * dx + dy * dy) > 1000)
            gw.removeObject(this);
    }
}
