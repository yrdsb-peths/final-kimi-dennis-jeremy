import greenfoot.*;

public class GameWorld extends World
{
    private static final String BACKGROUND_IMAGE = "images/Background .png";
    private static final int ENEMY_SPAWN_INTERVAL_MS = 2000;
    private static final int SKILL_INTERVAL = 90;
    private static final int MIN_SPAWN_DISTANCE = 300;
    private static final int MAX_ENEMIES = 30;

    public AureaSolvine aureaSolvine;
    private final SimpleTimer enemySpawnTimer = new SimpleTimer();

    int lightningTimer = 0;
    int fireballTimer = 0;

    GreenfootImage bgTile;
    int bgOffX = 0;
    int bgOffY = 0;

    int screenCX;
    int screenCY;

    public GameWorld()
    {
        super(1500, 750, 1);
        screenCX = getWidth() / 2;
        screenCY = getHeight() / 2;

        setUpBackgroundTile();
        drawBackground(0, 0);

        aureaSolvine = new AureaSolvine();
        addObject(aureaSolvine, screenCX, screenCY);
    }

    private void setUpBackgroundTile()
    {
        try
        {
            bgTile = new GreenfootImage(BACKGROUND_IMAGE);
        }
        catch(IllegalArgumentException exception)
        {
            bgTile = new GreenfootImage(getWidth(), getHeight());
            bgTile.setColor(Color.BLACK);
            bgTile.fill();
        }
    }

    public void act()
    {
        bgOffX = (int)((-aureaSolvine.worldX % bgTile.getWidth() + bgTile.getWidth()) % bgTile.getWidth());
        bgOffY = (int)((-aureaSolvine.worldY % bgTile.getHeight() + bgTile.getHeight()) % bgTile.getHeight());
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

        for(Enemy enemy : getObjects(Enemy.class))
        {
            int screenX = (int)(screenCX + (enemy.worldX - camX));
            int screenY = (int)(screenCY + (enemy.worldY - camY));
            enemy.setLocation(screenX, screenY);
            enemy.turnTowards(screenCX, screenCY);
        }

        for(Fireball fireball : getObjects(Fireball.class))
        {
            int screenX = (int)(screenCX + (fireball.worldX - camX));
            int screenY = (int)(screenCY + (fireball.worldY - camY));
            fireball.setLocation(screenX, screenY);
        }

        for(Lightning lightning : getObjects(Lightning.class))
        {
            int screenX = (int)(screenCX + (lightning.worldX - camX));
            int screenY = (int)(screenCY + (lightning.worldY - camY));
            lightning.setLocation(screenX, screenY);
        }
    }

    private void drawBackground(int offX, int offY)
    {
        GreenfootImage background = getBackground();
        background.clear();
        int tileWidth = bgTile.getWidth();
        int tileHeight = bgTile.getHeight();

        for(int x = offX - tileWidth; x < getWidth() + tileWidth; x += tileWidth)
        {
            for(int y = offY - tileHeight; y < getHeight() + tileHeight; y += tileHeight)
            {
                background.drawImage(bgTile, x, y);
            }
        }
    }

    public void drawHUD()
    {
        AureaSolvine player = aureaSolvine;
        drawBar(30, 30, 200, 18, player.hp, player.maxHp, new Color(180, 40, 40), new Color(60, 10, 10));
        drawBar(30, 56, 200, 14, player.xp, player.xpToNextLevel, new Color(50, 120, 220), new Color(15, 40, 80));
        showText("HP  " + player.hp + " / " + player.maxHp, 240, 39);
        showText("XP  " + player.xp + " / " + player.xpToNextLevel + "   Lv." + player.level, 270, 63);
        showText("Coin: " + player.coin, 80, 90);
    }

    private void drawBar(int x, int y, int width, int height, int current, int max, Color fillColor, Color backgroundColor)
    {
        GreenfootImage canvas = getBackground();
        canvas.setColor(backgroundColor);
        canvas.fillRect(x, y, width, height);

        int fillWidth = Math.max(0, Math.min(width, (int)((double)current / max * width)));
        canvas.setColor(fillColor);
        canvas.fillRect(x, y, fillWidth, height);
        canvas.setColor(Color.WHITE);
        canvas.drawRect(x, y, width, height);
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

            if(closest != null && aureaSolvine.hasEquippedFireball())
            {
                Fireball fireball = new Fireball(
                    aureaSolvine.worldX,
                    aureaSolvine.worldY,
                    closest.worldX,
                    closest.worldY,
                    aureaSolvine.getDamage()
                );
                addObject(fireball, screenCX, screenCY);
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
                double enemyX = closest.worldX;
                double enemyY = closest.worldY;
                int screenX = (int)(screenCX + (enemyX - aureaSolvine.worldX));
                int screenY = (int)(screenCY + (enemyY - aureaSolvine.worldY));
                Lightning lightning = new Lightning(enemyX, enemyY, aureaSolvine.getDamage());
                addObject(lightning, screenX, screenY);
            }
        }
    }

    public void spawnEnemy()
    {
        if(enemySpawnTimer.millisElapsed() < ENEMY_SPAWN_INTERVAL_MS)
        {
            return;
        }

        enemySpawnTimer.mark();

        if(getObjects(Enemy.class).size() >= MAX_ENEMIES)
        {
            return;
        }

        double x;
        double y;

        do
        {
            x = aureaSolvine.worldX + Greenfoot.getRandomNumber(1600) - 800;
            y = aureaSolvine.worldY + Greenfoot.getRandomNumber(900) - 450;
        }
        while(distanceBetween(x, y, aureaSolvine.worldX, aureaSolvine.worldY) < MIN_SPAWN_DISTANCE);

        Enemy enemy = new Enemy(x, y);
        int screenX = (int)(screenCX + (x - aureaSolvine.worldX));
        int screenY = (int)(screenCY + (y - aureaSolvine.worldY));
        addObject(enemy, screenX, screenY);
    }

    public Enemy getClosestEnemy()
    {
        java.util.List<Enemy> enemies = getObjects(Enemy.class);

        if(enemies.isEmpty())
        {
            return null;
        }

        Enemy closest = null;
        double minDistance = Double.MAX_VALUE;

        for(Enemy enemy : enemies)
        {
            double distance = distanceBetween(enemy.worldX, enemy.worldY, aureaSolvine.worldX, aureaSolvine.worldY);

            if(distance < minDistance)
            {
                minDistance = distance;
                closest = enemy;
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
