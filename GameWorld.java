import greenfoot.*;

public class GameWorld extends World
{
    private static final String BACKGROUND_IMAGE = "background.png";
    private static final int ENEMY_SPAWN_INTERVAL = 60;
    private static final int SKILL_INTERVAL = 90;
    private static final int MIN_SPAWN_DISTANCE = 300;
    private static final int MAX_ENEMIES = 30;

    public AureaSolvine aureaSolvine;
    public LeonClovis leonClovis;
    public KaineVelsarth kaineVelsarth;

    int enemySpawnTimer = 0;
    int lightningTimer = 0;
    int fireballTimer = 0;
    private boolean gameOverShown = false;

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

        setUpBackgroundTile();
        drawBackground(0, 0);

        if(character.equals("leon"))
        {
            leonClovis = new LeonClovis();
            addObject(leonClovis, screenCX, screenCY);
        }
        else if(character.equals("kaine"))
        {
            kaineVelsarth = new KaineVelsarth();
            addObject(kaineVelsarth, screenCX, screenCY);
            addObject(new fireSword(), screenCX, screenCY);
        }
        else
        {
            aureaSolvine = new AureaSolvine();
            addObject(aureaSolvine, screenCX, screenCY);
            addObject(new IceWave(), screenCX, screenCY);
        }
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
        if(gameOverShown)
        {
            return;
        }

        updateCamera();
        drawBackground(bgOffX, bgOffY);
        updateScreenPositions();
        spawnEnemy();

        if(aureaSolvine != null)
        {
            spawnFireball();
            spawnLightning();
            checkPlayerDead();
        }

        drawHUD();
    }

    public double getPlayerWorldX()
    {
        if(aureaSolvine != null)
        {
            return aureaSolvine.worldX;
        }

        if(leonClovis != null)
        {
            return leonClovis.worldX;
        }

        return 0;
    }

    public double getPlayerWorldY()
    {
        if(aureaSolvine != null)
        {
            return aureaSolvine.worldY;
        }

        if(leonClovis != null)
        {
            return leonClovis.worldY;
        }

        return 0;
    }

    public int getPlayerDamage()
    {
        if(aureaSolvine != null)
        {
            return aureaSolvine.getDamage();
        }

        return leonClovis.gunDamage;
    }

    public int getCurrentPlayerLevel()
    {
        if(aureaSolvine != null)
        {
            return aureaSolvine.level;
        }

        if(leonClovis != null)
        {
            return leonClovis.level;
        }

        return 1;
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
            aureaSolvine.gainXP(xp);
            aureaSolvine.gainCoin(coin);
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

        bgOffX = (int)((-px % bgTile.getWidth() + bgTile.getWidth()) % bgTile.getWidth());
        bgOffY = (int)((-py % bgTile.getHeight() + bgTile.getHeight()) % bgTile.getHeight());
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

        for(Enemy enemy : getObjects(Enemy.class))
        {
            int screenX = (int)(screenCX + (enemy.worldX - camX));
            int screenY = (int)(screenCY + (enemy.worldY - camY));
            enemy.setLocation(screenX, screenY);
            enemy.turnTowards(screenCX, screenCY);
        }

        for(Bullet bullet : getObjects(Bullet.class))
        {
            int screenX = (int)(screenCX + (bullet.worldX - camX));
            int screenY = (int)(screenCY + (bullet.worldY - camY));
            bullet.setLocation(screenX, screenY);
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

        for(IceWave iceWave : getObjects(IceWave.class))
        {
            iceWave.worldX = getPlayerWorldX();
            iceWave.worldY = getPlayerWorldY();
            iceWave.setLocation(screenCX, screenCY);
        }
    }

    public void drawBackground(int offX, int offY)
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
        if(aureaSolvine != null)
        {
            aureaSolvine.displayStats();
        }

        if(leonClovis != null)
        {
            leonClovis.displayStats();
        }
    }

    public void checkPlayerDead()
    {
        if(aureaSolvine.isDead && aureaSolvine.animFrame >= 6 && !gameOverShown)
        {
            gameOverShown = true;
            addObject(new GameOver(), screenCX, screenCY);
            Greenfoot.stop();
        }
    }

    public void spawnEnemy()
    {
        enemySpawnTimer++;

        if(enemySpawnTimer < ENEMY_SPAWN_INTERVAL)
        {
            return;
        }

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

        Enemy enemy = new Enemy(x, y);
        enemy.applyLevelScaling(getCurrentPlayerLevel());
        int screenX = (int)(screenCX + (x - px));
        int screenY = (int)(screenCY + (y - py));
        addObject(enemy, screenX, screenY);
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
                    getPlayerWorldX(),
                    getPlayerWorldY(),
                    closest.worldX,
                    closest.worldY,
                    getPlayerDamage()
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
                Lightning lightning = new Lightning(closest.worldX, closest.worldY, getPlayerDamage());
                int screenX = (int)(screenCX + (closest.worldX - getPlayerWorldX()));
                int screenY = (int)(screenCY + (closest.worldY - getPlayerWorldY()));
                addObject(lightning, screenX, screenY);
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
        double minDistance = Double.MAX_VALUE;
        double px = getPlayerWorldX();
        double py = getPlayerWorldY();

        for(Enemy enemy : enemies)
        {
            double distance = distanceBetween(enemy.worldX, enemy.worldY, px, py);

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
