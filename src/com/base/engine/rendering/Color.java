package com.base.engine.rendering;

// @TODO: add support for HSV colors, Color addition, multiplication etc.
public final class Color
{
    public float r, g, b, a; // alpha (0 is transparent, 1 is opaque)

    public Color(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.f;
    }

    public Color(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float maxColorComponent()
    {
        return Math.max(r, Math.max(g, b));
    }


//    public static Color Lerp(Color a, Color b, float t)
//    {
//
//    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Color color = (Color) o;

        if (Float.compare(color.r, r) != 0) return false;
        if (Float.compare(color.g, g) != 0) return false;
        if (Float.compare(color.b, b) != 0) return false;
        return Float.compare(color.a, a) == 0;
    }

    @Override
    public int hashCode()
    {
        int result = (r != +0.0f ? Float.floatToIntBits(r) : 0);
        result = 31 * result + (g != +0.0f ? Float.floatToIntBits(g) : 0);
        result = 31 * result + (b != +0.0f ? Float.floatToIntBits(b) : 0);
        result = 31 * result + (a != +0.0f ? Float.floatToIntBits(a) : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Color{" + "r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + '}';
    }

    public static Color cyan()
    {
        return new Color(0, 1, 1, 1);
    }

    // completely transparent
    public static Color clear()
    {
        return new Color(0, 0, 0, 0);
    }

    public static Color grey()
    {
        return new Color(0.5f, 0.5f, 0.5f, 0.5f);
    }

    public static Color magenta()
    {
        return new Color(1, 0, 1, 1);
    }

    public static Color red()
    {
        return new Color(1, 0, 0, 1);
    }

    public static Color yellow()
    {
        return new Color(1, 0.92f, 0.016f, 1);
    }

    public static Color black()
    {
        return new Color(0, 0, 0, 1);
    }

    public static Color white()
    {
        return new Color(1, 1, 1, 1);
    }

    public static Color green()
    {
        return new Color(0, 1, 0, 1);
    }

    public static Color blue()
    {
        return new Color(0, 0, 1, 1);
    }

}
