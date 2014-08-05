package com.litt.core.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import com.litt.core.common.Utility;
import com.litt.core.random.StringRandom;


/**
 * 图片认证码.
 * 
 * <pre><b>描述：</b>
 * 图片认证码实现
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-09-22
 * @version 1.0
 */
public class Captcha extends StringRandom
{    
    
    /** 认证码实际值. */
    private String captchaCode;
    
    /** 认证码图片. */
    private BufferedImage captchaImage;
    
    /** 图片宽度. */
    private int width = 60;	
    
    /** 图片高度. */
    private int height = 20;

    /**
	 * 生成随机颜色.
	 * 
	 * @param fc
	 *            the fc
	 * @param bc
	 *            the bc
	 * 
	 * @return the rand color
	 */
    private Color getRandColor(int fc, int bc)
    {
        Random random = new Random();
        if(fc > 255)
            fc = 255;
        if(bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    
    /**
	 * 生成字母数字混合型认证码.
	 * 
	 * @return 认证码图片对象
	 */
    public BufferedImage generate()        
    {   
        String captchaCode = this.getRandom();
        return this.generate(captchaCode);
    }      
    
    /**
	 * 生成数字型认证码.
	 * 
	 * @return 认证码图片对象
	 */
    public BufferedImage generateNum()        
    { 
        this.setCharset(StringRandom.CHARSET_NUM);  
        String captchaCode = this.getRandom();
        return this.generate(captchaCode);
    }
  
    
    /**
	 * 生成认证码图片.
	 * 
	 * @param captchaCode
	 *            认证码
	 * 
	 * @return 认证码图片对象
	 */
    public BufferedImage generate(String captchaCode)        
    {
    	if(Utility.isEmpty(captchaCode))
    		throw new java.lang.IllegalArgumentException("认证码不能为空！");
    	
    	this.captchaCode = captchaCode;
        captchaImage = new BufferedImage(width, height, 1);
        Graphics graphics = captchaImage.getGraphics();
        Random random = new Random();
        //设置背景色
        graphics.setColor(getRandColor(200, 250));
        graphics.fillRect(0, 0, width, height);
        //设置字体
        graphics.setFont(new Font("Times New Roman", Font.BOLD, 18));
        //设置花岗岩式背景色，不好看已禁用
//        graphics.setColor(getRandColor(160, 200));
//        for(int i = 0; i < 155; i++)
//        {
//            int x = random.nextInt(this.width);
//            int y = random.nextInt(this.height);
//            int xl = random.nextInt(12);
//            int yl = random.nextInt(12);
//            graphics.drawLine(x, y, x + xl, y + yl);
//        }   
        //设置随机干扰线条颜色 
//        graphics.setColor(this.getRandColor(50,100)); 
//        //产生20条干扰线条 
//        for (int i=0; i<5; i++){ 
//            int x1 = random.nextInt(this.width * this.captchaCode.length() - 4) + 2; 
//            int y1 = random.nextInt(this.height - 4) + 2; 
//            int x2 = random.nextInt(this.width * this.captchaCode.length() - 2 - x1) + x1; 
//            int y2 = y1; 
//            graphics.drawLine(x1, y1, x2, y2); 
//        } 
        //画认证码字符
        for(int i = 0; i < captchaCode.length(); i++)
        {
            graphics.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            graphics.drawString(captchaCode.substring(i, i + 1), 13 * i + 7, 16);
        }
        graphics.dispose();          
        return captchaImage;
    } 
    
    /**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
    public static void main(String[] args) throws Exception
    {
    	Captcha captchaCode = new Captcha();    	
    	captchaCode.setLength(4);
    	captchaCode.setCharset("CHARSET_LETTER_NUM");
    	BufferedImage image = captchaCode.generate(captchaCode.getRandom());
    	FileOutputStream out = new FileOutputStream("d:\\test.jpg");
		
    	ImageIO.write(image, "jpg", out);
		
    }


	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}


	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}


	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}


	/**
	 * @return the captchaCode
	 */
	public String getCaptchaCode()
	{
		return captchaCode;
	}


	/**
	 * @return the captchaImage
	 */
	public BufferedImage getCaptchaImage()
	{
		return captchaImage;
	}

}