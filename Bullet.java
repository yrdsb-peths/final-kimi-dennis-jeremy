import greenfoot.*;

public class Bullet extends Actor
{
    int damage;
    int speed = 10;

    public double worldX;
    public double worldY;

    double dx;
    double dy;

    public Bullet(double startX, double startY, double targetX, double targetY, int damage)
    {
        worldX = startX;
        worldY = startY;
        this.damage = damage;

        GreenfootImage img = new GreenfootImage(12, 12);
        img.setColor(Color.YELLOW);
        img.fillOval(0, 0, 12, 12);
        setImage(img);

        dx = targetX - startX;
        dy = targetY - startY;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if(dist != 0)
        {
            dx = dx / dist;
            dy = dy / dist;
        }
    }

    public void act()
    {
        moveBullet();
        hitEnemy();
    }

    public void moveBullet()
    {
        worldX += dx * speed;
        worldY += dy * speed;

        GameWorld gw = (GameWorld)getWorld();

        int sx = (int)(gw.screenCX + (worldX - gw.getPlayerWorldX()));
        int sy = (int)(gw.screenCY + (worldY - gw.getPlayerWorldY()));

        setLocation(sx, sy);
    }

    public void hitEnemy()
    {
        for(Enemy e : getWorld().getObjects(Enemy.class))
        {
            double dx = e.worldX - worldX;
            double dy = e.worldY - worldY;

            if(Math.sqrt(dx * dx + dy * dy) < 25)
            {
                e.takeDamage(damage);
                getWorld().removeObject(this);
                return;
            }
        }
    }
}