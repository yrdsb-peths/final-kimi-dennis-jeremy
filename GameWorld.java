import greenfoot.*;

public class GameWorld extends World
{
    public AureaSolvine aureaSolvine;

    int enemySpawnTimer = 0;
    int lightningTimer = 0;
    int fireballTimer = 0;

    static final int ENEMY_SPAWN_INTERVAL = 60;
    static final int SKILL_INTERVAL = 90;
    static final int MIN_SPAWN_DISTANCE = 200;
    static final int MAX_ENEMIES = 30;

    public GameWorld()
    {
        super(1500, 750, 1);
        aureaSolvine = new AureaSolvine();
        addObject(aureaSolvine, 750, 375);
    }

    public void act()
    {
        spawnEnemy();
        spawnFireball();
        spawnLightning();
        checkPlayerDead();
        drawHUD();
    }

    public void checkPlayerDead()
    {
        if(aureaSolvine.hp <= 0)
        {
            Greenfoot.setWorld(new TitleScreen());
        }
    }
    
    private void drawBar(int x, int y, int w, int h,
                     int current, int max,
                     Color fill,
                     Color bg)
    {
        getBackground().setColor(bg);
        getBackground().fillRect(x, y, w, h);
    
        int filled = (int)((double)current / max * w);
        filled = Math.max(0, Math.min(w, filled));
        getBackground().setColor(fill);
        getBackground().fillRect(x, y, filled, h);
    
        getBackground().setColor(Color.WHITE);
        getBackground().drawRect(x, y, w, h);
    }

    public void drawHUD()
    {
        AureaSolvine p = aureaSolvine;
    
        
        drawBar(30, 30, 200, 18,
            p.hp, p.maxHp,
            new Color(180, 40, 40),
            new Color(60, 10, 10));
    
        drawBar(30, 56, 200, 14,
            p.xp, p.xpToNextLevel,
            new Color(50, 120, 220),
            new Color(15, 40, 80));
    
        showText("HP  " + p.hp + " / " + p.maxHp, 240, 39);
        showText("XP  " + p.xp + " / " + p.xpToNextLevel
                 + "   Lv." + p.level, 270, 63);
        showText("Coin: " + p.coin, 80, 90);
    }
    
    public void spawnFireball()
    {
        fireballTimer++;
        if(fireballTimer >= SKILL_INTERVAL)
        {
            fireballTimer = 0;
            Enemy closest = getClosestEnemy();
            if(closest != null)
            {
                addObject(
                    new Fireball(closest.getX(), closest.getY()),
                    aureaSolvine.getX(),
                    aureaSolvine.getY()
                );
            }
        }
    }

    public void spawnLightning()
    {
        lightningTimer++;
        if(lightningTimer >= SKILL_INTERVAL)
        {
            lightningTimer = 0;
            Enemy closest = getClosestEnemy();
            if(closest != null)
            {
                addObject(
                    new Lightning(aureaSolvine.getDamage()),
                    closest.getX(),
                    closest.getY()
                );
            }
        }
    }

    public void spawnEnemy()
    {
        enemySpawnTimer++;
        if(enemySpawnTimer >= ENEMY_SPAWN_INTERVAL)
        {
            enemySpawnTimer = 0;

            if(getObjects(Enemy.class).size() >= MAX_ENEMIES) return;

            int x, y;
            do
            {
                x = Greenfoot.getRandomNumber(getWidth());
                y = Greenfoot.getRandomNumber(getHeight());
            }
            while(distanceBetween(x, y,
                    aureaSolvine.getX(),
                    aureaSolvine.getY()) < MIN_SPAWN_DISTANCE);

            addObject(new Enemy(), x, y);
        }
    }

    public Enemy getClosestEnemy()
    {
        java.util.List<Enemy> enemies = getObjects(Enemy.class);
        if(enemies.isEmpty()) return null;

        Enemy closest = enemies.get(0);
        double closestDist = distanceBetween(
            aureaSolvine.getX(), aureaSolvine.getY(),
            closest.getX(), closest.getY());

        for(Enemy e : enemies)
        {
            double d = distanceBetween(
                aureaSolvine.getX(), aureaSolvine.getY(),
                e.getX(), e.getY());
            if(d < closestDist) { closestDist = d; closest = e; }
        }
        return closest;
    }

    public double distanceBetween(int x1, int y1, int x2, int y2)
    {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }
}