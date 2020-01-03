package com.base.engine.core;

import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Color;
import com.base.engine.rendering.Shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public final class Debug
{
    // NOTE: These are set to some default values
    public static String infoColor = ConsoleColors.GREEN;
    public static String warningColor = ConsoleColors.YELLOW;
    public static String exceptionColor = ConsoleColors.PURPLE_BOLD;
    public static String errorColor = ConsoleColors.RED_BOLD_BRIGHT;

    private Debug() {}

    public static void Assert(boolean isTrue, String messageToDisplay)
    {
        if (!isTrue) // NOTE: output only if the assertion equals to 0, else output nothing
            System.out.println(messageToDisplay);
    }

    // TODO: make local to drawLine() ?
    private static final class LineShader extends Shader
    {
        LineShader()
        {
            super();

            addVertexShaderFromFile("line.vert");
            addFragmentShaderFromFile("line.frag");
            compileShader();

            addUniform("customColor");
            addUniform("viewProjection");
        }

        void updateUniforms(Color customColor, Matrix4f viewProjection)
        {
            setUniformVec3("customColor", customColor.toVec3());
            setUniformMat4("viewProjection", viewProjection);
        }

    }

    // TODO: To be normally implemented later
//    private static int VAO = 0, VBO = 0;
//    private static LineShader lineShader;
//    public static void drawLine(Vector3f startPoint, Vector3f endPoint, Color lineColor, Matrix4f viewProjection)
//    {
//        float vertices[] = {
//                startPoint.getX(), startPoint.getY(), startPoint.getZ(),
//                endPoint.getX(), endPoint.getY(), endPoint.getZ(),
//        };
//
//            if (VAO == 0)
//            {
//                VAO = glGenVertexArrays();
//                VBO = glGenBuffers();
//
//                glBindVertexArray(VAO);
//                glBindBuffer(GL_ARRAY_BUFFER, VBO);
//                glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
//
//                glEnableVertexAttribArray(0);
//                glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
//
//                lineShader = new LineShader();
//        }
//
//        glBindVertexArray(VAO);
//        lineShader.bind();
//        lineShader.updateUniforms(lineColor, viewProjection);
//
//        glLineWidth(100);
//        glDrawArrays(GL_LINES, 0, 2);
//
//        glBindVertexArray(0);
//    }
//
//    public static void drawLine(Vector3f startPoint, Vector3f endPoint, Color lineColor)
//    {
//        glLineWidth(10);
//        glColor3f(lineColor.r, lineColor.g, lineColor.b);
//        glBegin(GL_LINES);
//            glVertex3f(startPoint.getX(), startPoint.getY(), startPoint.getZ());
//            glVertex3f(endPoint.getX(), endPoint.getY(), endPoint.getZ());
//        glEnd();
//    }
//
//    public static void drawPoint(Vector3f startPoint, Color lineColor)
//    {
//        glLineWidth(10);
//        glColor3f(lineColor.r, lineColor.g, lineColor.b);
//        glBegin(GL_LINES);
//            glVertex3f(startPoint.getX(), startPoint.getY(), startPoint.getZ());
//        glEnd();
//    }
//
//    public static void drawRay(Vector3f startPoint, Vector3f targetPoint, Color rayColor, Matrix4f viewProjection)
//    {
//        // TODO
//        drawLine(startPoint, targetPoint, rayColor, viewProjection);
//    }

    public static void logInfo(String message)
    {
        System.out.println(infoColor + message + ConsoleColors.RESET);
    }

    public static void logWarning(String message)
    {
        System.out.println(warningColor + message + ConsoleColors.RESET);
    }

    public static void logException(String exceptionMessage)
    {
        System.out.println(exceptionColor + exceptionMessage + ConsoleColors.RESET);
    }

    public static void logError(String errorMessage)
    {
        System.out.println(errorColor + errorMessage + ConsoleColors.RESET);
    }


    public class ConsoleColors
    {
        // Reset
        public static final String RESET = "\033[0m";  // Text Reset

        // Regular Colors
        public static final String BLACK = "\033[0;30m";   // BLACK
        public static final String RED = "\033[0;31m";     // RED
        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
        public static final String PURPLE = "\033[0;35m";  // PURPLE
        public static final String CYAN = "\033[0;36m";    // CYAN
        public static final String WHITE = "\033[0;37m";   // WHITE

        // Bold
        public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
        public static final String RED_BOLD = "\033[1;31m";    // RED
        public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
        public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
        public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
        public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
        public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
        public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

        // Underline
        public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
        public static final String RED_UNDERLINED = "\033[4;31m";    // RED
        public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
        public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
        public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
        public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
        public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
        public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

        // Background
        public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
        public static final String RED_BACKGROUND = "\033[41m";    // RED
        public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
        public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
        public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
        public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
        public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
        public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

        // High Intensity
        public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
        public static final String RED_BRIGHT = "\033[0;91m";    // RED
        public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
        public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
        public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
        public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
        public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
        public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

        // Bold High Intensity
        public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
        public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
        public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
        public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
        public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
        public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
        public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
        public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

        // High Intensity backgrounds
        public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
        public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
        public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
        public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
        public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
        public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
        public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
        public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
    }

}
