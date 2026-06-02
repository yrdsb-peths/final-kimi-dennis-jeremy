import greenfoot.*;

public class KaineVelsarth extends Hero
{
    static GreenfootImage kaineImage;

    static
    {
        kaineImage = new GreenfootImage("character/Kaine.png");
        if(kaineImage.getWidth() > 0)
            kaineImage.scale(72, 72);
    }

    public KaineVelsarth()
    {
        super();
        if(kaineImage.getWidth() > 0)
            setImage(kaineImage);
    }

    @Override
    protected void updateAnimation()
    {
        animateKaine();
    }

    private void animateKaine()
    {
        GreenfootImage img = new GreenfootImage(kaineImage);
        if(facingLeft) img.mirrorHorizontally();
        setImage(img);
    }

    @Override
    protected void onDeathAnimation()
    {
        // Kaine has no death animation; keep last frame
    }
}
