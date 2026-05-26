import greenfoot.*;

public class GameWorld extends World
{
    public AureaSolvine aureaSolvine;

    int enemySpawnTimer = 0;
    int lightningTimer  = 0;
    int fireballTimer   = 0;

    static final int ENEMY_SPAWN_INTERVAL = 60;
    static final int SKILL_INTERVAL       = 90;
    static final int MIN_SPAWN_DISTANCE   = 300;
    static final int MAX_ENEMIES          = 30;

    GreenfootImage bgTile;
    int bgOffX = 0;
    int bgOffY = 0;

    int screenCX; 
    int screenCY; 

    public GameWorld()
    {
        super(1500, 750, 1);
        screenCX = getWidth()  / 2;
        screenCY = getHeight() / 2;

        
        bgTile = new GreenfootImage("background.png");
        drawBackground(0, 0);

        aureaSolvine = new AureaSolvine();
        addObject(aureaSolvine, screenCX, screenCY);
        
        IceWave iceWave = new IceWave();
        iceWave.worldX = aureaSolvine.worldX;
        iceWave.worldY = aureaSolvine.worldY;
        addObject(iceWave, screenCX, screenCY);
    }

    public void act()
    {

        bgOffX = (int)((-aureaSolvine.worldX % bgTile.getWidth()
                        + bgTile.getWidth()) % bgTile.getWidth());
        bgOffY = (int)((-aureaSolvine.worldY % bgTile.getHeight()
                        + bgTile.getHeight()) % bgTile.getHeight());
        drawBackground(bgOffX, bgOffY);


        updateScreenPositions();

        spawnEnemy();
        spawnFireball();
        spawnLightning();
        checkPlayerDead();
        drawHUD();
    }

    private void updateScreenPositions()
    {
        double camX = aureaSolvine.worldX;
        double camY = aureaSolvine.worldY;

        for(Enemy e : getObjects(Enemy.class))
        {
            int sx = (int)(screenCX + (e.worldX - camX));
            int sy = (int)(screenCY + (e.worldY - camY));
            e.setLocation(sx, sy);

            e.turnTowards(screenCX, screenCY);
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
            iw.setLocation(screenCX, screenCY);
        }
    }

    private void drawBackground(int offX, int offY)
    {
        GreenfootImage bg = getBackground();
        bg.clear();
        int tw = bgTile.getWidth();
        int th = bgTile.getHeight();
        for(int x = offX - tw; x < getWidth()  + tw; x += tw)
        for(int y = offY - th; y < getHeight() + th; y += th)
            bg.drawImage(bgTile, x, y);
    }

    public void drawHUD()
    {
        AureaSolvine p = aureaSolvine;
        drawBar(30, 30, 200, 18, p.hp, p.maxHp,
            new Color(180,40,40), new Color(60,10,10));
        drawBar(30, 56, 200, 14, p.xp, p.xpToNextLevel,
            new Color(50,120,220), new Color(15,40,80));
        showText("HP  " + p.hp + " / " + p.maxHp, 240, 39);
        showText("XP  " + p.xp + " / " + p.xpToNextLevel
                 + "   Lv." + p.level, 270, 63);
        showText("Coin: " + p.coin, 80, 90);
    }

    private void drawBar(int x, int y, int w, int h,
                         int cur, int max, Color fill, Color bg)
    {
        GreenfootImage c = getBackground();
        c.setColor(bg);   c.fillRect(x, y, w, h);
        int f = Math.max(0, Math.min(w, (int)((double)cur/max*w)));
        c.setColor(fill); c.fillRect(x, y, f, h);
        c.setColor(Color.WHITE); c.drawRect(x, y, w, h);
    }

    public void checkPlayerDead()
    {
        if(aureaSolvine.isDead && aureaSolvine.animFrame >= 6)
        {
            Greenfoot.setWorld(new TitleScreen());
        }
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
                Fireball fb = new Fireball(
                    aureaSolvine.worldX,
                    aureaSolvine.worldY,
                    closest.worldX,
                    closest.worldY,
                    aureaSolvine.getDamage()
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
            Enemy closest = getClosestEnemy();
            if(closest != null)
            {
                double ex = closest.worldX;
                double ey = closest.worldY;
                int sx = (int)(screenCX + (ex - aureaSolvine.worldX));
                int sy = (int)(screenCY + (ey - aureaSolvine.worldY));
                Lightning lt = new Lightning(ex, ey,
                                             aureaSolvine.getDamage());
                addObject(lt, sx, sy);
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

            double x, y;
            do {
                x = aureaSolvine.worldX
                    + Greenfoot.getRandomNumber(1600) - 800;
                y = aureaSolvine.worldY
                    + Greenfoot.getRandomNumber(900)  - 450;
            }
            while(distanceBetween(x, y,
                      aureaSolvine.worldX,
                      aureaSolvine.worldY) < MIN_SPAWN_DISTANCE);

            Enemy e = new Enemy(x, y);
            
            int sx = (int)(screenCX + (x - aureaSolvine.worldX));
            int sy = (int)(screenCY + (y - aureaSolvine.worldY));
            addObject(e, sx, sy);
        }
    }

    public Enemy getClosestEnemy()
    {
        java.util.List<Enemy> enemies = getObjects(Enemy.class);
        if(enemies.isEmpty()) return null;
        Enemy closest = null;
        double minD = Double.MAX_VALUE;
        for(Enemy e : enemies)
        {
            double d = distanceBetween(
                e.worldX, e.worldY,
                aureaSolvine.worldX, aureaSolvine.worldY);
            if(d < minD) { minD = d; closest = e; }
        }
        return closest;
    }

    public double distanceBetween(double x1, double y1,
                                  double x2, double y2)
    {
        double dx = x1-x2, dy = y1-y2;
        return Math.sqrt(dx*dx + dy*dy);
    }
}