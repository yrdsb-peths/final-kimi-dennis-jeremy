import greenfoot.*;

public class GameWorld extends World
{
    public AureaSolvine aureaSolvine;
    public LeonClovis leonClovis;

    int enemySpawnTimer = 0;
    int lightningTimer = 0;
    int fireballTimer = 0;

    static final int ENEMY_SPAWN_INTERVAL = 60;
    static final int SKILL_INTERVAL = 90;
    static final int MIN_SPAWN_DISTANCE = 300;
    static final int MAX_ENEMIES = 30;

    GreenfootImage bgTile;
    int bgOffX = 0;
    int bgOffY = 0;

    public int screenCX;
    public int screenCY;

    public GameWorld()
    {
        this("aurea");
    }

    public GameWorld(String character)
    {
        super(1500, 750, 1);

        screenCX = getWidth() / 2;
        screenCY = getHeight() / 2;

        bgTile = new GreenfootImage("background.png");
        drawBackground(0, 0);

        if(character.equals("leon"))
        {
            leonClovis = new LeonClovis();
            addObject(leonClovis, screenCX, screenCY);
        }
        else
        {
            aureaSolvine = new AureaSolvine();
            addObject(aureaSolvine, screenCX, screenCY);

            IceWave iceWave = new IceWave();
            addObject(iceWave, screenCX, screenCY);
        }
    }

    public void act()
    {
        updateCamera();
        drawBackground(bgOffX, bgOffY);
        updateScreenPositions();
        spawnEnemy();

        if(aureaSolvine != null)
        {
            spawnFireball();
            spawnLightning();
        }

        drawHUD();
    }

    public double getPlayerWorldX()
    {
        if(aureaSolvine != null)
        {
            return aureaSolvine.worldX;
        }

        return leonClovis.worldX;
    }

    public double getPlayerWorldY()
    {
        if(aureaSolvine != null)
        {
            return aureaSolvine.worldY;
        }

        return leonClovis.worldY;
    }

    public int getPlayerDamage()
    {
        if(aureaSolvine != null)
        {
            return aureaSolvine.getDamage();
        }

        return leonClovis.gunDamage;
    }

    public void damagePlayer(int damage)
    {
        if(aureaSolvine != null)
        {
            aureaSolvine.takeHit(damage);
        }

        if(leonClovis != null)
        {
            leonClovis.takeDamage(damage);
        }
    }

    public void givePlayerReward(int xp, int coin)
    {
        if(aureaSolvine != null)
        {
            aureaSolvine.xp += xp;
            aureaSolvine.coin += coin;
        }

        if(leonClovis != null)
        {
            leonClovis.xp += xp;
            leonClovis.coin += coin;
        }
    }

    public void updateCamera()
    {
        double px = getPlayerWorldX();
        double py = getPlayerWorldY();

        bgOffX = (int)((-px % bgTile.getWidth()
                + bgTile.getWidth())
                % bgTile.getWidth());

        bgOffY = (int)((-py % bgTile.getHeight()
                + bgTile.getHeight())
                % bgTile.getHeight());
    }

    public void updateScreenPositions()
    {
        double camX = getPlayerWorldX();
        double camY = getPlayerWorldY();

        if(aureaSolvine != null)
        {
            aureaSolvine.setLocation(screenCX, screenCY);
        }

        if(leonClovis != null)
        {
            leonClovis.setLocation(screenCX, screenCY);
        }

        for(Enemy e : getObjects(Enemy.class))
        {
            int sx = (int)(screenCX + (e.worldX - camX));
            int sy = (int)(screenCY + (e.worldY - camY));
            e.setLocation(sx, sy);
        }

        for(Bullet b : getObjects(Bullet.class))
        {
            int sx = (int)(screenCX + (b.worldX - camX));
            int sy = (int)(screenCY + (b.worldY - camY));
            b.setLocation(sx, sy);
        }

        for(Fireball f : getObjects(Fireball.class))
        {
            int sx = (int)(screenCX + (f.worldX - camX));
            int sy = (int)(screenCY + (f.worldY - camY));
            f.setLocation(sx, sy);
        }

        for(Lightning l : getObjects(Lightning.class))
        {
            int sx = (int)(screenCX + (l.worldX - camX));
            int sy = (int)(screenCY + (l.worldY - camY));
            l.setLocation(sx, sy);
        }

        for(IceWave iw : getObjects(IceWave.class))
        {
            iw.worldX = getPlayerWorldX();
            iw.worldY = getPlayerWorldY();
            iw.setLocation(screenCX, screenCY);
        }
    }

    public void drawBackground(int offX, int offY)
    {
        GreenfootImage bg = getBackground();
        bg.clear();

        int tw = bgTile.getWidth();
        int th = bgTile.getHeight();

        for(int x = offX - tw; x < getWidth() + tw; x += tw)
        {
            for(int y = offY - th; y < getHeight() + th; y += th)
            {
                bg.drawImage(bgTile, x, y);
            }
        }
    }

    public void drawHUD()
    {
        if(aureaSolvine != null)
        {
            aureaSolvine.displayStats();
        }

        if(leonClovis != null)
        {
            leonClovis.displayStats();
        }
    }

    public void spawnEnemy()
    {
        enemySpawnTimer++;

        if(enemySpawnTimer >= ENEMY_SPAWN_INTERVAL)
        {
            enemySpawnTimer = 0;

            if(getObjects(Enemy.class).size() >= MAX_ENEMIES)
            {
                return;
            }

            double px = getPlayerWorldX();
            double py = getPlayerWorldY();

            double x;
            double y;

            do
            {
                x = px + Greenfoot.getRandomNumber(1600) - 800;
                y = py + Greenfoot.getRandomNumber(900) - 450;
            }
            while(distanceBetween(x, y, px, py) < MIN_SPAWN_DISTANCE);

            Enemy e = new Enemy(x, y);

            int sx = (int)(screenCX + (x - px));
            int sy = (int)(screenCY + (y - py));

            addObject(e, sx, sy);
        }
    }

    public void spawnFireball()
    {
        fireballTimer++;

        if(fireballTimer >= SKILL_INTERVAL)
        {
            fireballTimer = 0;

            Enemy target = getClosestEnemy();

            if(target != null)
            {
                Fireball fb = new Fireball(
                    getPlayerWorldX(),
                    getPlayerWorldY(),
                    target.worldX,
                    target.worldY,
                    getPlayerDamage()
                );

                addObject(fb, screenCX, screenCY);
            }
        }
    }

    public void spawnLightning()
    {
        lightningTimer++;

        if(lightningTimer >= SKILL_INTERVAL)
        {
            lightningTimer = 0;

            Enemy target = getClosestEnemy();

            if(target != null)
            {
                Lightning lt = new Lightning(
                    target.worldX,
                    target.worldY,
                    getPlayerDamage()
                );

                int sx = (int)(screenCX + (target.worldX - getPlayerWorldX()));
                int sy = (int)(screenCY + (target.worldY - getPlayerWorldY()));

                addObject(lt, sx, sy);
            }
        }
    }

    public Enemy getClosestEnemy()
    {
        java.util.List<Enemy> enemies = getObjects(Enemy.class);

        if(enemies.isEmpty())
        {
            return null;
        }

        Enemy closest = null;
        double minD = Double.MAX_VALUE;

        double px = getPlayerWorldX();
        double py = getPlayerWorldY();

        for(Enemy e : enemies)
        {
            double d = distanceBetween(e.worldX, e.worldY, px, py);

            if(d < minD)
            {
                minD = d;
                closest = e;
            }
        }

        return closest;
    }

    public double distanceBetween(double x1, double y1, double x2, double y2)
    {
        double dx = x1 - x2;
        double dy = y1 - y2;

        return Math.sqrt(dx * dx + dy * dy);
    }
}