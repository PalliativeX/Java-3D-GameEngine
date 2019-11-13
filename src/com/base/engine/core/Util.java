package com.base.engine.core;

import com.base.engine.core.math.Matrix4f;
import com.base.engine.rendering.Vertex;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Util
{
    public static FloatBuffer createFloatBuffer(int size)
    {
        return BufferUtils.createFloatBuffer(size);
    }

    public static IntBuffer createIntBuffer(int size)
    {
        return BufferUtils.createIntBuffer(size);
    }

    public static ByteBuffer createByteBuffer(int size)
    {
        return BufferUtils.createByteBuffer(size);
    }

    public static IntBuffer createFlippedBuffer(int... values)
    {
        IntBuffer buffer = createIntBuffer(values.length);
        buffer.put(values);
        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Vertex[] vertices)
    {
        FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);

        for (int i = 0; i < vertices.length; i++) {
            buffer.put(vertices[i].getPos().getX());
            buffer.put(vertices[i].getPos().getY());
            buffer.put(vertices[i].getPos().getZ());
            buffer.put(vertices[i].getTexCoord().getX());
            buffer.put(vertices[i].getTexCoord().getY());
            buffer.put(vertices[i].getNormal().getX());
            buffer.put(vertices[i].getNormal().getY());
            buffer.put(vertices[i].getNormal().getZ());
            buffer.put(vertices[i].getTangent().getX());
            buffer.put(vertices[i].getTangent().getY());
            buffer.put(vertices[i].getTangent().getZ());
        }

        buffer.flip();

        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(Matrix4f value)
    {
        FloatBuffer buffer = createFloatBuffer( 4 * 4);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                buffer.put(value.get(i, j));

        buffer.flip();
        return buffer;
    }

    public static FloatBuffer createFlippedBuffer(float[] values)
    {
        FloatBuffer buffer = createFloatBuffer(values.length);

        for (int i = 0; i < values.length; i++)
            buffer.put(values[i]);

        buffer.flip();
        return buffer;
    }

    public static String[] removeEmptyString(String[] data)
    {
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            if (!data[i].equals(""))
                result.add(data[i]);
        }
        String[] res = new String[result.size()];
        result.toArray(res);

        return res;
    }

    public static int[] toIntArray(Integer[] data)
    {
        int[] result = new int[data.length];

        for (int i = 0; i < data.length; i++)
            result[i] = data[i].intValue();

        return result;
    }

    // a struct for holding all relevant image data
    public static class ImageData
    {
        public ByteBuffer pixelBuffer;
        public int width;
        public int height;

        public ImageData(ByteBuffer pixelBuffer, int width, int height)
        {
            this.pixelBuffer = pixelBuffer;
            this.height = height;
            this.width = width;
        }

        public ImageData()
        {
            pixelBuffer = null;
            width = 0;
            height = 0;
        }
    }

    public static ImageData loadImage(String fileName)
    {
        try {
            ImageData imageData = new ImageData();

            BufferedImage image = ImageIO.read(new File("./resources/textures/" + fileName));
            imageData.width = image.getWidth();
            imageData.height = image.getHeight();

            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

            ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 3);

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = pixels[y * image.getWidth() + x];

                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) ((pixel) & 0xFF));
                }
            }
            buffer.flip();
            imageData.pixelBuffer = buffer;
            return imageData;
        }
        catch (IOException e) {
            System.out.println("Skybox texture failed to load!");
            e.printStackTrace();
            System.exit(1);
        }

        return new ImageData();
    }

}
