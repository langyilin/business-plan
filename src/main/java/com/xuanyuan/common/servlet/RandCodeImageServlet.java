//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xuanyuan.common.servlet;

import com.xuanyuan.common.utils.PropertyHolder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@WebServlet(
        name = "RandCodeImage",
        urlPatterns = {"/randCodeImage"}
)
public class RandCodeImageServlet extends HttpServlet {
    private static final long serialVersionUID = -1257947018545327308L;
    private static final int count = 200;
    private static final int width = 105;
    private static final int height = 35;
    private static final int lineWidth = 2;

    public RandCodeImageServlet() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        BufferedImage image = new BufferedImage(105, 35, 1);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 105, 35);
        graphics.drawRect(0, 0, 104, 34);
        Random random = new Random();

        int sessonname;
        for(int resultCode = 0; resultCode < 200; ++resultCode) {
            graphics.setColor(this.getRandColor(150, 200));
            sessonname = random.nextInt(102) + 1;
            int y = random.nextInt(32) + 1;
            int xl = random.nextInt(2);
            int yl = random.nextInt(2);
            graphics.drawLine(sessonname, y, sessonname + xl, y + yl);
        }

        String var11 = this.exctractRandCode();

        for(sessonname = 0; sessonname < var11.length(); ++sessonname) {
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Times New Roman", 1, 24));
            graphics.drawString(String.valueOf(var11.charAt(sessonname)), 23 * sessonname + 8, 26);
        }

        String var12 = (String)PropertyHolder.getProperty("rand.code.sessonname");
        request.getSession().setAttribute(var12, var11);
        graphics.dispose();
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    private String exctractRandCode() {
        String randCodeType = (String)PropertyHolder.getProperty("rand.code.type");
        int randCodeLength = Integer.parseInt((String)PropertyHolder.getProperty("rand.code.length"));
        if(randCodeType == null) {
            return RandCodeImageEnum.NUMBER_CHAR.generateStr(randCodeLength);
        } else {
            switch(randCodeType.charAt(0)) {
                case '1':
                    return RandCodeImageEnum.NUMBER_CHAR.generateStr(randCodeLength);
                case '2':
                    return RandCodeImageEnum.LOWER_CHAR.generateStr(randCodeLength);
                case '3':
                    return RandCodeImageEnum.UPPER_CHAR.generateStr(randCodeLength);
                case '4':
                    return RandCodeImageEnum.LETTER_CHAR.generateStr(randCodeLength);
                case '5':
                    return RandCodeImageEnum.ALL_CHAR.generateStr(randCodeLength);
                default:
                    return RandCodeImageEnum.NUMBER_CHAR.generateStr(randCodeLength);
            }
        }
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if(fc > 255) {
            fc = 255;
        }

        if(bc > 255) {
            bc = 255;
        }

        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
